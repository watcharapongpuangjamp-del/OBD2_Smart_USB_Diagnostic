package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.DtcCode
import kotlinx.coroutines.flow.Flow

@Dao
interface DtcDao {
    @Query("SELECT * FROM dtc_codes WHERE code LIKE '%' || :searchQuery || '%' OR enTitle LIKE '%' || :searchQuery || '%' OR thTitle LIKE '%' || :searchQuery || '%'")
    fun searchDtcs(searchQuery: String): Flow<List<DtcCode>>

    @Query("SELECT * FROM dtc_codes")
    fun getAllDtcs(): Flow<List<DtcCode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dtcs: List<DtcCode>)

    @Query("SELECT COUNT(*) FROM dtc_codes")
    suspend fun getCount(): Int
}
