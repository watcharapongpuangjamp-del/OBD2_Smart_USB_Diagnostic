package com.example.presentation.screen.diagnostic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DtcFault
import com.example.domain.model.DtcSeverity
import com.example.domain.model.VehicleSystemCategory
import com.example.presentation.common.components.CarbonCard
import com.example.presentation.common.components.CarbonDark
import com.example.presentation.common.components.CyberAmber
import com.example.presentation.common.components.CyberCyan
import com.example.presentation.common.components.CyberGreen
import com.example.presentation.common.components.CyberRed
import com.example.presentation.common.components.GlassmorphicCard
import com.example.presentation.common.components.StatusBadge
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Search
import com.example.presentation.common.components.TextMuted

@Composable
fun DiagnosticScannerScreen(
    state: DiagnosticScannerUiState,
    onEvent: (DiagnosticScannerUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToDtcDatabase: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonDark)
            .padding(16.dp)
            .testTag("diagnostic_scanner_screen")
    ) {
        // Header Summary Card
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (state.criticalCount > 0) CyberRed.copy(alpha = 0.5f) else CyberCyan.copy(alpha = 0.3f)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ระบบวินิจฉัย OBD-II / CAN Bus",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = state.activeSession?.vehicleModel ?: "Toyota Hilux Revo 2.4 GD",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }

                StatusBadge(
                    label = if (state.detectedFaults.isEmpty() && !state.isScanning) "ระบบปกติ" else "${state.detectedFaults.size} โค้ด",
                    color = when {
                        state.criticalCount > 0 -> CyberRed
                        state.warningCount > 0 -> CyberAmber
                        else -> CyberGreen
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onEvent(DiagnosticScannerUiEvent.StartScan) },
                    enabled = !state.isScanning,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("start_scan_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Scan",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (state.isScanning) "สแกน..." else "สแกน",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { onEvent(DiagnosticScannerUiEvent.ExportPdfReport) },
                    enabled = !state.isScanning && !state.isGeneratingPdf,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2836)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("export_pdf_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "PDF Report",
                        tint = CyberCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.isGeneratingPdf) "สร้าง PDF..." else "รายงาน PDF",
                        color = CyberCyan,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }

                OutlinedButton(
                    onClick = { onEvent(DiagnosticScannerUiEvent.ClearFaultCodes) },
                    enabled = !state.isScanning && state.detectedFaults.isNotEmpty() && !state.isClearing,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberAmber),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberAmber.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("clear_dtc_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear DTC",
                        tint = CyberAmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "ลบโค้ด", fontSize = 12.sp)
                }
            }

            // Scanning progress
            AnimatedVisibility(visible = state.isScanning) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = state.currentProgressTask,
                            color = CyberCyan,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${(state.scanProgress * 100).toInt()}%",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { state.scanProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = CyberCyan,
                        trackColor = Color(0xFF1E2836)
                    )
                }
            }
        }

        // Offline DTC Database Banner Button
        if (onNavigateToDtcDatabase != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNavigateToDtcDatabase() }
                    .testTag("btn_banner_offline_dtc_db"),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF131D28),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ค้นหาความหมายรหัส DTC (ฐานข้อมูลออฟไลน์ Room)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "เปิดดูความหมาย อาการ สาเหตุ และวิธีแก้ปัญหาแม้ออฟไลน์",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Go",
                        tint = CyberCyan,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // System Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            item {
                FilterChip(
                    selected = state.selectedCategoryFilter == null,
                    onClick = { onEvent(DiagnosticScannerUiEvent.SelectCategoryFilter(null)) },
                    label = { Text("ทั้งหมด (${state.detectedFaults.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                        selectedLabelColor = CyberCyan
                    )
                )
            }
            item {
                FilterChip(
                    selected = state.selectedCategoryFilter == VehicleSystemCategory.POWERTRAIN,
                    onClick = { onEvent(DiagnosticScannerUiEvent.SelectCategoryFilter(VehicleSystemCategory.POWERTRAIN)) },
                    label = { Text("เครื่องยนต์/เกียร์ (P)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberAmber.copy(alpha = 0.2f),
                        selectedLabelColor = CyberAmber
                    )
                )
            }
            item {
                FilterChip(
                    selected = state.selectedCategoryFilter == VehicleSystemCategory.CHASSIS,
                    onClick = { onEvent(DiagnosticScannerUiEvent.SelectCategoryFilter(VehicleSystemCategory.CHASSIS)) },
                    label = { Text("ช่วงล่าง/เบรก (C)") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedCategoryFilter == VehicleSystemCategory.NETWORK,
                    onClick = { onEvent(DiagnosticScannerUiEvent.SelectCategoryFilter(VehicleSystemCategory.NETWORK)) },
                    label = { Text("เครือข่าย CAN (U)") }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Fault Code List
        if (state.filteredFaults.isEmpty() && !state.isScanning) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "No faults",
                        tint = CyberGreen,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "ไม่พบรหัสข้อบกพร่อง (No DTCs Found)",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ระบบเครื่องยนต์และระบบไฟฟ้าทำงานปกติสมบูรณ์",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.filteredFaults, key = { it.code }) { fault ->
                    DtcFaultCard(
                        fault = fault,
                        isSelected = state.selectedFault?.code == fault.code,
                        onClick = { onEvent(DiagnosticScannerUiEvent.SelectFault(fault)) },
                        onAiClick = { onEvent(DiagnosticScannerUiEvent.RequestAiDiagnosis(fault)) }
                    )
                }

                // AI Insight Panel
                item {
                    state.aiInsightText?.let { insight ->
                        GlassmorphicCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            borderColor = CyberCyan.copy(alpha = 0.5f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI Insight",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI ช่างยนต์วิเคราะห์เชิงลึก",
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = insight,
                                color = Color.White,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                            if (state.aiReport != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { onEvent(DiagnosticScannerUiEvent.OpenDiyRepairGuide) },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("open_diy_guide_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = "DIY Guide",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "เปิดคู่มือซ่อม DIY & ประเมินราคาอะไหล่",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.showDiyGuideDialog) {
            DiyRepairGuideDialog(
                report = state.aiReport,
                isLoading = state.isAiThinking,
                onDismiss = { onEvent(DiagnosticScannerUiEvent.DismissDiyRepairGuide) },
                onExportPdf = { onEvent(DiagnosticScannerUiEvent.ExportPdfReport) }
            )
        }
    }
}

@Composable
fun DtcFaultCard(
    fault: DtcFault,
    isSelected: Boolean,
    onClick: () -> Unit,
    onAiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val severityColor = when (fault.severity) {
        DtcSeverity.CRITICAL -> CyberRed
        DtcSeverity.WARNING -> CyberAmber
        DtcSeverity.INFO -> CyberCyan
    }

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("dtc_card_${fault.code}"),
        borderColor = if (isSelected) severityColor else severityColor.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = fault.code,
                    color = severityColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(10.dp))
                StatusBadge(
                    label = when (fault.severity) {
                        DtcSeverity.CRITICAL -> "อันตราย"
                        DtcSeverity.WARNING -> "เตือน"
                        DtcSeverity.INFO -> "ข้อมูล"
                    },
                    color = severityColor
                )
            }

            if (fault.estimatedRepairCostThb.isNotBlank()) {
                Text(
                    text = fault.estimatedRepairCostThb,
                    color = CyberGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = fault.titleTh,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = fault.titleEn,
            color = TextMuted,
            fontSize = 12.sp
        )

        if (fault.possibleCauses.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "สาเหตุที่พบบ่อย: ${fault.possibleCauses.joinToString(", ")}",
                color = Color(0xFFB0BEC5),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onAiClick,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Diagnose",
                    tint = CyberCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ถามช่างยนต์ AI",
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
