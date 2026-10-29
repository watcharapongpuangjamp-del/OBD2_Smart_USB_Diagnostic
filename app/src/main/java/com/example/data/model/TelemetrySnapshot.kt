package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "telemetry_snapshots")
data class TelemetrySnapshot(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val vehicleName: String,
    val title: String,
    val reason: String,
    val speedKmh: Int,
    val rpm: Int,
    val coolantTempC: Int,
    val intakeAirTempC: Int,
    val boostPressureBar: Float,
    val mafGps: Float,
    val throttlePercent: Float,
    val batteryVoltage: Float,
    val activeDtcs: String, // comma separated codes
    val notes: String
)
