package com.example.domain.usecase

import com.example.data.model.Vehicle
import com.example.domain.model.AiDiagnosticReport
import com.example.domain.model.DiyRepairStep
import com.example.domain.model.DomainFreezeFrame
import com.example.domain.model.DtcFault
import com.example.domain.model.DtcSeverity
import com.example.domain.model.PartPriceEstimate
import com.example.domain.model.VehicleSystemCategory
import com.example.domain.repository.AiDiagnosticRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GenerateAiDiagnosticReportUseCaseTest {

    private val fakeFault = DtcFault(
        code = "P0087",
        category = VehicleSystemCategory.POWERTRAIN,
        titleTh = "แรงดันในรางจ่ายเชื้อเพลิงต่ำ",
        titleEn = "Fuel Rail Pressure Low",
        severity = DtcSeverity.CRITICAL
    )

    private val fakeFreezeFrame = DomainFreezeFrame(
        dtcTrigger = "P0087",
        engineRpm = 2240,
        vehicleSpeedKmh = 82,
        coolantTempCelsius = 92,
        calculatedLoadPercent = 68.5f,
        shortTermFuelTrimPercent = 2.3f,
        longTermFuelTrimPercent = 4.1f,
        fuelRailPressureBar = 1120f,
        batteryVoltage = 14.1f
    )

    private val fakeReport = AiDiagnosticReport(
        dtcCode = "P0087",
        vehicleModel = "Toyota Hilux Revo",
        rootCauseSummary = "วาล์ว SCV ท้ายปั๊มติดขัด",
        probabilityScore = 92,
        sensorCorrelation = "แรงดันรางตกขณะเร่ง",
        severityLevel = DtcSeverity.CRITICAL,
        toolsRequired = listOf("บล็อก 10mm", "ประแจปอนด์"),
        estimatedMinutes = 45,
        diyDifficulty = "ปานกลาง",
        repairSteps = listOf(
            DiyRepairStep(1, "ดับเครื่อง", "ปลดขั้วแบตเตอรี่"),
            DiyRepairStep(2, "เปลี่ยนวาล์ว SCV", "ขันน็อตหกเหลี่ยม", torqueSpec = "8.5 N·m")
        ),
        priceEstimates = listOf(
            PartPriceEstimate("วาล์ว SCV Denso", "SCV Valve", 2400, 1450, 400)
        ),
        safetyNotes = listOf("ระวังน้ำมันดีเซลหก")
    )

    private val fakeAiRepo = object : AiDiagnosticRepository {
        override suspend fun generateDiagnosticReport(
            fault: DtcFault,
            freezeFrame: DomainFreezeFrame?,
            vehicle: Vehicle?
        ): AiDiagnosticReport {
            return fakeReport
        }
    }

    @Test
    fun `usecase generates structured AI diagnosis report with repair steps and pricing`() = runTest {
        val useCase = GenerateAiDiagnosticReportUseCase(fakeAiRepo)
        val result = useCase(fakeFault, fakeFreezeFrame, null)

        assertNotNull(result)
        assertEquals("P0087", result.dtcCode)
        assertEquals(92, result.probabilityScore)
        assertEquals(2, result.repairSteps.size)
        assertEquals(1, result.priceEstimates.size)
        assertEquals(2800, result.priceEstimates[0].totalEstimatedOemThb)
        assertTrue(result.toolsRequired.contains("บล็อก 10mm"))
    }
}
