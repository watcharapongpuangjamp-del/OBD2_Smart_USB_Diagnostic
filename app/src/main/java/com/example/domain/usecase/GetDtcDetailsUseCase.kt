package com.example.domain.usecase

import com.example.domain.model.DiagnosticCode
import com.example.domain.repository.DtcRepository
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to retrieve detailed trouble code data by code.
 */
class GetDtcDetailsUseCase(
    private val repository: DtcRepository
) {
    operator fun invoke(code: String): Flow<DiagnosticCode?> {
        return repository.getDtcByCode(code.trim().uppercase())
    }
}
