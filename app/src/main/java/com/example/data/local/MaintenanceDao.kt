package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MaintenanceLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance_logs WHERE vehicleId = :vehicleId ORDER BY serviceDate DESC")
    fun getLogsForVehicle(vehicleId: Long): Flow<List<MaintenanceLog>>

    @Query("SELECT * FROM maintenance_logs ORDER BY serviceDate DESC")
    fun getAllLogs(): Flow<List<MaintenanceLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MaintenanceLog): Long

    @Update
    suspend fun updateLog(log: MaintenanceLog)

    @Delete
    suspend fun deleteLog(log: MaintenanceLog)

    @Query("SELECT SUM(costThb) FROM maintenance_logs WHERE vehicleId = :vehicleId")
    fun getTotalCostForVehicle(vehicleId: Long): Flow<Double?>
}
