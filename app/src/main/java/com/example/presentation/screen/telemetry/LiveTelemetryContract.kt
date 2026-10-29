package com.example.presentation.screen.telemetry

import com.example.domain.model.SensorReading
import com.example.domain.usecase.SafetyAssessment
import com.example.presentation.common.UiEffect
import com.example.presentation.common.UiEvent
import com.example.presentation.common.UiState

data class LiveTelemetryUiState(
    val sensors: List<SensorReading> = emptyList(),
    val connectionState: String = "CONNECTING",
    val isStreaming: Boolean = true,
    val safetyStatus: SafetyAssessment = SafetyAssessment(isSafe = true),
    val selectedSensorId: String? = "engine_rpm",
    val isHudMirrorMode: Boolean = false
) : UiState {
    val rpm: Float
        get() = sensors.firstOrNull { it.id == "engine_rpm" }?.value ?: 0f

    val speed: Float
        get() = sensors.firstOrNull { it.id == "vehicle_speed" }?.value ?: 0f

    val coolantTemp: Float
        get() = sensors.firstOrNull { it.id == "coolant_temp" }?.value ?: 0f

    val fuelPressure: Float
        get() = sensors.firstOrNull { it.id == "fuel_rail_pressure" }?.value ?: 0f

    val batteryVoltage: Float
        get() = sensors.firstOrNull { it.id == "battery_voltage" }?.value ?: 0f
}

sealed class LiveTelemetryUiEvent : UiEvent {
    object ToggleStreaming : LiveTelemetryUiEvent()
    object ToggleHudMirror : LiveTelemetryUiEvent()
    data class SelectSensor(val sensorId: String) : LiveTelemetryUiEvent()
}

sealed class LiveTelemetryUiEffect : UiEffect {
    data class CriticalSensorAlert(val message: String) : LiveTelemetryUiEffect()
    data class ShowToast(val message: String) : LiveTelemetryUiEffect()
}
