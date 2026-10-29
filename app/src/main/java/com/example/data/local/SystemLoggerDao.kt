package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SystemLog
import com.example.data.model.TelemetrySnapshot
import kotlinx.coroutines.flow.Flow

@Dao
interface SystemLoggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: TelemetrySnapshot): Long

    @Query("SELECT * FROM telemetry_snapshots ORDER BY timestamp DESC")
    fun getAllSnapshotsFlow(): Flow<List<TelemetrySnapshot>>

    @Query("DELETE FROM telemetry_snapshots WHERE id = :snapshotId")
    suspend fun deleteSnapshot(snapshotId: Long)

    @Query("DELETE FROM telemetry_snapshots")
    suspend fun clearAllSnapshots()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: SystemLog): Long

    @Query("SELECT * FROM system_logs ORDER BY timestamp DESC LIMIT 500")
    fun getRecentLogsFlow(): Flow<List<SystemLog>>

    @Query("DELETE FROM system_logs")
    suspend fun clearAllLogs()
}
