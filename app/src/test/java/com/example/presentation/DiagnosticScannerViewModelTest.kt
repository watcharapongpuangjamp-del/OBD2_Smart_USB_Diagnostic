package com.example.presentation

import com.example.domain.model.DiagnosticResult
import com.example.domain.model.DiagnosticSession
import com.example.domain.model.DtcFault
import com.example.domain.model.DtcSeverity
import com.example.domain.model.SensorReading
import com.example.domain.model.VehicleSystemCategory
import com.example.domain.repository.ObdDiagnosticRepository
import com.example.domain.usecase.ClearDtcCodesUseCase
import com.example.domain.usecase.ScanDtcCodesUseCase
import com.example.presentation.screen.diagnostic.DiagnosticScannerUiEvent
import com.example.presentation.screen.diagnostic.DiagnosticScannerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiagnosticScannerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val fakeDtcList = listOf(
        DtcFault(
            code = "P0171",
            category = VehicleSystemCategory.POWERTRAIN,
            titleTh = "ส่วนผสมไอดีบางเกินไป",
            titleEn = "System Too Lean",
            severity = DtcSeverity.WARNING
        )
    )

    private val fakeSession = DiagnosticSession(
        id = "session-1",
        vehicleVin = "VIN123456",
        vehicleModel = "Toyota Revo",
        faults = fakeDtcList
    )

    private val fakeRepo = object : ObdDiagnosticRepository {
        override fun performFullSystemScan(): Flow<DiagnosticResult<DiagnosticSession>> = flow {
            emit(DiagnosticResult.Success(fakeSession))
        }

        override suspend fun clearDtcCodes(): DiagnosticResult<Boolean> = DiagnosticResult.Success(true)
        override fun observeLiveTelemetry(): Flow<List<SensorReading>> = flow { emit(emptyList()) }
        override fun observeConnectionStatus(): Flow<String> = flow { emit("CONNECTED") }
        override suspend fun lookupDtcFault(code: String): DtcFault? = null
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel performs scan and updates detected faults in state`() = runTest(testDispatcher) {
        val scanUseCase = ScanDtcCodesUseCase(fakeRepo)
        val clearUseCase = ClearDtcCodesUseCase(fakeRepo)
        val viewModel = DiagnosticScannerViewModel(scanUseCase, clearUseCase)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.currentState
        assertFalse(state.isScanning)
        assertEquals(1, state.detectedFaults.size)
        assertEquals("P0171", state.detectedFaults[0].code)
        assertEquals(1, state.warningCount)
        assertEquals(0, state.criticalCount)
    }

    @Test
    fun `viewModel clears fault codes when clear event received`() = runTest(testDispatcher) {
        val scanUseCase = ScanDtcCodesUseCase(fakeRepo)
        val clearUseCase = ClearDtcCodesUseCase(fakeRepo)
        val viewModel = DiagnosticScannerViewModel(scanUseCase, clearUseCase)

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, viewModel.currentState.detectedFaults.size)

        viewModel.onEvent(DiagnosticScannerUiEvent.ClearFaultCodes)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.currentState.detectedFaults.size)
        assertFalse(viewModel.currentState.isClearing)
    }
}
