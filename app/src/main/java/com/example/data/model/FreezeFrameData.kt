package com.example.data.model

data class FreezeFrameData(
    val triggerDtc: String = "P0171",
    val triggerSystem: DiagnosticSystem = DiagnosticSystem.ENGINE,
    val timestamp: Long = System.currentTimeMillis(),

    // Engine Core Freeze Frame
    val engineRpm: Int = 2450,
    val vehicleSpeedKmh: Int = 78,
    val engineLoadPercent: Float = 62.5f,
    val coolantTempCelsius: Int = 94,
    val intakeAirTempCelsius: Int = 38,
    val shortTermFuelTrimPercent: Float = 14.8f,
    val longTermFuelTrimPercent: Float = 19.5f,
    val manifoldPressureKpa: Float = 65.2f,
    val massAirFlowGps: Float = 28.4f,
    val throttlePositionPercent: Float = 34.0f,
    val batteryVoltage: Float = 13.9f,
    val fuelRailPressureBar: Float = 160.0f,
    val timingAdvanceDeg: Float = 18.5f,
    val o2Sensor1Voltage: Float = 0.18f,

    // Powertrain & Transmission Freeze Frame
    val transmissionFluidTempC: Int = 88,
    val gearAtFault: String = "D4",
    val calculatedGearRatio: Float = 1.05f,
    val torqueConverterSlipRpm: Int = 240,
    val tcmLinePressurePsi: Float = 142.0f,
    val turboBoostBar: Float = 0.95f,

    // Chassis & ABS Freeze Frame
    val wheelSpeedFlKmh: Float = 78.0f,
    val wheelSpeedFrKmh: Float = 78.2f,
    val wheelSpeedRlKmh: Float = 77.9f,
    val wheelSpeedRrKmh: Float = 78.1f,
    val brakeMasterCylinderPressureBar: Float = 14.5f,
    val steeringAngleDeg: Float = -2.8f,
    val yawRateDegPerSec: Float = 0.4f,
    val absPumpMotorVoltage: Float = 13.8f,

    // Electrical & BCM Freeze Frame
    val alternatorDutyPercent: Float = 68.0f,
    val bcmSupplyVoltage: Float = 13.7f,
    val cabinBusVoltage: Float = 13.8f,
    val parasiticDarkCurrentMa: Float = 38.0f,
    val smartKeyRssiDbm: Int = -56,
    val batteryHealthSoh: Float = 82.0f
)

