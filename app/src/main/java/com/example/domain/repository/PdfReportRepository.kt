package com.example.domain.repository

import com.example.data.model.Vehicle
import com.example.domain.model.AiDiagnosticReport
import com.example.domain.model.DiagnosticPdfResult
import com.example.domain.model.DiagnosticSession

/**
 * Domain repository contract for generating native PDF inspection documents.
 */
interface PdfReportRepository {

    suspend fun generatePdfReport(
        session: DiagnosticSession,
        aiReport: AiDiagnosticReport?,
        vehicle: Vehicle?,
        technicianName: String = "ช่างยนต์ผู้ชำนาญการ (Master Diagnostic Tech)"
    ): DiagnosticPdfResult
}
