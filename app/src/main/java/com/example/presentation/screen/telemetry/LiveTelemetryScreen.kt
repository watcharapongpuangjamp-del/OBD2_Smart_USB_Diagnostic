package com.example.presentation.screen.telemetry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.common.components.CarbonDark
import com.example.presentation.common.components.CyberAmber
import com.example.presentation.common.components.CyberCyan
import com.example.presentation.common.components.CyberGreen
import com.example.presentation.common.components.CyberRed
import com.example.presentation.common.components.GlassmorphicCard
import com.example.presentation.common.components.MetricGaugeCard
import com.example.presentation.common.components.StatusBadge

@Composable
fun LiveTelemetryScreen(
    state: LiveTelemetryUiState,
    onEvent: (LiveTelemetryUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // HUD Mirror mode flips horizontally for reflection onto windshield
    val contentScaleX = if (state.isHudMirrorMode) -1f else 1f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonDark)
            .scale(scaleX = contentScaleX, scaleY = 1f)
            .padding(16.dp)
            .testTag("live_telemetry_screen")
    ) {
        // Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusBadge(
                    label = state.connectionState,
                    color = if (state.isStreaming) CyberGreen else CyberAmber
                )
                if (state.isHudMirrorMode) {
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(label = "HUD ยิงกระจก", color = CyberCyan)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { onEvent(LiveTelemetryUiEvent.ToggleHudMirror) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.testTag("hud_mirror_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ScreenRotation,
                        contentDescription = "HUD Mode",
                        tint = CyberCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "HUD")
                }

                Button(
                    onClick = { onEvent(LiveTelemetryUiEvent.ToggleStreaming) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isStreaming) CyberAmber else CyberGreen
                    ),
                    modifier = Modifier.testTag("toggle_stream_button")
                ) {
                    Icon(
                        imageVector = if (state.isStreaming) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Toggle Stream",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.isStreaming) "หยุดชั่วคราว" else "เริ่มสตรีม",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Critical Safety Alert Banner
        AnimatedVisibility(visible = !state.safetyStatus.isSafe) {
            state.safetyStatus.criticalAlerts.firstOrNull()?.let { alert ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberRed.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberRed.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Safety Alert",
                            tint = CyberRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = alert,
                            color = CyberRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Live Metric Gauge Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.sensors, key = { it.id }) { sensor ->
                val accentColor = when {
                    sensor.isCritical -> CyberRed
                    sensor.isWarning -> CyberAmber
                    sensor.id == "engine_rpm" -> CyberCyan
                    sensor.id == "vehicle_speed" -> CyberGreen
                    else -> CyberCyan
                }

                MetricGaugeCard(
                    title = sensor.displayNameTh,
                    value = sensor.value,
                    unit = sensor.unit,
                    min = sensor.minSafeValue,
                    max = sensor.maxSafeValue,
                    accentColor = accentColor,
                    onClick = { onEvent(LiveTelemetryUiEvent.SelectSensor(sensor.id)) }
                )
            }
        }
    }
}
