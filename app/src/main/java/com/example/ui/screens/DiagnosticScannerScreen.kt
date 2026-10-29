package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.UsbOff
import androidx.compose.material.icons.filled.Warning
import com.example.ui.components.ObdConnectionDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.DiagnosticSystem
import com.example.data.model.DtcCategory
import com.example.data.model.DtcCode
import com.example.data.model.DtcSeverity
import com.example.data.model.LiveSensorData
import com.example.ui.MainViewModel

@Composable
fun DiagnosticScannerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH

    val selectedSystem by viewModel.selectedDiagnosticSystem.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val currentScanStep by viewModel.currentScanSystem.collectAsState()
    val activeDtcs by viewModel.activeDtcs.collectAsState()
    val isMilOn by viewModel.isMilOn.collectAsState()
    val freezeFrame by viewModel.freezeFrame.collectAsState()
    val sensors by viewModel.liveSensors.collectAsState()

    var selectedDtcForDetail by remember { mutableStateOf<DtcCode?>(null) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    var showConnectionDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showActiveTestDialog by remember { mutableStateOf(false) }
    var showSpecialFunctionsDialog by remember { mutableStateOf(false) }
    val isUsbConnected by viewModel.isUsbConnected.collectAsState()
    val isUsbConnecting by viewModel.isUsbConnecting.collectAsState()
    val selectedUsbDevice by viewModel.selectedUsbDevice.collectAsState()
    val baudRate by viewModel.selectedBaudRate.collectAsState()
    val isFetchingManual by viewModel.isFetchingManual.collectAsState()
    val repairManualContent by viewModel.repairManualContent.collectAsState()

    if (showConnectionDialog) {
        ObdConnectionDialog(
            viewModel = viewModel,
            onDismiss = { showConnectionDialog = false }
        )
    }

    if (showSearchDialog) {
        com.example.ui.components.DtcSearchDialog(
            viewModel = viewModel,
            onDismiss = { showSearchDialog = false }
        )
    }

    if (showActiveTestDialog) {
        com.example.ui.components.ActiveTestDialog(
            viewModel = viewModel,
            onDismiss = { showActiveTestDialog = false }
        )
    }

    if (showSpecialFunctionsDialog) {
        com.example.ui.components.SpecialFunctionsDialog(
            viewModel = viewModel,
            onDismiss = { showSpecialFunctionsDialog = false }
        )
    }

    if (isFetchingManual || repairManualContent != null) {
        com.example.ui.components.RepairManualDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.clearRepairManual() }
        )
    }

    // Filter active DTCs according to selected module
    val filteredDtcs = when (selectedSystem) {
        DiagnosticSystem.ALL -> activeDtcs
        DiagnosticSystem.ENGINE -> activeDtcs.filter { it.category == DtcCategory.POWERTRAIN && !it.code.startsWith("P07") && !it.code.startsWith("P08") }
        DiagnosticSystem.POWERTRAIN -> activeDtcs.filter { it.category == DtcCategory.POWERTRAIN || it.code == "U0101" }
        DiagnosticSystem.CHASSIS -> activeDtcs.filter { it.category == DtcCategory.CHASSIS || it.code == "U0121" }
        DiagnosticSystem.ELECTRICAL -> activeDtcs.filter { it.category == DtcCategory.BODY || it.category == DtcCategory.NETWORK }
        DiagnosticSystem.TRANSMISSION -> activeDtcs.filter { it.code.startsWith("P07") || it.code.startsWith("P08") || it.code == "U0101" }
        DiagnosticSystem.ABS -> activeDtcs.filter { it.category == DtcCategory.CHASSIS || it.code == "U0121" }
        DiagnosticSystem.AIRBAG -> activeDtcs.filter { it.category == DtcCategory.BODY && (it.code.startsWith("B0") || it.code == "B1000") }
        DiagnosticSystem.BODY -> activeDtcs.filter { it.category == DtcCategory.BODY && (it.code.startsWith("B1") && it.code != "B1000" || it.code.startsWith("B2")) }
        DiagnosticSystem.NETWORK -> activeDtcs.filter { it.category == DtcCategory.NETWORK }
        DiagnosticSystem.IMMOBILIZER -> activeDtcs.filter { it.code.startsWith("B1000") || it.code.startsWith("B16") || it.code.startsWith("P16") || it.category == DtcCategory.BODY }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isTh) "ระบบสแกนวินิจฉัยแยกโมดูล (Advanced Diagnostics)" else "Advanced Module Diagnostics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isTh) "รองรับ ECM, TCM (เกียร์), ABS, SRS (ถุงลม), BCM (ตัวถัง) & CAN" else "Covers ECM, TCM Transmission, ABS, SRS Airbag, BCM & CAN Gateway",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // USB OBD Link Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isUsbConnected) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { showConnectionDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isUsbConnected) Icons.Default.Usb else Icons.Default.UsbOff,
                                contentDescription = "USB",
                                tint = if (isUsbConnected) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isUsbConnected) "USB: OK" else "USB: OFF",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isUsbConnected) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // MIL Status Indicator
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isMilOn) Color(0xFFFFEAEA) else Color(0xFFE8F5E9)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isMilOn) Color(0xFFE53935) else Color(0xFF4CAF50))
                            )
                            Text(
                                text = if (isMilOn) "MIL: ON" else "MIL: OFF",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isMilOn) Color(0xFFE53935) else Color(0xFF4CAF50)
                            )
                        }
                    }

                    // Dictionary Search Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.clickable { showSearchDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isTh) "ค้นหา" else "Search",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        // Diagnostic System Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = DiagnosticSystem.values().indexOf(selectedSystem),
                edgePadding = 0.dp,
                divider = {},
                containerColor = Color.Transparent
            ) {
                DiagnosticSystem.values().forEach { sys ->
                    val isSelected = selectedSystem == sys
                    val icon = getSystemIcon(sys)
                    Tab(
                        selected = isSelected,
                        onClick = { viewModel.selectDiagnosticSystem(sys) },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (isTh) sys.titleTh.split(" ")[0] else sys.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    )
                }
            }
        }

        // Bi-Directional Active Test & Special Component Registration Quick Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showActiveTestDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00))
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTh) "Active Test สั่งอุปกรณ์" else "Active Test Mode",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { showSpecialFunctionsDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                ) {
                    Icon(Icons.Default.Build, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTh) "ลงทะเบียนอุปกรณ์ใหม่" else "Component Register",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Active Module Status Card & Real-time Live Sensor Snapshot for Selected System
        item {
            ModuleTelemetryBannerCard(
                system = selectedSystem,
                sensors = sensors,
                isTh = isTh
            )
        }

        // Scan Action Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scan_action_panel"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isTh) selectedSystem.titleTh else selectedSystem.titleEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${selectedSystem.ecuName} • ${selectedSystem.protocol}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp
                            )
                        }
                    }

                    if (isScanning) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            LinearProgressIndicator(
                                progress = { scanProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Text(
                                text = currentScanStep,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Scan & Clear Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.runModuleDiagnosticScan(selectedSystem) },
                            enabled = !isScanning,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_run_scan"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (selectedSystem == DiagnosticSystem.ALL) {
                                    if (isTh) "สแกนทุกระบบ" else "Full Scan"
                                } else {
                                    if (isTh) "สแกนโมดูลนี้" else "Scan Module"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { showClearConfirmDialog = true },
                            enabled = !isScanning && filteredDtcs.isNotEmpty(),
                            modifier = Modifier.testTag("btn_clear_dtc"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFE53935)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isTh) "ลบโค้ด" else "Clear DTC",
                                color = Color(0xFFE53935),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Technical Manual Button
                    OutlinedButton(
                        onClick = { viewModel.fetchRepairManual() },
                        enabled = !isScanning,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTh) "ดึงคู่มือซ่อมรถยนต์ (OEM Manual)" else "Fetch Technical Repair Manual",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Active Fault Codes List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isTh) "รหัสข้อผิดพลาดที่ตรวจพบ (${filteredDtcs.size})" else "Detected DTC Faults (${filteredDtcs.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (filteredDtcs.isNotEmpty()) {
                    Text(
                        text = if (isTh) "แตะเพื่อดูวิธีแก้" else "Tap for repair guide",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (filteredDtcs.isEmpty() && !isScanning) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = if (isTh) "ไม่พบรหัสข้อผิดพลาดในโมดูลนี้" else "No Fault Codes in this Module",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isTh) "ระบบทำงานเป็นปกติ หรือกดปุ่ม 'สแกน' เพื่อตรวจสอบสัญญาณเซนเซอร์" else "All sub-circuits operating nominally. Run scan to re-verify.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredDtcs) { dtc ->
                DtcFaultCodeCard(
                    dtc = dtc,
                    isTh = isTh,
                    onClick = { selectedDtcForDetail = dtc },
                    onAskAi = {
                        val prompt = if (isTh) {
                            "ช่วยอธิบายรหัสปัญหา ${dtc.code} (${dtc.thTitle}) สำหรับโมดูล ${selectedSystem.titleTh} วิเคราะห์สาเหตุและขั้นตอนแก้ไขสำหรับรถยนต์ในไทยให้ทีครับ"
                        } else {
                            "Please analyze DTC code ${dtc.code} (${dtc.enTitle}) for ${selectedSystem.titleEn}."
                        }
                        viewModel.setTab(4) // AI Mechanic Tab
                        viewModel.sendChatMessage(prompt)
                    }
                )
            }
        }

        // Freeze Frame Snapshot (if available)
        if (freezeFrame != null && (selectedSystem == DiagnosticSystem.ALL || selectedSystem == DiagnosticSystem.ENGINE)) {
            item {
                FreezeFrameCard(freezeFrame = freezeFrame!!, isTh = isTh)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // DTC Detail Dialog
    if (selectedDtcForDetail != null) {
        val dtc = selectedDtcForDetail!!
        DtcDetailDialog(
            dtc = dtc,
            isTh = isTh,
            onDismiss = { selectedDtcForDetail = null },
            onConsultAi = {
                val prompt = if (isTh) {
                    "ช่วยอธิบายรหัสปัญหา ${dtc.code} (${dtc.thTitle}) และขั้นตอนการตรวจเช็คแก้ไขปัญหา พร้อมแนะนำอู่หรือราคาซ่อมในไทย"
                } else {
                    "Explain repair procedure for DTC ${dtc.code}: ${dtc.enTitle}."
                }
                selectedDtcForDetail = null
                viewModel.setTab(4)
                viewModel.sendChatMessage(prompt)
            }
        )
    }

    // Clear DTC Confirmation Dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = {
                Text(
                    text = if (isTh) "ยืนยันการลบรหัสข้อผิดพลาด?" else "Confirm DTC Clear & ECU Reset?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isTh) {
                        "การส่งคำสั่ง Mode 04 / UDS 0x14 จะลบรหัส DTC, รีเซ็ตค่า Freeze Frame และสั่งดับไฟ Check Engine / MIL คุณต้องการดำเนินการต่อหรือไม่?"
                    } else {
                        "Sending Mode 04 / UDS 0x14 will clear diagnostic trouble codes, reset freeze frame snapshots, and extinguish the MIL lamp. Continue?"
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearDtcFaultCodes(selectedSystem)
                        showClearConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text(if (isTh) "ลบโค้ดทันที" else "Clear Codes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text(if (isTh) "ยกเลิก" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun ModuleTelemetryBannerCard(
    system: DiagnosticSystem,
    sensors: LiveSensorData,
    isTh: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                        imageVector = getSystemIcon(system),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (isTh) "มาตรวัดสดเฉพาะโมดูล (Live Module PIDs)" else "Module Live Sensor Stream",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF4CAF50).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "CAN ONLINE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Specific Sensor Badges based on module
            when (system) {
                DiagnosticSystem.ALL, DiagnosticSystem.ENGINE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "รอบเครื่อง" else "RPM", "${sensors.rpm}", "RPM")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "ความร้อน ECT" else "Coolant", "${sensors.coolantTempC}", "°C")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "แรงดันราง" else "Rail Press.", "${String.format("%.0f", sensors.fuelRailPressureBar)}", "Bar")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "บูสต์เทอร์โบ" else "Boost", "${String.format("%.2f", sensors.boostPressureBar)}", "Bar")
                    }
                }
                DiagnosticSystem.POWERTRAIN, DiagnosticSystem.TRANSMISSION -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "น้ำมันเกียร์" else "ATF Temp", "${sensors.transmissionFluidTempC}", "°C")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "เกียร์ปัจจุบัน" else "Gear", sensors.currentGear, "")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "สลิปคลัตช์" else "Slip RPM", "${sensors.torqueConverterSlipRpm}", "RPM")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "แรงดัน Line" else "Line Press", "${String.format("%.0f", sensors.tcmLinePressurePsi)}", "PSI")
                    }
                }
                DiagnosticSystem.CHASSIS, DiagnosticSystem.ABS -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryMiniChip(Modifier.weight(1f), "FL Wheel", "${String.format("%.1f", sensors.wheelSpeedFlKmh)}", "km/h")
                        TelemetryMiniChip(Modifier.weight(1f), "FR Wheel", "${String.format("%.1f", sensors.wheelSpeedFrKmh)}", "km/h")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "แรงดันเบรก" else "Brake Press", "${sensors.brakeFluidPressureBar}", "Bar")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "ไฟปั๊ม ABS" else "ABS Motor", "${sensors.absPumpMotorVoltage}", "V")
                    }
                }
                DiagnosticSystem.ELECTRICAL, DiagnosticSystem.BODY, DiagnosticSystem.IMMOBILIZER -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "แบตเตอรี่ 12V" else "Battery", "${String.format("%.2f", sensors.batteryVoltage)}", "V")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "ไดชาร์จ" else "Alternator", "${String.format("%.0f", sensors.alternatorDutyPercent)}", "%")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "กระแสไฟหน้า" else "Headlight", "${sensors.headlightCurrentAmp}", "A")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "Smart Key" else "Key RSSI", "${sensors.smartKeySignalRssi}", "dBm")
                    }
                }
                DiagnosticSystem.AIRBAG -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "สายแพรคนขับ" else "Driver Squib", "${sensors.driverAirbagLoopResistanceOhm}", "Ω")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "สายแพรผู้โดยสาร" else "Pass. Squib", "${sensors.passengerAirbagLoopResistanceOhm}", "Ω")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "เซนเซอร์ชน" else "Crash Sensor", sensors.frontImpactSensor1Status, "")
                        TelemetryMiniChip(Modifier.weight(1f), if (isTh) "สายแพรพวงมาลัย" else "Clockspring", sensors.clockspringIntegrity, "")
                    }
                }
                DiagnosticSystem.NETWORK -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryMiniChip(Modifier.weight(1f), "CAN Speed", "500", "kbps")
                        TelemetryMiniChip(Modifier.weight(1f), "ECM Link", "OK", "")
                        TelemetryMiniChip(Modifier.weight(1f), "TCM Link", "OK", "")
                        TelemetryMiniChip(Modifier.weight(1f), "ABS Link", "OK", "")
                    }
                }
            }
        }
    }
}

@Composable
fun TelemetryMiniChip(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    unit: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                maxLines = 1
            )
            Text(
                text = "$value $unit".trim(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
        }
    }
}

@Composable
fun DtcFaultCodeCard(
    dtc: DtcCode,
    isTh: Boolean,
    onClick: () -> Unit,
    onAskAi: () -> Unit
) {
    val severityColor = when (dtc.severity) {
        DtcSeverity.CRITICAL -> Color(0xFFE53935)
        DtcSeverity.WARNING -> Color(0xFFFF9800)
        DtcSeverity.INFO -> Color(0xFF2196F3)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("dtc_item_${dtc.code}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, severityColor.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = severityColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = dtc.code,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = severityColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = dtc.category.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = dtc.estimatedCostThb,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Description
            Text(
                text = if (isTh) dtc.thTitle else dtc.enTitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (isTh) dtc.thDescription else dtc.enDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            // System affected & Ask AI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${if (isTh) "ระบบ: " else "System: "}${dtc.affectedSystem}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

                OutlinedButton(
                    onClick = onAskAi,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isTh) "วิเคราะห์ AI" else "AI Guide",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun FreezeFrameCard(
    freezeFrame: com.example.data.model.FreezeFrameData,
    isTh: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isTh) "ภาพถ่ายสภาวะขณะเกิดปัญหา (Freeze Frame Snapshot)" else "DTC Trigger Freeze Frame",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = freezeFrame.triggerDtc,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Primary Engine freeze frame row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryMiniChip(Modifier.weight(1f), "Engine RPM", "${freezeFrame.engineRpm}", "RPM")
                TelemetryMiniChip(Modifier.weight(1f), "Speed", "${freezeFrame.vehicleSpeedKmh}", "km/h")
                TelemetryMiniChip(Modifier.weight(1f), "Coolant", "${freezeFrame.coolantTempCelsius}", "°C")
                TelemetryMiniChip(Modifier.weight(1f), "Engine Load", "${String.format("%.0f", freezeFrame.engineLoadPercent)}", "%")
            }

            // Subsystem freeze frame row (Powertrain / Chassis / Electrical)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryMiniChip(Modifier.weight(1f), if (isTh) "น้ำมันเกียร์" else "ATF Temp", "${freezeFrame.transmissionFluidTempC}", "°C")
                TelemetryMiniChip(Modifier.weight(1f), if (isTh) "เกียร์" else "Gear", freezeFrame.gearAtFault, "")
                TelemetryMiniChip(Modifier.weight(1f), if (isTh) "ล้อหน้า FL" else "FL Speed", "${String.format("%.0f", freezeFrame.wheelSpeedFlKmh)}", "km/h")
                TelemetryMiniChip(Modifier.weight(1f), if (isTh) "แบตเตอรี่" else "Battery", "${String.format("%.1f", freezeFrame.batteryVoltage)}", "V")
            }
        }
    }
}

@Composable
fun DtcDetailDialog(
    dtc: DtcCode,
    isTh: Boolean,
    onDismiss: () -> Unit,
    onConsultAi: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = dtc.code, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                Text(text = if (isTh) dtc.thTitle else dtc.enTitle, style = MaterialTheme.typography.titleSmall)
            }
        },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    Text(
                        text = if (isTh) dtc.thDescription else dtc.enDescription,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item {
                    Text(
                        text = if (isTh) "สาเหตุที่พบบ่อย (Common Causes):" else "Common Causes:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    val causes = if (isTh) dtc.commonCausesTh else dtc.commonCausesEn
                    causes.forEach { cause ->
                        Text("• $cause", style = MaterialTheme.typography.bodySmall)
                    }
                }

                item {
                    Text(
                        text = if (isTh) "อาการที่แสดง (Symptoms):" else "Symptoms:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    val symptoms = if (isTh) dtc.symptomsTh else dtc.symptomsEn
                    symptoms.forEach { symptom ->
                        Text("• $symptom", style = MaterialTheme.typography.bodySmall)
                    }
                }

                item {
                    Text(
                        text = if (isTh) "ราคาประเมินค่าซ่อม/อะไหล่ในไทย: ${dtc.estimatedCostThb}" else "Estimated Cost: ${dtc.estimatedCostThb}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onConsultAi) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isTh) "ปรึกษาช่าง AI เพื่อซ่อม" else "Ask AI Mechanic")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isTh) "ปิด" else "Close")
            }
        }
    )
}

fun getSystemIcon(system: DiagnosticSystem): ImageVector {
    return when (system) {
        DiagnosticSystem.ALL -> Icons.Default.DirectionsCar
        DiagnosticSystem.ENGINE -> Icons.Default.Speed
        DiagnosticSystem.POWERTRAIN -> Icons.Default.Build
        DiagnosticSystem.CHASSIS -> Icons.Default.Security
        DiagnosticSystem.ELECTRICAL -> Icons.Default.ElectricCar
        DiagnosticSystem.TRANSMISSION -> Icons.Default.Settings
        DiagnosticSystem.ABS -> Icons.Default.Security
        DiagnosticSystem.AIRBAG -> Icons.Default.Warning
        DiagnosticSystem.BODY -> Icons.Default.LightMode
        DiagnosticSystem.NETWORK -> Icons.Default.Lan
        DiagnosticSystem.IMMOBILIZER -> Icons.Default.Security
    }
}
