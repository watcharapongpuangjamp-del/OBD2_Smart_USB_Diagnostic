package com.example.domain.model

/**
 * Automotive diagnostic category conforming to SAE J2012 / ISO 15031-6.
 */
enum class DomainDtcCategory {
    POWERTRAIN, // P codes: Engine, Transmission, Fuel, Emissions
    CHASSIS,    // C codes: ABS, ESP, Traction, Suspension, Steering
    BODY,       // B codes: Airbags, Climate Control, Lighting, Door Lock
    NETWORK     // U codes: CAN Bus, ECU Communication, Sensor Network
}

/**
 * Severity ranking for trouble codes.
 */
enum class DomainDtcSeverity {
    CRITICAL,   // Stop driving immediately, severe risk
    WARNING,    // Service required soon, emissions / performance issue
    INFO        // Pending or historical notice
}

/**
 * Clean Architecture pure domain representation of an OBD-II Diagnostic Trouble Code.
 * Uncoupled from Room annotations or Android framework dependencies.
 */
data class DiagnosticCode(
    val code: String,
    val category: DomainDtcCategory,
    val enTitle: String,
    val thTitle: String,
    val enDescription: String,
    val thDescription: String,
    val severity: DomainDtcSeverity,
    val commonCausesTh: List<String>,
    val commonCausesEn: List<String>,
    val symptomsTh: List<String>,
    val symptomsEn: List<String>,
    val estimatedCostThb: String,
    val diyDifficulty: String,
    val affectedSystem: String
)
