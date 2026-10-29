package com.example.domain.usecase

import com.example.data.model.Vehicle
import com.example.domain.model.AiDiagnosticReport
import com.example.domain.model.DiagnosticPdfResult
import com.example.domain.model.DiagnosticSession
import com.example.domain.repository.PdfReportRepository

/**
 * UseCase to generate and export an official, branded PDF Diagnostic Inspection Report.
 */
class ExportDiagnosticPdfUseCase(
    private val pdfRepository: PdfReportRepository
) {
    suspend operator fun invoke(
        session: DiagnosticSession,
        aiReport: AiDiagnosticReport? = null,
        vehicle: Vehicle? = null,
        technicianName: String = "ช่างยนต์ผู้ชำนาญการ (Master Diagnostic Tech)"
    ): DiagnosticPdfResult {
        return pdfRepository.generatePdfReport(
            session = session,
            aiReport = aiReport,
            vehicle = vehicle,
            technicianName = technicianName
        )
    }
}
