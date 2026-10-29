package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_logs")
data class MaintenanceLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vehicleId: Long,
    val serviceTypeTh: String,
    val serviceTypeEn: String,
    val mileageAtServiceKm: Int,
    val nextServiceMileageKm: Int,
    val serviceDate: Long,
    val nextServiceDate: Long,
    val costThb: Double,
    val garageNameTh: String,
    val notes: String = "",
    val isCompleted: Boolean = true
)
