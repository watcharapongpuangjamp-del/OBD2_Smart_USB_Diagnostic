package com.example.domain.usecase

import com.example.domain.model.DiagnosticResult
import com.example.domain.model.DiagnosticSession
import com.example.domain.model.DtcFault
import com.example.domain.model.DtcSeverity
import com.example.domain.model.SensorReading
import com.example.domain.model.VehicleSystemCategory
import com.example.domain.repository.ObdDiagnosticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScanDtcCodesUseCaseTest {

    private val fakeDtcList = listOf(
        DtcFault(
            code = "P0087",
            category = VehicleSystemCategory.POWERTRAIN,
            titleTh = "แรงดันในรางจ่ายเชื้อเพลิงต่ำ",
            titleEn = "Fuel Rail Pressure Low",
            severity = DtcSeverity.CRITICAL
        )
    )

    private val fakeSession = DiagnosticSession(
        id = "test-session-123",
        vehicleVin = "MR0KA3CD600123456",
        vehicleModel = "Toyota Hilux Revo",
        faults = fakeDtcList
    )

    private val fakeRepository = object : ObdDiagnosticRepository {
        override fun performFullSystemScan(): Flow<DiagnosticResult<DiagnosticSession>> = flow {
            emit(DiagnosticResult.InProgress(0.5f, "Scanning"))
            emit(DiagnosticResult.Success(fakeSession))
        }

        override suspend fun clearDtcCodes(): DiagnosticResult<Boolean> = DiagnosticResult.Success(true)
        override fun observeLiveTelemetry(): Flow<List<SensorReading>> = flow { emit(emptyList()) }
        override fun observeConnectionStatus(): Flow<String> = flow { emit("CONNECTED") }
        override suspend fun lookupDtcFault(code: String): DtcFault? = null
    }

    @Test
    fun `ScanDtcCodesUseCase emits InProgress and Success states sequentially`() = runTest {
        val useCase = ScanDtcCodesUseCase(fakeRepository)
        val emissions = useCase().toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is DiagnosticResult.InProgress)
        assertTrue(emissions[1] is DiagnosticResult.Success)

        val success = emissions[1] as DiagnosticResult.Success
        assertEquals("test-session-123", success.data.id)
        assertEquals(1, success.data.faults.size)
        assertEquals("P0087", success.data.faults[0].code)
    }
}
