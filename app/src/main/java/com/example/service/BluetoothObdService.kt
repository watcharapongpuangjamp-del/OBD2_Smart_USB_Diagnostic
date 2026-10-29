package com.example.service

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.data.model.LiveSensorData
import com.example.data.model.TerminalLogEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

enum class BleConnectionState {
    DISCONNECTED,
    SCANNING,
    CONNECTING,
    CONNECTED,
    ERROR
}

data class BluetoothObdDevice(
    val name: String,
    val address: String,
    val rssi: Int = 0,
    val isBle: Boolean = true,
    val device: BluetoothDevice? = null
)

class BluetoothObdService(private val context: Context) {

    private val bluetoothManager: BluetoothManager? by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    }

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        bluetoothManager?.adapter
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    private val _connectionState = MutableStateFlow(BleConnectionState.DISCONNECTED)
    val connectionState: StateFlow<BleConnectionState> = _connectionState.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<BluetoothObdDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<BluetoothObdDevice>> = _discoveredDevices.asStateFlow()

    private val _telemetryData = MutableStateFlow(LiveSensorData())
    val telemetryData: StateFlow<LiveSensorData> = _telemetryData.asStateFlow()

    private val _terminalLogs = MutableStateFlow<List<TerminalLogEntry>>(emptyList())
    val terminalLogs: StateFlow<List<TerminalLogEntry>> = _terminalLogs.asStateFlow()

    private val _connectedDeviceName = MutableStateFlow<String?> (null)
    val connectedDeviceName: StateFlow<String?> = _connectedDeviceName.asStateFlow()

    private var activeGatt: BluetoothGatt? = null
    private var rxCharacteristic: BluetoothGattCharacteristic? = null
    private var txCharacteristic: BluetoothGattCharacteristic? = null

    private var rfcommSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    private var isBleConnection = true
    private var pollingJob: Job? = null
    private val responseBuffer = StringBuilder()

    // Standard SPP UUID for Bluetooth Classic
    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    // Common BLE UART Service and Characteristic UUIDs for ELM327 / Vgate / OBDLink adapters
    private val UART_SERVICE_UUIDS = listOf(
        UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB"), // HM-10 / ELM327 BLE
        UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB"),
        UUID.fromString("0000e0ff-3c17-d293-8e48-14fe2e4da212"), // vLinker
        UUID.fromString("49535343-fe7d-4ae5-8fa9-9fafd205e455")  // Microchip transparent UART
    )

    private val UART_CHAR_UUIDS = listOf(
        UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB"),
        UUID.fromString("0000FFF1-0000-1000-8000-00805F9B34FB"),
        UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616")
    )

    private val CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            logTerminal("Bluetooth is disabled or not available", isTx = false, isSuccess = false)
            _connectionState.value = BleConnectionState.ERROR
            return
        }

        _discoveredDevices.value = emptyList()
        _connectionState.value = BleConnectionState.SCANNING

        // Include paired Bluetooth Classic devices
        val pairedList = mutableListOf<BluetoothObdDevice>()
        try {
            bluetoothAdapter?.bondedDevices?.forEach { dev ->
                if (isObdCandidate(dev.name)) {
                    pairedList.add(
                        BluetoothObdDevice(
                            name = dev.name ?: "Unknown OBD (Paired)",
                            address = dev.address,
                            rssi = -50,
                            isBle = false,
                            device = dev
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("BluetoothObdService", "Error reading paired devices", e)
        }

        _discoveredDevices.value = pairedList

        // Start BLE Scanner
        val scanner = bluetoothAdapter?.bluetoothLeScanner
        if (scanner == null) {
            Log.w("BluetoothObdService", "BLE Scanner unavailable")
            return
        }

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        try {
            scanner.startScan(null, scanSettings, leScanCallback)
        } catch (e: Exception) {
            Log.e("BluetoothObdService", "Error starting BLE scan", e)
        }

        serviceScope.launch {
            delay(10000) // Scan for 10s
            stopScan()
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        if (_connectionState.value == BleConnectionState.SCANNING) {
            try {
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
            } catch (e: Exception) {
                Log.e("BluetoothObdService", "Error stopping BLE scan", e)
            }
            _connectionState.value = BleConnectionState.DISCONNECTED
        }
    }

    private val leScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.device?.let { dev ->
                val deviceName = dev.name ?: result.scanRecord?.deviceName ?: "Unknown BLE Device"
                val address = dev.address

                val current = _discoveredDevices.value.toMutableList()
                if (current.none { it.address == address }) {
                    current.add(
                        BluetoothObdDevice(
                            name = deviceName,
                            address = address,
                            rssi = result.rssi,
                            isBle = true,
                            device = dev
                        )
                    )
                    _discoveredDevices.value = current
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BluetoothObdService", "BLE scan failed code: $errorCode")
        }
    }

    @SuppressLint("MissingPermission")
    fun connect(obdDevice: BluetoothObdDevice) {
        stopScan()
        _connectionState.value = BleConnectionState.CONNECTING
        _connectedDeviceName.value = obdDevice.name
        isBleConnection = obdDevice.isBle

        logTerminal("Initiating connection to ${obdDevice.name} (${obdDevice.address})", isTx = true)

        if (obdDevice.isBle) {
            val dev = obdDevice.device ?: bluetoothAdapter?.getRemoteDevice(obdDevice.address)
            if (dev == null) {
                _connectionState.value = BleConnectionState.ERROR
                logTerminal("Device handle null for ${obdDevice.address}", isTx = false, isSuccess = false)
                return
            }
            activeGatt = dev.connectGatt(context, false, gattCallback)
        } else {
            // Classic Bluetooth RFCOMM Connection
            serviceScope.launch(Dispatchers.IO) {
                try {
                    val dev = obdDevice.device ?: bluetoothAdapter?.getRemoteDevice(obdDevice.address)
                    rfcommSocket = dev?.createRfcommSocketToServiceRecord(SPP_UUID)
                    rfcommSocket?.connect()
                    inputStream = rfcommSocket?.inputStream
                    outputStream = rfcommSocket?.outputStream

                    _connectionState.value = BleConnectionState.CONNECTED
                    logTerminal("Classic Bluetooth RFCOMM connected successfully!", isTx = false)
                    initElm327Adapter()
                } catch (e: Exception) {
                    Log.e("BluetoothObdService", "RFCOMM Connection error", e)
                    _connectionState.value = BleConnectionState.ERROR
                    logTerminal("Classic BT connection failed: ${e.message}", isTx = false, isSuccess = false)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        pollingJob?.cancel()
        pollingJob = null

        try {
            activeGatt?.disconnect()
            activeGatt?.close()
            activeGatt = null
        } catch (e: Exception) {
            Log.e("BluetoothObdService", "Gatt disconnect error", e)
        }

        try {
            inputStream?.close()
            outputStream?.close()
            rfcommSocket?.close()
            rfcommSocket = null
        } catch (e: Exception) {
            Log.e("BluetoothObdService", "Socket close error", e)
        }

        _connectedDeviceName.value = null
        _connectionState.value = BleConnectionState.DISCONNECTED
        logTerminal("Disconnected from Bluetooth OBD-II device", isTx = false)
    }

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                logTerminal("GATT Connected. Discovering services...", isTx = false)
                gatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                logTerminal("GATT Disconnected", isTx = false)
                _connectionState.value = BleConnectionState.DISCONNECTED
                _connectedDeviceName.value = null
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS && gatt != null) {
                var foundService: BluetoothGattService? = null
                var foundChar: BluetoothGattCharacteristic? = null

                for (serviceUuid in UART_SERVICE_UUIDS) {
                    val s = gatt.getService(serviceUuid)
                    if (s != null) {
                        foundService = s
                        for (charUuid in UART_CHAR_UUIDS) {
                            val c = s.getCharacteristic(charUuid)
                            if (c != null) {
                                foundChar = c
                                break
                            }
                        }
                        if (foundChar != null) break
                    }
                }

                if (foundChar == null) {
                    // Fallback to first write/notify characteristic
                    gatt.services?.forEach { s ->
                        s.characteristics?.forEach { c ->
                            if ((c.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0 ||
                                (c.properties and BluetoothGattCharacteristic.PROPERTY_WRITE) != 0
                            ) {
                                foundChar = c
                                foundService = s
                            }
                        }
                    }
                }

                if (foundChar != null) {
                    txCharacteristic = foundChar
                    rxCharacteristic = foundChar
                    gatt.setCharacteristicNotification(foundChar, true)

                    val descriptor = foundChar?.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG)
                    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    descriptor?.let { gatt.writeDescriptor(it) }

                    _connectionState.value = BleConnectionState.CONNECTED
                    logTerminal("BLE GATT Services ready!", isTx = false)

                    serviceScope.launch {
                        delay(500)
                        initElm327Adapter()
                    }
                } else {
                    logTerminal("No compatible UART BLE Service found on adapter", isTx = false, isSuccess = false)
                    _connectionState.value = BleConnectionState.ERROR
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            val data = characteristic?.value ?: return
            val text = String(data)
            synchronized(responseBuffer) {
                responseBuffer.append(text)
            }
        }
    }

    private suspend fun initElm327Adapter() {
        val initCmds = listOf(
            "AT Z",      // Reset
            "AT E0",     // Echo off
            "AT L0",     // Linefeed off
            "AT S0",     // Spaces off
            "AT SP 0"    // Auto protocol
        )

        for (cmd in initCmds) {
            sendRawCommandInternal(cmd)
            delay(250)
        }

        logTerminal("ELM327 Initialization Complete. Starting live PID stream...", isTx = false)
        startTelemetryPolling()
    }

    private fun startTelemetryPolling() {
        pollingJob?.cancel()
        pollingJob = serviceScope.launch {
            while (isActive && _connectionState.value == BleConnectionState.CONNECTED) {
                // Poll Engine RPM (01 0C)
                val rpmResp = sendRawCommandInternal("010C")
                val rpm = parseRpm(rpmResp)

                // Poll Vehicle Speed (01 0D)
                val speedResp = sendRawCommandInternal("010D")
                val speed = parseSpeed(speedResp)

                // Poll Coolant Temp (01 05)
                val coolantResp = sendRawCommandInternal("0105")
                val coolant = parseCoolant(coolantResp)

                // Poll Throttle Position (01 11)
                val throttleResp = sendRawCommandInternal("0111")
                val throttle = parseThrottle(throttleResp)

                // Poll Engine Load (01 04)
                val loadResp = sendRawCommandInternal("0104")
                val load = parseEngineLoad(loadResp)

                // Poll Battery Voltage (AT RV)
                val voltResp = sendRawCommandInternal("AT RV")
                val voltage = parseVoltage(voltResp)

                val current = _telemetryData.value
                _telemetryData.value = current.copy(
                    isConnected = true,
                    connectionMode = "ELM327 Bluetooth",
                    isEngineRunning = rpm > 300,
                    rpm = if (rpm > 0) rpm else current.rpm,
                    speedKmh = if (speed >= 0) speed else current.speedKmh,
                    coolantTempC = if (coolant != -999) coolant else current.coolantTempC,
                    throttlePercent = if (throttle >= 0) throttle.toFloat() else current.throttlePercent,
                    engineLoadPercent = if (load >= 0) load.toFloat() else current.engineLoadPercent,
                    batteryVoltage = if (voltage > 0f) voltage else current.batteryVoltage
                )

                delay(100) // 10 Hz polling loop
            }
        }
    }

    suspend fun fetchDtcCodes(): List<String> = withContext(Dispatchers.IO) {
        val storedResp = sendRawCommandInternal("03")
        val pendingResp = sendRawCommandInternal("07")
        val dtcs = mutableSetOf<String>()
        dtcs.addAll(parseDtcResponse(storedResp))
        dtcs.addAll(parseDtcResponse(pendingResp))
        return@withContext dtcs.toList()
    }

    fun parseDtcResponse(resp: String): List<String> {
        val dtcs = mutableListOf<String>()
        val clean = resp.replace("\r", " ").replace("\n", " ").replace(">", " ").trim()
        val tokens = clean.split("\\s+".toRegex()).filter { it.length == 2 && it.toIntOrNull(16) != null }
        
        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            if (token == "43" || token == "47") {
                i++
                if (i < tokens.size && tokens[i].toIntOrNull(16) != null && tokens.size - i >= 2 && tokens[i].toInt(16) <= 6) {
                    i++
                }
                while (i + 1 < tokens.size) {
                    val b1 = tokens[i].toInt(16)
                    val b2 = tokens[i + 1].toInt(16)
                    i += 2
                    if (b1 == 0 && b2 == 0) continue
                    
                    val prefixChar = when ((b1 and 0xC0) shr 6) {
                        0 -> 'P'
                        1 -> 'C'
                        2 -> 'B'
                        3 -> 'U'
                        else -> 'P'
                    }
                    val digit2 = (b1 and 0x30) shr 4
                    val digit3 = b1 and 0x0F
                    val digit4 = (b2 and 0xF0) shr 4
                    val digit5 = b2 and 0x0F
                    
                    val code = "$prefixChar$digit2${digit3.toString(16).uppercase()}${digit4.toString(16).uppercase()}${digit5.toString(16).uppercase()}"
                    dtcs.add(code)
                }
                break
            }
            i++
        }
        return dtcs.distinct()
    }

    suspend fun sendRawCommand(command: String): String = withContext(Dispatchers.IO) {
        sendRawCommandInternal(command)
    }

    @SuppressLint("MissingPermission")
    private suspend fun sendRawCommandInternal(command: String): String = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val fullCmd = "$command\r"
        synchronized(responseBuffer) {
            responseBuffer.clear()
        }

        try {
            if (isBleConnection) {
                val char = txCharacteristic
                val gatt = activeGatt
                if (char != null && gatt != null) {
                    char.value = fullCmd.toByteArray()
                    gatt.writeCharacteristic(char)
                }
            } else {
                outputStream?.write(fullCmd.toByteArray())
                outputStream?.flush()
            }
        } catch (e: Exception) {
            Log.e("BluetoothObdService", "Command write error: $command", e)
        }

        // Wait for response terminated by '>' or timeout
        var elapsed = 0L
        val timeout = 1200L
        val builder = StringBuilder()

        while (elapsed < timeout) {
            delay(30)
            elapsed += 30

            if (!isBleConnection && inputStream != null) {
                try {
                    while (inputStream!!.available() > 0) {
                        val b = inputStream!!.read()
                        if (b != -1) {
                            builder.append(b.toChar())
                        }
                    }
                } catch (e: Exception) {
                    break
                }
            } else {
                synchronized(responseBuffer) {
                    if (responseBuffer.isNotEmpty()) {
                        builder.append(responseBuffer)
                        responseBuffer.clear()
                    }
                }
            }

            if (builder.contains(">")) {
                break
            }
        }

        val rawResp = builder.toString().replace(">", "").trim()
        val latency = System.currentTimeMillis() - startTime

        logTerminal(
            cmd = command,
            resp = rawResp,
            latency = latency,
            isTx = true
        )

        return@withContext rawResp
    }

    private fun parseRpm(resp: String): Int {
        val clean = cleanHexResponse(resp, "410C")
        val bytes = parseHexBytes(clean)
        return if (bytes.size >= 2) {
            ((bytes[0] * 256) + bytes[1]) / 4
        } else 0
    }

    private fun parseSpeed(resp: String): Int {
        val clean = cleanHexResponse(resp, "410D")
        val bytes = parseHexBytes(clean)
        return if (bytes.isNotEmpty()) bytes[0] else -1
    }

    private fun parseCoolant(resp: String): Int {
        val clean = cleanHexResponse(resp, "4105")
        val bytes = parseHexBytes(clean)
        return if (bytes.isNotEmpty()) bytes[0] - 40 else -999
    }

    private fun parseThrottle(resp: String): Int {
        val clean = cleanHexResponse(resp, "4111")
        val bytes = parseHexBytes(clean)
        return if (bytes.isNotEmpty()) (bytes[0] * 100) / 255 else -1
    }

    private fun parseEngineLoad(resp: String): Int {
        val clean = cleanHexResponse(resp, "4104")
        val bytes = parseHexBytes(clean)
        return if (bytes.isNotEmpty()) (bytes[0] * 100) / 255 else -1
    }

    private fun parseVoltage(resp: String): Float {
        val clean = resp.replace("V", "").trim()
        return clean.toFloatOrNull() ?: 12.6f
    }

    private fun cleanHexResponse(resp: String, header: String): String {
        val idx = resp.indexOf(header)
        return if (idx != -1) {
            resp.substring(idx + header.length).replace(" ", "").trim()
        } else {
            resp.replace(" ", "").trim()
        }
    }

    private fun parseHexBytes(hexStr: String): List<Int> {
        val list = mutableListOf<Int>()
        var i = 0
        while (i + 1 < hexStr.length) {
            val byteHex = hexStr.substring(i, i + 2)
            byteHex.toIntOrNull(16)?.let { list.add(it) }
            i += 2
        }
        return list
    }

    private fun isObdCandidate(name: String?): Boolean {
        if (name == null) return false
        val n = name.uppercase()
        return n.contains("OBD") || n.contains("ELM") || n.contains("VGATE") ||
                n.contains("VLINKER") || n.contains("CAR") || n.contains("BT") ||
                n.contains("SCAN") || n.contains("ECU")
    }

    private fun logTerminal(
        cmd: String,
        resp: String = "",
        latency: Long = 0,
        isTx: Boolean = true,
        isSuccess: Boolean = true
    ) {
        val entry = TerminalLogEntry(
            isTx = isTx,
            command = cmd,
            rawResponse = resp,
            latencyMs = latency,
            isSuccess = isSuccess
        )
        val logs = _terminalLogs.value.toMutableList()
        if (logs.size > 100) logs.removeAt(0)
        logs.add(entry)
        _terminalLogs.value = logs
    }
}
