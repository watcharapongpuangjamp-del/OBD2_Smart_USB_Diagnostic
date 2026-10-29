package com.example.domain.usecase

import com.example.domain.model.DiagnosticResult
import com.example.domain.model.DiagnosticSession
import com.example.domain.repository.ObdDiagnosticRepository
import kotlinx.coroutines.flow.Flow

/**
 * Single-responsibility UseCase to execute an in-depth OBD-II diagnostic fault code scan.
 */
class ScanDtcCodesUseCase(
    private val obdRepository: ObdDiagnosticRepository
) {
    operator fun invoke(): Flow<DiagnosticResult<DiagnosticSession>> {
        return obdRepository.performFullSystemScan()
    }
}
