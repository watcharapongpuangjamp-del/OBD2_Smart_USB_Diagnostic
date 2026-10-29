package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.DtcCode
import com.example.data.model.MaintenanceLog
import com.example.data.model.ScanRecord
import com.example.data.model.SystemLog
import com.example.data.model.TelemetrySnapshot
import com.example.data.model.Vehicle

@Database(
    entities = [Vehicle::class, MaintenanceLog::class, ScanRecord::class, DtcCode::class, TelemetrySnapshot::class, SystemLog::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun scanRecordDao(): ScanRecordDao
    abstract fun dtcDao(): DtcDao
    abstract fun systemLoggerDao(): SystemLoggerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "thai_obd2_diagnostics.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
