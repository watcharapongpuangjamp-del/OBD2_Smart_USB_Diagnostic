package com.example.presentation.screen.dashboard

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.StreamLiveTelemetryUseCase
import com.example.presentation.common.BaseViewModel
import com.example.presentation.common.chart.TelemetryDataPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * MVI ViewModel powering the Customizable D3 Telemetry Dashboard.
 */
class TelemetryDashboardViewModel(
    private val streamLiveTelemetryUseCase: StreamLiveTelemetryUseCase
) : BaseViewModel<TelemetryDashboardUiState, TelemetryDashboardUiEvent, TelemetryDashboardUiEffect>(
    initialState = TelemetryDashboardUiState()
) {

    private var streamingJob: Job? = null
    // Internal rolling buffers per metric id
    private val internalBuffers = mutableMapOf<String, MutableList<TelemetryDataPoint>>()

    init {
        startStreaming()
    }

    override fun onEvent(event: TelemetryDashboardUiEvent) {
        when (event) {
            is TelemetryDashboardUiEvent.ToggleStreaming -> {
                if (currentState.isStreaming) {
                    stopStreaming()
                } else {
                    startStreaming()
                }
            }
            is TelemetryDashboardUiEvent.SetTimeWindow -> {
                setState { copy(timeWindowSeconds = event.seconds) }
                trimBuffersToTimeWindow()
            }
            is TelemetryDashboardUiEvent.ToggleMetricVisibility -> toggleMetric(event.metricId)
            is TelemetryDashboardUiEvent.MoveMetricUp -> moveMetric(event.metricId, moveUp = true)
            is TelemetryDashboardUiEvent.MoveMetricDown -> moveMetric(event.metricId, moveUp = false)
            is TelemetryDashboardUiEvent.OpenCustomizeModal -> setState { copy(showCustomizeModal = true) }
            is TelemetryDashboardUiEvent.DismissCustomizeModal -> setState { copy(showCustomizeModal = false) }
            is TelemetryDashboardUiEvent.ResetBuffers -> {
                internalBuffers.clear()
                setState { copy(chartDataMap = emptyMap()) }
                sendEffect(TelemetryDashboardUiEffect.ShowToast("รีเซ็ตประวัติกราฟเรียบร้อย"))
            }
        }
    }

    private fun startStreaming() {
        streamingJob?.cancel()
        streamingJob = viewModelScope.launch {
            setState { copy(isStreaming = true, connectionState = "CONNECTED") }
            streamLiveTelemetryUseCase().collectLatest { sensorList ->
                val now = System.currentTimeMillis()
                val windowCutoff = now - (currentState.timeWindowSeconds * 1000L)

                sensorList.forEach { sensor ->
                    val buffer = internalBuffers.getOrPut(sensor.id) { mutableListOf() }
                    buffer.add(TelemetryDataPoint(timestamp = now, value = sensor.value))

                    // Keep points within current window plus buffer
                    while (buffer.isNotEmpty() && buffer.first().timestamp < windowCutoff) {
                        buffer.removeAt(0)
                    }
                }

                val snapshot = internalBuffers.mapValues { (_, list) -> list.toList() }
                setState { copy(chartDataMap = snapshot) }
            }
        }
    }

    private fun stopStreaming() {
        streamingJob?.cancel()
        streamingJob = null
        setState { copy(isStreaming = false, connectionState = "PAUSED") }
        sendEffect(TelemetryDashboardUiEffect.ShowToast("หยุดสตรีมกราฟชั่วคราว"))
    }

    private fun toggleMetric(metricId: String) {
        val updated = currentState.metricConfigs.map { config ->
            if (config.id == metricId) config.copy(isVisible = !config.isVisible) else config
        }
        setState { copy(metricConfigs = updated) }
    }

    private fun moveMetric(metricId: String, moveUp: Boolean) {
        val list = currentState.metricConfigs.sortedBy { it.order }.toMutableList()
        val index = list.indexOfFirst { it.id == metricId }
        if (index == -1) return

        val targetIndex = if (moveUp) index - 1 else index + 1
        if (targetIndex in 0 until list.size) {
            val item = list.removeAt(index)
            list.add(targetIndex, item)
            val reordered = list.mapIndexed { idx, cfg -> cfg.copy(order = idx) }
            setState { copy(metricConfigs = reordered) }
        }
    }

    private fun trimBuffersToTimeWindow() {
        val now = System.currentTimeMillis()
        val windowCutoff = now - (currentState.timeWindowSeconds * 1000L)
        internalBuffers.values.forEach { buffer ->
            while (buffer.isNotEmpty() && buffer.first().timestamp < windowCutoff) {
                buffer.removeAt(0)
            }
        }
        val snapshot = internalBuffers.mapValues { (_, list) -> list.toList() }
        setState { copy(chartDataMap = snapshot) }
    }

    override fun onCleared() {
        super.onCleared()
        streamingJob?.cancel()
    }
}
