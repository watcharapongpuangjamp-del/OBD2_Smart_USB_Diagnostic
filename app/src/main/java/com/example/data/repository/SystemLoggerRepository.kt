package com.example.data.repository

import com.example.data.local.SystemLoggerDao
import com.example.data.model.LiveSensorData
import com.example.data.model.SystemLog
import com.example.data.model.TelemetrySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SystemLoggerRepository(private val dao: SystemLoggerDao) {
    val telemetrySnapshots: Flow<List<TelemetrySnapshot>> = dao.getAllSnapshotsFlow()
    val systemLogs: Flow<List<SystemLog>> = dao.getRecentLogsFlow()

    suspend fun captureSnapshot(
        vehicleName: String,
        title: String,
        reason: String,
        sensors: LiveSensorData,
        activeDtcs: String,
        notes: String
    ) {
        val snapshot = TelemetrySnapshot(
            vehicleName = vehicleName,
            title = title,
            reason = reason,
            speedKmh = sensors.speedKmh,
            rpm = sensors.rpm,
            coolantTempC = sensors.coolantTempC,
            intakeAirTempC = sensors.intakeAirTempC,
            boostPressureBar = sensors.boostPressureBar,
            mafGps = sensors.mafGps,
            throttlePercent = sensors.throttlePercent,
            batteryVoltage = sensors.batteryVoltage,
            activeDtcs = activeDtcs,
            notes = notes
        )
        dao.insertSnapshot(snapshot)
    }

    suspend fun deleteSnapshot(id: Long) {
        dao.deleteSnapshot(id)
    }

    suspend fun clearAllSnapshots() {
        dao.clearAllSnapshots()
    }

    suspend fun log(level: String, category: String, message: String, extraData: String? = null) {
        val entry = SystemLog(
            level = level,
            category = category,
            message = message,
            extraData = extraData
        )
        dao.insertLog(entry)
    }

    suspend fun clearAllLogs() {
        dao.clearAllLogs()
    }

    fun logSync(level: String, category: String, message: String, extraData: String? = null) {
        kotlinx.coroutines.GlobalScope.launch {
            log(level, category, message, extraData)
        }
    }

    fun i(category: String, message: String, extraData: String? = null) = logSync("INFO", category, message, extraData)
    fun w(category: String, message: String, extraData: String? = null) = logSync("WARN", category, message, extraData)
    fun e(category: String, message: String, extraData: String? = null) = logSync("ERROR", category, message, extraData)
    fun c(category: String, message: String, extraData: String? = null) = logSync("CRITICAL", category, message, extraData)
}
