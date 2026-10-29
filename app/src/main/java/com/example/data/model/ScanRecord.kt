package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vehicleId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val dtcCodes: String, // Comma separated codes
    val dtcCount: Int,
    val milStatus: Boolean, // Check Engine Light
    val healthSummaryTh: String,
    val healthSummaryEn: String,
    val rpm: Int,
    val coolantTempC: Int,
    val speedKmh: Int,
    val batteryVoltage: Float
)
