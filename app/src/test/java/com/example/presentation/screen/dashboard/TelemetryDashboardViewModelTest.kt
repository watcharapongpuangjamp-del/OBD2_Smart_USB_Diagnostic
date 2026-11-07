package com.example.presentation.screen.dashboard

import com.example.domain.model.DiagnosticResult
import com.example.domain.model.DiagnosticSession
import com.example.domain.model.DtcFault
import com.example.domain.model.SensorReading
import com.example.domain.repository.ObdDiagnosticRepository
import com.example.domain.usecase.StreamLiveTelemetryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TelemetryDashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val telemetryFlow = MutableSharedFlow<List<SensorReading>>()

    private val fakeRepo = object : ObdDiagnosticRepository {
        override fun performFullSystemScan(): Flow<DiagnosticResult<DiagnosticSession>> = flow { }
        override suspend fun clearDtcCodes(): DiagnosticResult<Boolean> = DiagnosticResult.Success(true)
        override fun observeLiveTelemetry(): Flow<List<SensorReading>> = telemetryFlow
        override fun observeConnectionStatus(): Flow<String> = flow { emit("CONNECTED") }
        override suspend fun lookupDtcFault(code: String): DtcFault? = null
    private var activeViewModel: TelemetryDashboardViewModel? = null

    private fun createViewModel(): TelemetryDashboardViewModel {
        val useCase = StreamLiveTelemetryUseCase(fakeRepo)
        val vm = TelemetryDashboardViewModel(useCase)
        activeViewModel = vm
        return vm
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        if (activeViewModel?.uiState?.value?.isStreaming == true) {
            activeViewModel?.onEvent(TelemetryDashboardUiEvent.ToggleStreaming)
        }
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has live streaming enabled and default metric configs`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isStreaming)
        assertEquals("CONNECTED", state.connectionState)
        assertEquals(30, state.timeWindowSeconds)
        assertTrue(state.metricConfigs.isNotEmpty())
    }

    @Test
    fun `toggleStreaming correctly flips streaming and connection state`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TelemetryDashboardUiEvent.ToggleStreaming)
        testDispatcher.scheduler.advanceUntilIdle()
        val pausedState = viewModel.uiState.value
        assertFalse(pausedState.isStreaming)
        assertEquals("PAUSED", pausedState.connectionState)

        viewModel.onEvent(TelemetryDashboardUiEvent.ToggleStreaming)
        testDispatcher.scheduler.advanceUntilIdle()
        val resumedState = viewModel.uiState.value
        assertTrue(resumedState.isStreaming)
        assertEquals("CONNECTED", resumedState.connectionState)
    }

    @Test
    fun `setTimeWindow updates window in state`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TelemetryDashboardUiEvent.SetTimeWindow(60))
        assertEquals(60, viewModel.uiState.value.timeWindowSeconds)

        viewModel.onEvent(TelemetryDashboardUiEvent.SetTimeWindow(120))
        assertEquals(120, viewModel.uiState.value.timeWindowSeconds)
    }

    @Test
    fun `toggleMetricVisibility updates visibility of target metric`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val initialRpmVisible = viewModel.uiState.value.metricConfigs.first { it.id == "engine_rpm" }.isVisible
        viewModel.onEvent(TelemetryDashboardUiEvent.ToggleMetricVisibility("engine_rpm"))
        val updatedRpmVisible = viewModel.uiState.value.metricConfigs.first { it.id == "engine_rpm" }.isVisible

        assertEquals(!initialRpmVisible, updatedRpmVisible)
    }

    @Test
    fun `moveMetricDown changes order of top metric`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val topMetric = viewModel.uiState.value.metricConfigs.minByOrNull { it.order }!!
        viewModel.onEvent(TelemetryDashboardUiEvent.MoveMetricDown(topMetric.id))

        val newMetric = viewModel.uiState.value.metricConfigs.first { it.id == topMetric.id }
        assertTrue(newMetric.order > topMetric.order)
    }

    @Test
    fun `modal visibility events open and dismiss customization modal`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TelemetryDashboardUiEvent.OpenCustomizeModal)
        assertTrue(viewModel.uiState.value.showCustomizeModal)

        viewModel.onEvent(TelemetryDashboardUiEvent.DismissCustomizeModal)
        assertFalse(viewModel.uiState.value.showCustomizeModal)
    }

    @Test
    fun `live sensor telemetry populates chart data map`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val fakeReadings = listOf(
            SensorReading(
                id = "engine_rpm",
                displayNameTh = "รอบเครื่องยนต์",
                displayNameEn = "Engine RPM",
                value = 2400f,
                unit = "RPM",
                minSafeValue = 600f,
                maxSafeValue = 6000f
            ),
            SensorReading(
                id = "vehicle_speed",
                displayNameTh = "ความเร็วรถ",
                displayNameEn = "Vehicle Speed",
                value = 85f,
                unit = "km/h",
                minSafeValue = 0f,
                maxSafeValue = 220f
            )
        )

        telemetryFlow.emit(fakeReadings)
        testDispatcher.scheduler.advanceUntilIdle()

        val chartData = viewModel.uiState.value.chartDataMap
        assertTrue(chartData.containsKey("engine_rpm"))
        assertEquals(1, chartData["engine_rpm"]?.size)
        assertEquals(2400f, chartData["engine_rpm"]?.first()?.value)

        // Reset buffers
        viewModel.onEvent(TelemetryDashboardUiEvent.ResetBuffers)
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.chartDataMap.isEmpty())
    }
}
