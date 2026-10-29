package com.example.domain.usecase

import com.example.data.model.Vehicle
import com.example.domain.model.AiDiagnosticReport
import com.example.domain.model.DomainFreezeFrame
import com.example.domain.model.DtcFault
import com.example.domain.repository.AiDiagnosticRepository

/**
 * UseCase to generate deep-reasoning predictive diagnosis, Freeze Frame correlation,
 * step-by-step DIY repair guides, and Thai Baht price estimations.
 */
class GenerateAiDiagnosticReportUseCase(
    private val aiRepository: AiDiagnosticRepository
) {
    suspend operator fun invoke(
        fault: DtcFault,
        freezeFrame: DomainFreezeFrame?,
        vehicle: Vehicle?
    ): AiDiagnosticReport {
        return aiRepository.generateDiagnosticReport(fault, freezeFrame, vehicle)
    }
}
