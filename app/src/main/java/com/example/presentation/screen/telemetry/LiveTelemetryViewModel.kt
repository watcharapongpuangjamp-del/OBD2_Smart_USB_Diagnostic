package com.example.presentation.screen.telemetry

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.EvaluateSensorSafetyUseCase
import com.example.domain.usecase.StreamLiveTelemetryUseCase
import com.example.presentation.common.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * MVI ViewModel managing real-time vehicle engine telemetry and dynamic safety evaluation.
 */
class LiveTelemetryViewModel(
    private val streamLiveTelemetryUseCase: StreamLiveTelemetryUseCase,
    private val evaluateSensorSafetyUseCase: EvaluateSensorSafetyUseCase = EvaluateSensorSafetyUseCase()
) : BaseViewModel<LiveTelemetryUiState, LiveTelemetryUiEvent, LiveTelemetryUiEffect>(
    initialState = LiveTelemetryUiState()
) {

    private var streamingJob: Job? = null

    init {
        startTelemetryStream()
    }

    override fun onEvent(event: LiveTelemetryUiEvent) {
        when (event) {
            is LiveTelemetryUiEvent.ToggleStreaming -> {
                if (currentState.isStreaming) {
                    stopTelemetryStream()
                } else {
                    startTelemetryStream()
                }
            }
            is LiveTelemetryUiEvent.ToggleHudMirror -> {
                setState { copy(isHudMirrorMode = !isHudMirrorMode) }
            }
            is LiveTelemetryUiEvent.SelectSensor -> {
                setState { copy(selectedSensorId = event.sensorId) }
            }
        }
    }

    private fun startTelemetryStream() {
        streamingJob?.cancel()
        streamingJob = viewModelScope.launch {
            setState { copy(isStreaming = true, connectionState = "CONNECTED") }
            streamLiveTelemetryUseCase().collectLatest { sensorList ->
                val assessment = evaluateSensorSafetyUseCase(sensorList)
                setState {
                    copy(
                        sensors = sensorList,
                        safetyStatus = assessment
                    )
                }

                if (!assessment.isSafe) {
                    assessment.criticalAlerts.firstOrNull()?.let { alert ->
                        sendEffect(LiveTelemetryUiEffect.CriticalSensorAlert(alert))
                    }
                }
            }
        }
    }

    private fun stopTelemetryStream() {
        streamingJob?.cancel()
        streamingJob = null
        setState { copy(isStreaming = false, connectionState = "PAUSED") }
        sendEffect(LiveTelemetryUiEffect.ShowToast("หยุดการสตรีมข้อมูลเซนเซอร์ชั่วคราว"))
    }

    override fun onCleared() {
        super.onCleared()
        streamingJob?.cancel()
    }
}
