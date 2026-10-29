package com.example.domain.model

/**
 * Severity levels for automotive diagnostic trouble codes.
 */
enum class DtcSeverity {
    INFO,
    WARNING,
    CRITICAL
}

/**
 * Automotive vehicle diagnostic system categories (SAE J2012 / ISO 15031-6).
 */
enum class VehicleSystemCategory {
    POWERTRAIN, // P-codes
    CHASSIS,    // C-codes
    BODY,       // B-codes
    NETWORK     // U-codes
}

/**
 * Pure Kotlin domain model representing an OBD-II Diagnostic Trouble Code (DTC).
 * Completely decoupled from Android framework and Room annotations.
 */
data class DtcFault(
    val code: String,
    val category: VehicleSystemCategory,
    val titleTh: String,
    val titleEn: String,
    val severity: DtcSeverity,
    val symptoms: List<String> = emptyList(),
    val possibleCauses: List<String> = emptyList(),
    val recommendedActions: List<String> = emptyList(),
    val isPending: Boolean = false,
    val isCleared: Boolean = false,
    val estimatedRepairCostThb: String = ""
)

/**
 * Pure Kotlin domain model for real-time engine telemetry sensor readouts.
 */
data class SensorReading(
    val id: String,
    val displayNameTh: String,
    val displayNameEn: String,
    val value: Float,
    val unit: String,
    val minSafeValue: Float,
    val maxSafeValue: Float,
    val isWarning: Boolean = false,
    val isCritical: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) {
    val isWithinSafeRange: Boolean
        get() = value in minSafeValue..maxSafeValue
}

/**
 * Snapshot of engine operating conditions when a DTC fault was recorded (Mode 02).
 */
data class DomainFreezeFrame(
    val dtcTrigger: String,
    val engineRpm: Int,
    val vehicleSpeedKmh: Int,
    val coolantTempCelsius: Int,
    val calculatedLoadPercent: Float,
    val shortTermFuelTrimPercent: Float,
    val longTermFuelTrimPercent: Float,
    val fuelRailPressureBar: Float,
    val batteryVoltage: Float,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Complete record of a vehicle diagnostic scanning session.
 */
data class DiagnosticSession(
    val id: String,
    val vehicleVin: String,
    val vehicleModel: String,
    val timestamp: Long = System.currentTimeMillis(),
    val faults: List<DtcFault> = emptyList(),
    val freezeFrame: DomainFreezeFrame? = null,
    val ecuProtocols: List<String> = listOf("ISO 15765-4 CAN (11-bit / 500k)"),
    val milStatus: Boolean = false, // Malfunction Indicator Lamp (Check Engine Light)
    val distanceWithMilOnKm: Int = 0
)

/**
 * Generic Domain Result sealed hierarchy.
 */
sealed class DiagnosticResult<out T> {
    data class Success<out T>(val data: T) : DiagnosticResult<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : DiagnosticResult<Nothing>()
    data class InProgress(val progressPercent: Float, val currentTask: String) : DiagnosticResult<Nothing>()
}
