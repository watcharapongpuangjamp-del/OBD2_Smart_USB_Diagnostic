package com.example.domain.usecase

import com.example.domain.model.DiagnosticCode
import com.example.domain.model.DomainDtcCategory
import com.example.domain.model.DomainDtcSeverity
import com.example.domain.repository.DtcRepository
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to search and filter OBD-II trouble codes offline from Room database.
 */
class SearchDtcUseCase(
    private val repository: DtcRepository
) {
    operator fun invoke(
        query: String = "",
        category: DomainDtcCategory? = null,
        severity: DomainDtcSeverity? = null
    ): Flow<List<DiagnosticCode>> {
        return repository.searchDtcCodes(
            query = query.trim(),
            category = category,
            severity = severity
        )
    }
}
