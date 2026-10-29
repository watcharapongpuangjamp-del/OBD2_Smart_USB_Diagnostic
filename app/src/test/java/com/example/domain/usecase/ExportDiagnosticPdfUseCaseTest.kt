package com.example.domain.usecase

import com.example.data.model.Vehicle
import com.example.domain.model.AiDiagnosticReport
import com.example.domain.model.DiagnosticPdfResult
import com.example.domain.model.DiagnosticSession
import com.example.domain.model.DomainFreezeFrame
import com.example.domain.model.DtcFault
import com.example.domain.model.DtcSeverity
import com.example.domain.model.VehicleSystemCategory
import com.example.domain.repository.PdfReportRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ExportDiagnosticPdfUseCaseTest {

    private val fakeFault = DtcFault(
        code = "P0087",
        category = VehicleSystemCategory.POWERTRAIN,
        titleTh = "แรงดันในรางจ่ายเชื้อเพลิงต่ำ",
        titleEn = "Fuel Rail Pressure Low",
        severity = DtcSeverity.CRITICAL
    )

    private val fakeSession = DiagnosticSession(
        id = "session-test-pdf",
        vehicleVin = "MR0KA3CD600123456",
        vehicleModel = "Toyota Hilux Revo",
        faults = listOf(fakeFault)
    )

    private val fakePdfFile = File.createTempFile("test_report", ".pdf").apply {
        deleteOnExit()
    }

    private val fakePdfRepo = object : PdfReportRepository {
        override suspend fun generatePdfReport(
            session: DiagnosticSession,
            aiReport: AiDiagnosticReport?,
            vehicle: Vehicle?,
            technicianName: String
        ): DiagnosticPdfResult {
            return DiagnosticPdfResult(
                file = fakePdfFile,
                fileName = "ThaiAuto_Inspection_123456.pdf",
                fileSizeFormatted = "48 KB",
                pageCount = 1
            )
        }
    }

    @Test
    fun `usecase calls repository and returns valid PDF result`() = runTest {
        val useCase = ExportDiagnosticPdfUseCase(fakePdfRepo)
        val result = useCase(fakeSession, null, null, "ช่างยนต์ ทดสอบ")

        assertNotNull(result)
        assertEquals("ThaiAuto_Inspection_123456.pdf", result.fileName)
        assertEquals("48 KB", result.fileSizeFormatted)
        assertEquals(1, result.pageCount)
        assertTrue(result.file.exists())
    }
}
