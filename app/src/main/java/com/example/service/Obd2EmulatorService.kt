package com.example.service

import com.example.data.model.FreezeFrameData
import com.example.data.model.LiveSensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

enum class DrivingMode {
    IDLE,
    CRUISE,
    HARD_ACCEL,
    OVERHEAT_WARNING
}

class Obd2EmulatorService(
    private val scope: CoroutineScope
) {
    private val _sensorData = MutableStateFlow(LiveSensorData())
    val sensorData: StateFlow<LiveSensorData> = _sensorData.asStateFlow()

    private val _currentDrivingMode = MutableStateFlow(DrivingMode.CRUISE)
    val currentDrivingMode: StateFlow<DrivingMode> = _currentDrivingMode.asStateFlow()

    private val _freezeFrame = MutableStateFlow<FreezeFrameData?>(FreezeFrameData())
    val freezeFrame: StateFlow<FreezeFrameData?> = _freezeFrame.asStateFlow()

    private var telemetryJob: Job? = null
    private var tick = 0

    init {
        // Telemetry simulation is started when simulator mode is active
    }

    fun stopTelemetrySimulation() {
        telemetryJob?.cancel()
        telemetryJob = null
    }

    fun startTelemetrySimulation() {
        telemetryJob?.cancel()
        _sensorData.value = LiveSensorData.simulatedDefault()
        telemetryJob = scope.launch(Dispatchers.Default) {
            while (isActive) {
                tick++
                val current = _sensorData.value
                val mode = _currentDrivingMode.value

                val newSensorState = when (mode) {
                    DrivingMode.IDLE -> {
                        val baseRpm = 750 + Random.nextInt(-15, 20)
                        val sineO2 = (0.45f + 0.35f * sin(tick * 0.4)).toFloat()
                        current.copy(
                            rpm = baseRpm,
                            speedKmh = 0,
                            coolantTempC = 88,
                            engineLoadPercent = 18.5f + Random.nextFloat() * 1.5f,
                            throttlePercent = 11.5f + Random.nextFloat() * 1.0f,
                            mafGps = 3.2f + Random.nextFloat() * 0.3f,
                            boostPressureBar = 0.04f,
                            batteryVoltage = 14.2f + Random.nextFloat() * 0.1f,
                            stftPercent = (-0.8f + Random.nextFloat() * 1.6f),
                            ltftPercent = 2.1f,
                            o2Sensor1Voltage = sineO2,
                            instantKmPerLiter = 0.0f,
                            instantLPer100Km = 0.85f,
                            fuelRailPressureBar = 320.0f + Random.nextFloat() * 10f,
                            engineOilPressureBar = 2.1f + Random.nextFloat() * 0.1f,
                            oilTempC = 86,
                            transmissionFluidTempC = 74,
                            currentGear = "P / N",
                            gearRatio = 0.0f,
                            torqueConverterSlipRpm = 750,
                            tcmLinePressurePsi = 65.0f + Random.nextFloat() * 3f,
                            turbineInputSpeedRpm = 0,
                            tcmClutchLockupStatus = "OPEN",
                            wheelSpeedFlKmh = 0.0f,
                            wheelSpeedFrKmh = 0.0f,
                            wheelSpeedRlKmh = 0.0f,
                            wheelSpeedRrKmh = 0.0f,
                            brakeFluidPressureBar = 1.2f,
                            steeringAngleDeg = 0.0f,
                            absPumpMotorVoltage = 14.1f,
                            driverAirbagLoopResistanceOhm = 2.38f + Random.nextFloat() * 0.04f,
                            passengerAirbagLoopResistanceOhm = 2.32f + Random.nextFloat() * 0.03f,
                            bcmSupplyVoltage = 14.15f,
                            alternatorDutyPercent = 48.0f + Random.nextFloat() * 3f,
                            headlightCurrentAmp = 6.8f,
                            ambientTemperatureC = 33
                        )
                    }
                    DrivingMode.CRUISE -> {
                        val targetRpm = 1850 + (80 * sin(tick * 0.2)).toInt() + Random.nextInt(-10, 10)
                        val targetSpeed = 82 + (4 * sin(tick * 0.15)).toInt()
                        val sineO2 = (0.5f + 0.4f * sin(tick * 0.6)).toFloat()
                        current.copy(
                            rpm = targetRpm,
                            speedKmh = targetSpeed,
                            coolantTempC = 90,
                            engineLoadPercent = 36.0f + (4f * sin(tick * 0.2)).toFloat(),
                            throttlePercent = 22.0f + (2f * sin(tick * 0.2)).toFloat(),
                            mafGps = 18.2f + Random.nextFloat() * 1.0f,
                            boostPressureBar = 0.72f + (0.08f * sin(tick * 0.3)).toFloat(),
                            batteryVoltage = 14.1f + Random.nextFloat() * 0.1f,
                            stftPercent = 1.2f + Random.nextFloat() * 1.2f,
                            ltftPercent = 3.1f,
                            o2Sensor1Voltage = sineO2,
                            instantKmPerLiter = 16.2f + Random.nextFloat() * 0.8f,
                            instantLPer100Km = 6.17f,
                            fuelRailPressureBar = 580.0f + Random.nextFloat() * 15f,
                            engineOilPressureBar = 3.9f + Random.nextFloat() * 0.2f,
                            oilTempC = 92,
                            transmissionFluidTempC = 82,
                            currentGear = "D5 (Cruising)",
                            gearRatio = 0.82f,
                            torqueConverterSlipRpm = 12 + Random.nextInt(0, 8),
                            tcmLinePressurePsi = 135.0f + Random.nextFloat() * 5f,
                            turbineInputSpeedRpm = targetRpm - 15,
                            tcmClutchLockupStatus = "LOCKED",
                            wheelSpeedFlKmh = targetSpeed.toFloat(),
                            wheelSpeedFrKmh = targetSpeed.toFloat() + 0.1f,
                            wheelSpeedRlKmh = targetSpeed.toFloat() - 0.1f,
                            wheelSpeedRrKmh = targetSpeed.toFloat(),
                            brakeFluidPressureBar = 0.0f,
                            steeringAngleDeg = (0.8f * sin(tick * 0.1)).toFloat(),
                            absPumpMotorVoltage = 14.0f,
                            driverAirbagLoopResistanceOhm = 2.40f + Random.nextFloat() * 0.03f,
                            passengerAirbagLoopResistanceOhm = 2.34f + Random.nextFloat() * 0.02f,
                            bcmSupplyVoltage = 14.10f,
                            alternatorDutyPercent = 64.0f + Random.nextFloat() * 2f,
                            headlightCurrentAmp = 7.1f,
                            ambientTemperatureC = 34
                        )
                    }
                    DrivingMode.HARD_ACCEL -> {
                        val targetRpm = 3500 + (250 * sin(tick * 0.5)).toInt()
                        val targetSpeed = 118 + (8 * sin(tick * 0.3)).toInt()
                        current.copy(
                            rpm = targetRpm,
                            speedKmh = targetSpeed,
                            coolantTempC = 95,
                            engineLoadPercent = 88.0f + Random.nextFloat() * 6f,
                            throttlePercent = 82.0f + Random.nextFloat() * 4f,
                            mafGps = 68.0f + Random.nextFloat() * 4f,
                            boostPressureBar = 1.72f + Random.nextFloat() * 0.1f,
                            batteryVoltage = 13.9f + Random.nextFloat() * 0.1f,
                            stftPercent = 8.8f + Random.nextFloat() * 2f,
                            ltftPercent = 5.9f,
                            o2Sensor1Voltage = 0.91f,
                            instantKmPerLiter = 7.8f,
                            instantLPer100Km = 12.8f,
                            fuelRailPressureBar = 1320.0f + Random.nextFloat() * 40f,
                            engineOilPressureBar = 5.4f + Random.nextFloat() * 0.3f,
                            oilTempC = 98,
                            transmissionFluidTempC = 91,
                            currentGear = "D3 (Kickdown)",
                            gearRatio = 1.48f,
                            torqueConverterSlipRpm = 140 + Random.nextInt(0, 30),
                            tcmLinePressurePsi = 195.0f + Random.nextFloat() * 10f,
                            turbineInputSpeedRpm = targetRpm - 120,
                            tcmClutchLockupStatus = "SLIP_ENGAGE",
                            wheelSpeedFlKmh = targetSpeed.toFloat() + 0.4f,
                            wheelSpeedFrKmh = targetSpeed.toFloat() + 0.3f,
                            wheelSpeedRlKmh = targetSpeed.toFloat(),
                            wheelSpeedRrKmh = targetSpeed.toFloat(),
                            brakeFluidPressureBar = 0.0f,
                            steeringAngleDeg = (1.5f * sin(tick * 0.2)).toFloat(),
                            absPumpMotorVoltage = 13.85f,
                            driverAirbagLoopResistanceOhm = 2.42f + Random.nextFloat() * 0.04f,
                            passengerAirbagLoopResistanceOhm = 2.35f + Random.nextFloat() * 0.03f,
                            bcmSupplyVoltage = 13.95f,
                            alternatorDutyPercent = 86.0f + Random.nextFloat() * 4f,
                            headlightCurrentAmp = 7.3f,
                            ambientTemperatureC = 34
                        )
                    }
                    DrivingMode.OVERHEAT_WARNING -> {
                        current.copy(
                            rpm = 2300,
                            speedKmh = 52,
                            coolantTempC = 114 + Random.nextInt(0, 3),
                            engineLoadPercent = 65.0f,
                            throttlePercent = 38.0f,
                            mafGps = 26.0f,
                            boostPressureBar = 0.45f,
                            batteryVoltage = 13.5f,
                            stftPercent = 14.0f,
                            ltftPercent = 16.5f,
                            instantKmPerLiter = 9.5f,
                            instantLPer100Km = 10.5f,
                            fuelRailPressureBar = 440.0f,
                            engineOilPressureBar = 1.9f, // Low oil pressure due to extreme heat thinning
                            oilTempC = 122,
                            transmissionFluidTempC = 108,
                            currentGear = "D2 (Limp Mode)",
                            gearRatio = 1.95f,
                            torqueConverterSlipRpm = 320,
                            tcmLinePressurePsi = 110.0f,
                            tcmClutchLockupStatus = "UNLOCKED_PROTECT",
                            wheelSpeedFlKmh = 52.0f,
                            wheelSpeedFrKmh = 52.0f,
                            wheelSpeedRlKmh = 51.8f,
                            wheelSpeedRrKmh = 51.9f,
                            absPumpMotorVoltage = 13.5f,
                            bcmSupplyVoltage = 13.45f,
                            alternatorDutyPercent = 95.0f,
                            ambientTemperatureC = 37
                        )
                    }
                }

                _sensorData.value = newSensorState
                delay(300) // 3.3 Hz update rate
            }
        }
    }

    fun setDrivingMode(mode: DrivingMode) {
        _currentDrivingMode.value = mode
    }

    fun toggleEnginePower() {
        val curr = _sensorData.value
        val newState = !curr.isEngineRunning
        _sensorData.value = curr.copy(
            isEngineRunning = newState,
            rpm = if (newState) 800 else 0,
            speedKmh = if (newState) curr.speedKmh else 0,
            batteryVoltage = if (newState) 14.1f else 12.4f,
            engineOilPressureBar = if (newState) 2.1f else 0.0f
        )
    }

    fun clearDtcAndResetEcu() {
        _freezeFrame.value = null
        val curr = _sensorData.value
        _sensorData.value = curr.copy(
            stftPercent = 0.0f,
            ltftPercent = 0.0f
        )
    }

    fun triggerSimulatedFault(dtcCode: String) {
        val curr = _sensorData.value
        _freezeFrame.value = FreezeFrameData(
            triggerDtc = dtcCode,
            timestamp = System.currentTimeMillis(),
            engineRpm = curr.rpm,
            vehicleSpeedKmh = curr.speedKmh,
            engineLoadPercent = curr.engineLoadPercent,
            coolantTempCelsius = curr.coolantTempC,
            intakeAirTempCelsius = curr.intakeAirTempC,
            shortTermFuelTrimPercent = curr.stftPercent,
            longTermFuelTrimPercent = curr.ltftPercent,
            manifoldPressureKpa = 68.0f,
            massAirFlowGps = curr.mafGps,
            throttlePositionPercent = curr.throttlePercent,
            batteryVoltage = curr.batteryVoltage,
            fuelRailPressureBar = curr.fuelRailPressureBar
        )
    }
}
