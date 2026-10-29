package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val make: String,
    val model: String,
    val year: Int,
    val engine: String,
    val licensePlate: String,
    val provinceTh: String = "กรุงเทพมหานคร",
    val vin: String,
    val currentMileageKm: Int,
    val fuelType: String = "ดีเซล (Diesel)",
    val isSelected: Boolean = false,
    val lastScanDate: Long = System.currentTimeMillis(),
    val healthScore: Int = 94
)
