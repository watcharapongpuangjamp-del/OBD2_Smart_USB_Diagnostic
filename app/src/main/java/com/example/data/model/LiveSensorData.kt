package com.example.data.model

data class LiveSensorData(
    val isConnected: Boolean = true,
    val connectionMode: String = "ELM327 Bluetooth (ISO 15765-4 CAN)",
    val isEngineRunning: Boolean = true,
    
    // Core Powertrain / Engine PIDs
    val rpm: Int = 1850,
    val speedKmh: Int = 65,
    val coolantTempC: Int = 89,
    val engineLoadPercent: Float = 42.0f,
    val throttlePercent: Float = 28.5f,
    val mafGps: Float = 16.8f,
    val boostPressureBar: Float = 0.65f, // Commonrail diesel turbo boost
    val batteryVoltage: Float = 14.1f,
    val stftPercent: Float = 1.6f,
    val ltftPercent: Float = 3.2f,
    val intakeAirTempC: Int = 34,
    val o2Sensor1Voltage: Float = 0.65f,
    val fuelRailPressureBar: Float = 380.0f,
    val instantKmPerLiter: Float = 14.8f,
    val instantLPer100Km: Float = 6.75f,
    val catalystTempC: Int = 420,
    val oilTempC: Int = 92,
    val engineOilPressureBar: Float = 3.8f,
    val oilLifeRemainingPercent: Float = 72.0f,

    // Transmission (TCM) PIDs
    val transmissionFluidTempC: Int = 78,
    val currentGear: String = "D4",
    val gearRatio: Float = 1.02f,
    val torqueConverterSlipRpm: Int = 18,
    val tcmLinePressurePsi: Float = 125.0f,
    val turbineInputSpeedRpm: Int = 1832,
    val tcmClutchLockupStatus: String = "LOCKED",
    val transmissionOilLifePercent: Float = 65.0f,

    // ABS & Chassis (EBCM) PIDs
    val wheelSpeedFlKmh: Float = 65.0f,
    val wheelSpeedFrKmh: Float = 65.2f,
    val wheelSpeedRlKmh: Float = 64.9f,
    val wheelSpeedRrKmh: Float = 65.0f,
    val brakeFluidPressureBar: Float = 0.0f,
    val steeringAngleDeg: Float = 1.2f,
    val yawRateDegPerSec: Float = 0.1f,
    val lateralGForce: Float = 0.02f,
    val absPumpMotorVoltage: Float = 13.9f,
    val absBrakeStatus: String = "STANDBY_READY",
    val brakePadRemainingPercent: Float = 68.0f,

    // Airbag & Restraints (SRS) PIDs
    val driverAirbagLoopResistanceOhm: Float = 2.4f,
    val passengerAirbagLoopResistanceOhm: Float = 2.3f,
    val driverPretensionerResistanceOhm: Float = 2.2f,
    val passengerPretensionerResistanceOhm: Float = 2.2f,
    val srsBatterySupplyVoltage: Float = 14.1f,
    val frontImpactSensor1Status: String = "NORMAL",
    val clockspringIntegrity: String = "PASS",

    // Body Control & Electrical (BCM) PIDs
    val bcmSupplyVoltage: Float = 14.1f,
    val alternatorDutyPercent: Float = 62.0f,
    val cabinBusVoltage: Float = 13.8f,
    val headlightCurrentAmp: Float = 7.2f,
    val smartKeySignalRssi: Int = -52, // dBm
    val ambientTemperatureC: Int = 33,
    val doorLockActuatorState: String = "LOCKED",
    val batteryHealthSohPercent: Float = 84.0f
) {
    companion object {
        fun disconnected(messageTh: String = "ยังไม่ได้เชื่อมต่อสาย OBD2 USB", messageEn: String = "OBD2 USB Not Connected"): LiveSensorData {
            return LiveSensorData(
                isConnected = false,
                connectionMode = "Disconnected",
                isEngineRunning = false,
                rpm = 0,
                speedKmh = 0,
                coolantTempC = 0,
                engineLoadPercent = 0.0f,
                throttlePercent = 0.0f,
                mafGps = 0.0f,
                boostPressureBar = 0.0f,
                batteryVoltage = 0.0f,
                stftPercent = 0.0f,
                ltftPercent = 0.0f,
                intakeAirTempC = 0,
                o2Sensor1Voltage = 0.0f,
                fuelRailPressureBar = 0.0f,
                instantKmPerLiter = 0.0f,
                instantLPer100Km = 0.0f,
                catalystTempC = 0,
                oilTempC = 0,
                engineOilPressureBar = 0.0f,
                oilLifeRemainingPercent = 0.0f,
                transmissionFluidTempC = 0,
                currentGear = "P",
                gearRatio = 0.0f,
                torqueConverterSlipRpm = 0,
                tcmLinePressurePsi = 0.0f,
                turbineInputSpeedRpm = 0,
                tcmClutchLockupStatus = "DISCONNECTED",
                transmissionOilLifePercent = 0.0f,
                wheelSpeedFlKmh = 0.0f,
                wheelSpeedFrKmh = 0.0f,
                wheelSpeedRlKmh = 0.0f,
                wheelSpeedRrKmh = 0.0f,
                brakeFluidPressureBar = 0.0f,
                steeringAngleDeg = 0.0f,
                yawRateDegPerSec = 0.0f,
                lateralGForce = 0.0f,
                absPumpMotorVoltage = 0.0f,
                absBrakeStatus = "OFFLINE",
                brakePadRemainingPercent = 0.0f,
                driverAirbagLoopResistanceOhm = 0.0f,
                passengerAirbagLoopResistanceOhm = 0.0f,
                driverPretensionerResistanceOhm = 0.0f,
                passengerPretensionerResistanceOhm = 0.0f,
                srsBatterySupplyVoltage = 0.0f,
                frontImpactSensor1Status = "OFFLINE",
                clockspringIntegrity = "UNKNOWN",
                bcmSupplyVoltage = 0.0f,
                alternatorDutyPercent = 0.0f,
                cabinBusVoltage = 0.0f,
                headlightCurrentAmp = 0.0f,
                smartKeySignalRssi = 0,
                ambientTemperatureC = 0,
                doorLockActuatorState = "UNKNOWN",
                batteryHealthSohPercent = 0.0f
            )
        }

        fun simulatedDefault(): LiveSensorData {
            return LiveSensorData(
                isConnected = true,
                connectionMode = "Virtual CAN Simulator Engine",
                isEngineRunning = true
            )
        }
    }
}

