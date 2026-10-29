package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class DtcCategory {
    POWERTRAIN, // P codes: Engine, Transmission, Fuel, Emissions
    CHASSIS,    // C codes: ABS, ESP, Traction, Suspension, Steering
    BODY,       // B codes: Airbags, Climate Control, Lighting, Door Lock
    NETWORK     // U codes: CAN Bus, ECU Communication, Sensor Network
}

enum class DtcSeverity {
    CRITICAL,   // Red: Stop driving immediately, severe engine/transmission hazard
    WARNING,    // Amber: Service soon, emissions or performance degradation
    INFO        // Blue: Pending or history code, intermittent issue
}

@Entity(tableName = "dtc_codes")
data class DtcCode(
    @PrimaryKey
    val code: String,
    val category: DtcCategory,
    val enTitle: String,
    val thTitle: String,
    val enDescription: String,
    val thDescription: String,
    val severity: DtcSeverity,
    val commonCausesTh: List<String>,
    val commonCausesEn: List<String>,
    val symptomsTh: List<String>,
    val symptomsEn: List<String>,
    val estimatedCostThb: String,
    val diyDifficulty: String, // Easy, Medium, Hard, Pro Only
    val affectedSystem: String
)
