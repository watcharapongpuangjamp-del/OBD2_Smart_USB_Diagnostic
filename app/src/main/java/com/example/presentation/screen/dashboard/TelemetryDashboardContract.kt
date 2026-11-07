package com.example.presentation.screen.dashboard

import androidx.compose.ui.graphics.Color
import com.example.presentation.common.UiEffect
import com.example.presentation.common.UiEvent
import com.example.presentation.common.UiState
import com.example.presentation.common.chart.TelemetryDataPoint
import com.example.presentation.common.components.CyberAmber
import com.example.presentation.common.components.CyberCyan
import com.example.presentation.common.components.CyberGreen
import com.example.presentation.common.components.CyberRed

/**
 * Configuration and customization settings for a telemetry metric series.
 */
data class TelemetryMetricConfig(
    val id: String,
    val displayNameTh: String,
    val displayNameEn: String,
    val unit: String,
    val color: Color,
    val isVisible: Boolean = true,
    val order: Int = 0,
    val warningThreshold: Float? = null,
    val criticalThreshold: Float? = null,
    val fixedMinDomain: Float? = null,
    val fixedMaxDomain: Float? = null
)

val defaultTelemetryMetrics = listOf(
    TelemetryMetricConfig(
        id = "engine_rpm",
        displayNameTh = "รอบเครื่องยนต์ (RPM)",
        displayNameEn = "Engine Speed",
        unit = "RPM",
        color = CyberCyan,
        isVisible = true,
        order = 0,
        warningThreshold = 3800f,
        criticalThreshold = 4500f,
        fixedMinDomain = 0f,
        fixedMaxDomain = 5000f
    ),
    TelemetryMetricConfig(
        id = "vehicle_speed",
        displayNameTh = "ความเร็วรถยนต์",
        displayNameEn = "Vehicle Speed",
        unit = "km/h",
        color = CyberGreen,
        isVisible = true,
        order = 1,
        warningThreshold = 120f,
        criticalThreshold = 140f,
        fixedMinDomain = 0f,
        fixedMaxDomain = 180f
    ),
    TelemetryMetricConfig(
        id = "coolant_temp",
        displayNameTh = "อุณหภูมิน้ำหล่อเย็น (Coolant)",
        displayNameEn = "Coolant Temp",
        unit = "°C",
        color = CyberRed,
        isVisible = true,
        order = 2,
        warningThreshold = 98f,
        criticalThreshold = 108f,
        fixedMinDomain = 60f,
        fixedMaxDomain = 120f
    ),
    TelemetryMetricConfig(
        id = "fuel_rail_pressure",
        displayNameTh = "แรงดันรางดีเซลคอมมอนเรล",
        displayNameEn = "Fuel Rail Pressure",
        unit = "Bar",
        color = CyberAmber,
        isVisible = true,
        order = 3,
        warningThreshold = 1600f,
        criticalThreshold = 1900f,
        fixedMinDomain = 200f,
        fixedMaxDomain = 2000f
    ),
    TelemetryMetricConfig(
        id = "battery_voltage",
        displayNameTh = "แรงดันไฟแบตเตอรี่ (Battery)",
        displayNameEn = "Battery Voltage",
        unit = "V",
        color = Color(0xFFB388FF),
        isVisible = false,
        order = 4,
        warningThreshold = 12.2f,
        criticalThreshold = 11.5f,
        fixedMinDomain = 10f,
        fixedMaxDomain = 16f
    )
)

data class TelemetryDashboardUiState(
    val isStreaming: Boolean = true,
    val timeWindowSeconds: Int = 30, // 15, 30, 60, 120s
    val metricConfigs: List<TelemetryMetricConfig> = defaultTelemetryMetrics,
    val chartDataMap: Map<String, List<TelemetryDataPoint>> = emptyMap(),
    val showCustomizeModal: Boolean = false,
    val connectionState: String = "CONNECTED"
) : UiState {
    val activeVisibleMetrics: List<TelemetryMetricConfig>
        get() = metricConfigs.filter { it.isVisible }.sortedBy { it.order }
}

sealed class TelemetryDashboardUiEvent : UiEvent {
    object ToggleStreaming : TelemetryDashboardUiEvent()
    data class SetTimeWindow(val seconds: Int) : TelemetryDashboardUiEvent()
    data class ToggleMetricVisibility(val metricId: String) : TelemetryDashboardUiEvent()
    data class MoveMetricUp(val metricId: String) : TelemetryDashboardUiEvent()
    data class MoveMetricDown(val metricId: String) : TelemetryDashboardUiEvent()
    object OpenCustomizeModal : TelemetryDashboardUiEvent()
    object DismissCustomizeModal : TelemetryDashboardUiEvent()
    object ResetBuffers : TelemetryDashboardUiEvent()
}

sealed class TelemetryDashboardUiEffect : UiEffect {
    data class ShowToast(val message: String) : TelemetryDashboardUiEffect()
}
