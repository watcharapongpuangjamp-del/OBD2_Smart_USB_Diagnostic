package com.example.domain.usecase

import com.example.domain.model.DiagnosticResult
import com.example.domain.repository.ObdDiagnosticRepository

/**
 * Single-responsibility UseCase to clear DTC trouble codes and reset the Malfunction Indicator Lamp.
 */
class ClearDtcCodesUseCase(
    private val obdRepository: ObdDiagnosticRepository
) {
    suspend operator fun invoke(): DiagnosticResult<Boolean> {
        return obdRepository.clearDtcCodes()
    }
}
