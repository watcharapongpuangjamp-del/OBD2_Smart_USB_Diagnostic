package com.example.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.common.chart.D3InteractiveChart
import com.example.presentation.common.components.CarbonDark
import com.example.presentation.common.components.CyberAmber
import com.example.presentation.common.components.CyberCyan
import com.example.presentation.common.components.CyberGreen
import com.example.presentation.common.components.StatusBadge
import com.example.presentation.common.components.TextMuted

@Composable
fun TelemetryDashboardScreen(
    state: TelemetryDashboardUiState,
    onEvent: (TelemetryDashboardUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonDark)
            .padding(16.dp)
            .testTag("telemetry_dashboard_screen")
    ) {
        // Dashboard Control Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ShowChart,
                        contentDescription = "Telemetry Charts",
                        tint = CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "แดชบอร์ดกราฟ D3 Telemetry",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "กราฟสดความละเอียดสูง แตะ/ลากเพื่อดูประวัติ",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Customize Button
                OutlinedButton(
                    onClick = { onEvent(TelemetryDashboardUiEvent.OpenCustomizeModal) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.testTag("btn_customize_dashboard")
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Customize",
                        tint = CyberCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "ปรับแต่ง", fontSize = 12.sp)
                }

                // Streaming Play/Pause Button
                Button(
                    onClick = { onEvent(TelemetryDashboardUiEvent.ToggleStreaming) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isStreaming) CyberAmber else CyberGreen
                    ),
                    modifier = Modifier.testTag("btn_toggle_telemetry_stream")
                ) {
                    Icon(
                        imageVector = if (state.isStreaming) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Stream Toggle",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.isStreaming) "หยุด" else "เล่น",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Time Window & Status Pill Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StatusBadge(
                    label = if (state.isStreaming) "LIVE สตรีมมิ่ง" else "PAUSED หยุดชั่วคราว",
                    color = if (state.isStreaming) CyberGreen else CyberAmber
                )
                StatusBadge(
                    label = "หน้าต่างเวลา ${state.timeWindowSeconds}s",
                    color = CyberCyan
                )
            }

            Text(
                text = "กำลังแสดง ${state.activeVisibleMetrics.size} กราฟ",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // List of Active D3 Interactive Charts
        if (state.activeVisibleMetrics.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "No charts",
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ยังไม่ได้เลือกแสดงผลกราฟเซนเซอร์",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = { onEvent(TelemetryDashboardUiEvent.OpenCustomizeModal) },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                    ) {
                        Text(text = "กดเพื่อเลือกกราฟที่ต้องการ", color = Color.Black)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.activeVisibleMetrics, key = { it.id }) { config ->
                    val dataPoints = state.chartDataMap[config.id] ?: emptyList()

                    D3InteractiveChart(
                        title = config.displayNameTh,
                        unit = config.unit,
                        dataPoints = dataPoints,
                        lineColor = config.color,
                        warningThreshold = config.warningThreshold,
                        criticalThreshold = config.criticalThreshold,
                        fixedMinDomain = config.fixedMinDomain,
                        fixedMaxDomain = config.fixedMaxDomain
                    )
                }
            }
        }

        // Customization Dialog
        if (state.showCustomizeModal) {
            CustomizeDashboardDialog(
                metricConfigs = state.metricConfigs,
                selectedTimeWindow = state.timeWindowSeconds,
                onToggleVisibility = { onEvent(TelemetryDashboardUiEvent.ToggleMetricVisibility(it)) },
                onMoveUp = { onEvent(TelemetryDashboardUiEvent.MoveMetricUp(it)) },
                onMoveDown = { onEvent(TelemetryDashboardUiEvent.MoveMetricDown(it)) },
                onSelectTimeWindow = { onEvent(TelemetryDashboardUiEvent.SetTimeWindow(it)) },
                onResetBuffers = { onEvent(TelemetryDashboardUiEvent.ResetBuffers) },
                onDismiss = { onEvent(TelemetryDashboardUiEvent.DismissCustomizeModal) }
            )
        }
    }
}
