package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.DtcCategory
import com.example.data.model.DtcCode
import com.example.data.model.DtcSeverity
import kotlinx.coroutines.flow.Flow

@Dao
interface DtcDao {
    @Query("""
        SELECT * FROM dtc_codes 
        WHERE code LIKE '%' || :searchQuery || '%' 
           OR enTitle LIKE '%' || :searchQuery || '%' 
           OR thTitle LIKE '%' || :searchQuery || '%' 
           OR enDescription LIKE '%' || :searchQuery || '%' 
           OR thDescription LIKE '%' || :searchQuery || '%'
           OR affectedSystem LIKE '%' || :searchQuery || '%'
        ORDER BY code ASC
    """)
    fun searchDtcs(searchQuery: String): Flow<List<DtcCode>>

    @Query("SELECT * FROM dtc_codes WHERE code = :code LIMIT 1")
    fun getDtcByCode(code: String): Flow<DtcCode?>

    @Query("SELECT * FROM dtc_codes WHERE category = :category ORDER BY code ASC")
    fun getDtcsByCategory(category: DtcCategory): Flow<List<DtcCode>>

    @Query("SELECT * FROM dtc_codes WHERE severity = :severity ORDER BY code ASC")
    fun getDtcsBySeverity(severity: DtcSeverity): Flow<List<DtcCode>>

    @Query("SELECT * FROM dtc_codes ORDER BY code ASC")
    fun getAllDtcs(): Flow<List<DtcCode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dtcs: List<DtcCode>)

    @Query("SELECT COUNT(*) FROM dtc_codes")
    suspend fun getCount(): Int
}

