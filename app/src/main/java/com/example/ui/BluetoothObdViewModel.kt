package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.LiveSensorData
import com.example.data.model.TerminalLogEntry
import com.example.service.BleConnectionState
import com.example.service.BluetoothObdService
import com.example.service.BluetoothObdDevice
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class BluetoothObdViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothService = BluetoothObdService(application.applicationContext)

    val connectionState: StateFlow<BleConnectionState> = bluetoothService.connectionState
    val discoveredDevices: StateFlow<List<BluetoothObdDevice>> = bluetoothService.discoveredDevices
    val connectedDeviceName: StateFlow<String?> = bluetoothService.connectedDeviceName
    val terminalLogs: StateFlow<List<TerminalLogEntry>> = bluetoothService.terminalLogs

    private val _isDemoMode = MutableStateFlow(false)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode.asStateFlow()

    private val _telemetryData = MutableStateFlow(LiveSensorData.disconnected())
    val telemetryData: StateFlow<LiveSensorData> = _telemetryData.asStateFlow()

    private val _dtcList = MutableStateFlow<List<com.example.data.model.DtcCode>>(emptyList())
    val dtcList: StateFlow<List<com.example.data.model.DtcCode>> = _dtcList.asStateFlow()

    private val _isScanningDtcs = MutableStateFlow(false)
    val isScanningDtcs: StateFlow<Boolean> = _isScanningDtcs.asStateFlow()

    private var demoJob: Job? = null

    fun scanDtcCodes() {
        viewModelScope.launch {
            _isScanningDtcs.value = true
            if (_isDemoMode.value) {
                delay(1200)
                val demoCodes = listOf("P0171", "P0087", "P0401")
                val resultList = demoCodes.mapNotNull { code ->
                    com.example.data.repository.DtcDatabase.allCodes.find { it.code == code }
                }
                _dtcList.value = resultList
            } else {
                val codes = bluetoothService.fetchDtcCodes()
                val resultList = if (codes.isNotEmpty()) {
                    codes.map { code ->
                        com.example.data.repository.DtcDatabase.allCodes.find { it.code == code }
                            ?: com.example.data.model.DtcCode(
                                code = code,
                                category = com.example.data.model.DtcCategory.POWERTRAIN,
                                enTitle = "Trouble Code $code",
                                thTitle = "รหัสข้อผิดพลาด $code",
                                enDescription = "OBD-II Diagnostic Trouble Code $code detected on CAN bus.",
                                thDescription = "ตรวจพบรหัสวิเคราะห์ข้อผิดพลาด OBD-II $code ผ่านระบบ CAN bus",
                                severity = com.example.data.model.DtcSeverity.WARNING,
                                commonCausesTh = listOf("โปรดตรวจสอบระบบเซนเซอร์ที่เกี่ยวข้อง"),
                                commonCausesEn = listOf("Inspect sensor circuit and connections"),
                                symptomsTh = listOf("ไฟ Check Engine โชว์ที่หน้าปัด"),
                                symptomsEn = listOf("Check Engine Light illuminated"),
                                estimatedCostThb = "฿500 - ฿3,000",
                                diyDifficulty = "ปานกลาง",
                                affectedSystem = "Engine Control Unit (ECU)"
                            )
                    }
                } else {
                    emptyList()
                }
                _dtcList.value = resultList
            }
            _isScanningDtcs.value = false
        }
    }

    init {
        // Observe real BLE service telemetry
        viewModelScope.launch {
            bluetoothService.telemetryData.collect { data ->
                if (!_isDemoMode.value) {
                    _telemetryData.value = data
                }
            }
        }
    }

    fun startScan() {
        if (_isDemoMode.value) {
            // Provide simulated discovered devices in demo mode
            _isDemoMode.value = false
            demoJob?.cancel()
        }
        bluetoothService.startScan()
    }

    fun stopScan() {
        bluetoothService.stopScan()
    }

    fun connectDevice(device: BluetoothObdDevice) {
        bluetoothService.connect(device)
    }

    fun disconnect() {
        if (_isDemoMode.value) {
            toggleDemoMode(false)
        } else {
            bluetoothService.disconnect()
        }
    }

    fun sendCustomCommand(command: String) {
        viewModelScope.launch {
            if (_isDemoMode.value) {
                simulateCommandResponse(command)
            } else {
                bluetoothService.sendRawCommand(command)
            }
        }
    }

    fun clearDtcCodes() {
        viewModelScope.launch {
            if (_isDemoMode.value) {
                simulateCommandResponse("04")
            } else {
                bluetoothService.sendRawCommand("04")
            }
        }
    }

    fun toggleDemoMode(enable: Boolean? = null) {
        val targetState = enable ?: !_isDemoMode.value
        _isDemoMode.value = targetState

        if (targetState) {
            bluetoothService.disconnect()
            startDemoSimulation()
        } else {
            demoJob?.cancel()
            demoJob = null
            _telemetryData.value = LiveSensorData.disconnected()
        }
    }

    private fun startDemoSimulation() {
        demoJob?.cancel()
        demoJob = viewModelScope.launch {
            var simRpm = 850
            var simSpeed = 0
            var simCoolant = 82

            while (isActive) {
                simRpm = (simRpm + Random.nextInt(-30, 35)).coerceIn(750, 4200)
                simSpeed = if (simRpm > 1200) ((simRpm - 1000) / 25).coerceIn(0, 140) else 0
                simCoolant = (simCoolant + Random.nextInt(-1, 2)).coerceIn(80, 96)

                _telemetryData.value = LiveSensorData(
                    isConnected = true,
                    connectionMode = "BLE OBD-II Demo Simulator",
                    isEngineRunning = true,
                    rpm = simRpm,
                    speedKmh = simSpeed,
                    coolantTempC = simCoolant,
                    throttlePercent = (simSpeed * 0.6f).coerceIn(12f, 85f),
                    engineLoadPercent = (simRpm / 50f).coerceIn(15f, 90f),
                    batteryVoltage = 14.1f + Random.nextFloat() * 0.2f
                )

                delay(200)
            }
        }
    }

    private fun simulateCommandResponse(cmd: String) {
        val resp = when (cmd.uppercase().trim()) {
            "AT Z" -> "ELM327 v2.1"
            "0100" -> "41 00 BE 3F A8 13"
            "010C" -> "41 0C 1A F0"
            "010D" -> "41 0D 35"
            "04" -> "44 (DTC Cleared OK)"
            else -> "41 ${cmd.takeLast(2)} 00 00"
        }
        // Simulated terminal log entry if needed
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothService.disconnect()
        demoJob?.cancel()
    }
}
