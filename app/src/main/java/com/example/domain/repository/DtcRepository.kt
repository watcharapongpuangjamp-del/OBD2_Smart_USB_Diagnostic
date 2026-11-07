package com.example.domain.repository

import com.example.domain.model.DiagnosticCode
import com.example.domain.model.DomainDtcCategory
import com.example.domain.model.DomainDtcSeverity
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository contract defining operations for querying OBD-II diagnostic trouble codes.
 */
interface DtcRepository {
    /**
     * Search diagnostic codes matching a text query, with optional category and severity filters.
     */
    fun searchDtcCodes(
        query: String = "",
        category: DomainDtcCategory? = null,
        severity: DomainDtcSeverity? = null
    ): Flow<List<DiagnosticCode>>

    /**
     * Get a specific trouble code by exact code string (e.g. "P0300").
     */
    fun getDtcByCode(code: String): Flow<DiagnosticCode?>

    /**
     * Retrieve all stored diagnostic trouble codes.
     */
    fun getAllDtcCodes(): Flow<List<DiagnosticCode>>

    /**
     * Return total number of codes stored in the local SQLite database.
     */
    suspend fun getDtcCount(): Int

    /**
     * Ensure the local Room SQLite database is populated with bundled codes.
     * Returns the count of codes in the database.
     */
    suspend fun ensureDatabaseSeeded(): Int
}
