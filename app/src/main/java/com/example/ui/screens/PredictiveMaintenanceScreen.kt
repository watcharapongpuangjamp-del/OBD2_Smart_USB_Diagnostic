package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.DiagnosticSystem
import com.example.data.model.PredictiveComponent
import com.example.data.model.PredictiveStatus
import com.example.data.model.UrgencyLevel
import com.example.service.DrivingMode
import com.example.ui.MainViewModel

@Composable
fun PredictiveMaintenanceScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH
    val summary by viewModel.predictiveHealth.collectAsState()
    val vehicle by viewModel.selectedVehicle.collectAsState()
    val drivingMode by viewModel.drivingMode.collectAsState()

    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredComponents = when (selectedFilter) {
        "CRITICAL" -> summary.components.filter { it.status == PredictiveStatus.IMMINENT_FAILURE }
        "ATTENTION" -> summary.components.filter { it.status == PredictiveStatus.ATTENTION || it.status == PredictiveStatus.IMMINENT_FAILURE }
        "HEALTHY" -> summary.components.filter { it.status == PredictiveStatus.HEALTHY || it.status == PredictiveStatus.MONITORING }
        else -> summary.components
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Screen Header & AI Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isTh) "ระบบพยากรณ์ความเสื่อมสภาพชิ้นส่วน" else "Predictive Component Health",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isTh) "วิเคราะห์ข้อมูลย้อนหลัง ผสานเซนเซอร์สด OBD-II & สภาพการขับขี่" else "Historical records & Real-time telemetry failure forecasting",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.clickable { viewModel.recalculatePredictiveHealth() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isTh) "คำนวณใหม่" else "Re-evaluate",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // --- Timely Alerts Banner ---
        val urgentAlerts = summary.components.filter { it.urgencyLevel == UrgencyLevel.CRITICAL_IMMEDIATE || it.urgencyLevel == UrgencyLevel.HIGH_PRIORITY }
        if (urgentAlerts.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = BorderStroke(1.dp, Color(0xFFE53935))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE53935))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTh) "การแจ้งเตือนอัจฉริยะ (Timely Alerts)" else "Timely Alerts",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                        }
                        urgentAlerts.forEach { alert ->
                            val msgTh = when(alert.id) {
                                "brake_pads" -> "ตรวจพบความเป็นไปได้ที่ผ้าเบรกสึกหรอ แนะนำให้ตรวจสอบภายใน 1,000 กม."
                                "cooling_system" -> "เซ็นเซอร์อุณหภูมิน้ำหล่อเย็นแสดงสัญญาณเสื่อมสภาพ แนะนำให้ตรวจสอบ"
                                else -> alert.recommendationTh
                            }
                            val msgEn = when(alert.id) {
                                "brake_pads" -> "Potential brake wear detected, recommend inspection within 1000 km."
                                "cooling_system" -> "Coolant temperature sensor shows signs of degradation, recommend check."
                                else -> alert.recommendationEn
                            }
                            Text(
                                text = "• ${if (isTh) msgTh else msgEn}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFB71C1C)
                            )
                        }
                    }
                }
            }
        }

        // Overview Health Score Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("predictive_overview_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isTh) "คะแนนสุขภาพยานยนต์รวม (RUL Index)" else "Overall Vehicle Health Index",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${vehicle?.make ?: "Toyota"} ${vehicle?.model ?: "Hilux Revo"} • ${summary.mileageAnalyzedKm} km",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Score Badge
                        val scoreColor = when {
                            summary.overallHealthScore >= 85 -> Color(0xFF4CAF50)
                            summary.overallHealthScore >= 65 -> Color(0xFFFF9800)
                            else -> Color(0xFFE53935)
                        }

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(scoreColor.copy(alpha = 0.15f))
                                .border(2.dp, scoreColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${summary.overallHealthScore}%",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = scoreColor
                                )
                            }
                        }
                    }

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { summary.overallHealthScore / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = when {
                            summary.overallHealthScore >= 85 -> Color(0xFF4CAF50)
                            summary.overallHealthScore >= 65 -> Color(0xFFFF9800)
                            else -> Color(0xFFE53935)
                        },
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                    // Summary Status Banner
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (summary.imminentFailureCount > 0) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (summary.imminentFailureCount > 0) Color(0xFFE53935) else Color(0xFF4CAF50),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isTh) summary.riskCategoryTh else summary.riskCategoryEn,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = if (summary.imminentFailureCount > 0) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = if (isTh) "สภาวะ: ${summary.drivingStressFactorTh}" else "Stress: ${summary.drivingStressFactorEn}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Driving Stress Profile Selector (Interactively changes telemetry conditions)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (isTh) "จำลองสภาวะการขับขี่ (Driving Stress Mode):" else "Driving Stress Profile:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(DrivingMode.values()) { mode ->
                        val isSelected = drivingMode == mode
                        val label = when (mode) {
                            DrivingMode.IDLE -> if (isTh) "รอบเดินเบา / ติดไฟแดง" else "Idle / Traffic Stop"
                            DrivingMode.CRUISE -> if (isTh) "ขับทางไกลปกติ (Cruise)" else "Highway Cruise"
                            DrivingMode.HARD_ACCEL -> if (isTh) "เร่งแซง / บรรทุกหนัก" else "Hard Accel / Towing"
                            DrivingMode.OVERHEAT_WARNING -> if (isTh) "ความร้อนสะสมสูง (Overheat)" else "High Thermal Stress"
                        }

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.emulatorService.setDrivingMode(mode)
                                viewModel.recalculatePredictiveHealth()
                            },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }
        }

        // Filter Chips for Components
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" },
                    label = { Text(if (isTh) "ทั้งหมด (${summary.components.size})" else "All (${summary.components.size})") }
                )
                FilterChip(
                    selected = selectedFilter == "CRITICAL",
                    onClick = { selectedFilter = "CRITICAL" },
                    label = {
                        Text(
                            if (isTh) "⚠️ เสี่ยงชำรุด (${summary.imminentFailureCount})" else "⚠️ Imminent (${summary.imminentFailureCount})",
                            color = if (summary.imminentFailureCount > 0) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                FilterChip(
                    selected = selectedFilter == "ATTENTION",
                    onClick = { selectedFilter = "ATTENTION" },
                    label = { Text(if (isTh) "ถึงรอบเช็ค (${summary.activeAlertCount})" else "Due (${summary.activeAlertCount})") }
                )
                FilterChip(
                    selected = selectedFilter == "HEALTHY",
                    onClick = { selectedFilter = "HEALTHY" },
                    label = { Text(if (isTh) "สมบูรณ์" else "Optimal") }
                )
            }
        }

        // Component Risk Cards
        items(filteredComponents) { component ->
            PredictiveComponentCard(
                component = component,
                isTh = isTh,
                onAskAi = {
                    val prompt = if (isTh) {
                        "ช่วยวิเคราะห์ความเสี่ยงและวิธีซ่อมแซมชิ้นส่วน '${component.nameTh}' ของรถ ${vehicle?.make ?: "Toyota"} ${vehicle?.model ?: "Revo"} เลขไมล์ ${summary.mileageAnalyzedKm} กม. อาการ: ${component.reasonTh} แนะนำอะไหล่และค่าแรงโดยละเอียด"
                    } else {
                        "Please provide diagnostic breakdown and repair procedure for '${component.nameEn}' on ${vehicle?.make} ${vehicle?.model}. Symptoms: ${component.reasonEn}."
                    }
                    viewModel.setTab(4) // Switch to AI Mechanic tab
                    viewModel.sendChatMessage(prompt)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PredictiveComponentCard(
    component: PredictiveComponent,
    isTh: Boolean,
    onAskAi: () -> Unit
) {
    val statusColor = when (component.status) {
        PredictiveStatus.IMMINENT_FAILURE -> Color(0xFFE53935)
        PredictiveStatus.ATTENTION -> Color(0xFFFF9800)
        PredictiveStatus.MONITORING -> Color(0xFF2196F3)
        PredictiveStatus.HEALTHY -> Color(0xFF4CAF50)
    }

    val statusLabel = when (component.status) {
        PredictiveStatus.IMMINENT_FAILURE -> if (isTh) "เสี่ยงชำรุดเร่งด่วน (Imminent)" else "Imminent Failure"
        PredictiveStatus.ATTENTION -> if (isTh) "ควรตรวจเช็คตามรอบ" else "Attention Due"
        PredictiveStatus.MONITORING -> if (isTh) "เฝ้าระวังปกติ" else "Monitoring"
        PredictiveStatus.HEALTHY -> if (isTh) "สมบูรณ์ปกติ" else "Healthy"
    }

    var isExpanded by remember { mutableStateOf(component.status == PredictiveStatus.IMMINENT_FAILURE) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("predictive_item_${component.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (component.status == PredictiveStatus.IMMINENT_FAILURE) {
            androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE53935).copy(alpha = 0.8f))
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: System Chip + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = if (isTh) component.system.titleTh else component.system.titleEn,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Text(
                            text = statusLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = statusColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Title and Health Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isTh) component.nameTh else component.nameEn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isTh) component.nameEn else component.nameTh,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "${component.healthScore}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = statusColor
                )
            }

            // Health Progress Bar
            LinearProgressIndicator(
                progress = { component.healthScore / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = statusColor,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            )

            // Remaining Mileage & Days Horizon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isTh) "ระยะคงเหลือประเมิน: ~${component.estimatedRemainingKm} km" else "Est. RUL: ~${component.estimatedRemainingKm} km",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (isTh) "~${component.estimatedRemainingDays} วัน" else "~${component.estimatedRemainingDays} days",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Monitored Live Sensors Chips
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (isTh) "เซนเซอร์ที่ตรวจจับ (Live Sensors):" else "Monitored Telemetry:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    component.monitoredSensors.take(2).forEach { sensorVal ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = sensorVal,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Reason & Recommendation (Expandable or always visible for critical)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (component.status == PredictiveStatus.IMMINENT_FAILURE) Color(0xFFE53935).copy(alpha = 0.08f) else Color.Transparent)
                    .padding(if (component.status == PredictiveStatus.IMMINENT_FAILURE) 8.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (isTh) "สาเหตุ: ${component.reasonTh}" else "Cause: ${component.reasonEn}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isTh) "ข้อแนะนำ: ${component.recommendationTh}" else "Action: ${component.recommendationEn}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (component.status == PredictiveStatus.IMMINENT_FAILURE) Color(0xFFE53935) else MaterialTheme.colorScheme.primary
                )
            }

            // Part Cost & Ask AI Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isTh) "ประมาณการอะไหล่: ${component.estimatedPartCostThb}" else "Est. Cost: ${component.estimatedPartCostThb}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                OutlinedButton(
                    onClick = onAskAi,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isTh) "ปรึกษาช่าง AI" else "Ask AI",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
