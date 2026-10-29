package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "system_logs")
data class SystemLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val level: String, // INFO, WARN, ERROR, CRITICAL
    val category: String, // OBD2, AI_MECHANIC, SENSOR, APP, SAFETY
    val message: String,
    val extraData: String? = null
)
