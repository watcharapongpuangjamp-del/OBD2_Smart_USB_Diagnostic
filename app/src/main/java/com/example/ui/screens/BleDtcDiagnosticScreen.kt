package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DtcCode
import com.example.data.model.DtcSeverity
import com.example.service.BleConnectionState
import com.example.service.BluetoothObdDevice
import com.example.ui.BluetoothObdViewModel

@Composable
fun BleDtcDiagnosticScreen(
    viewModel: BluetoothObdViewModel,
    onConsultAi: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val connectionState by viewModel.connectionState.collectAsState()
    val discoveredDevices by viewModel.discoveredDevices.collectAsState()
    val connectedDeviceName by viewModel.connectedDeviceName.collectAsState()
    val dtcList by viewModel.dtcList.collectAsState()
    val isScanningDtcs by viewModel.isScanningDtcs.collectAsState()
    val isDemoMode by viewModel.isDemoMode.collectAsState()

    var showClearConfirmDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "สแกนรหัสปัญหารถยนต์ (BLE OBD-II)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "เชื่อมต่อ Bluetooth LE อ่านค่ารหัสข้อผิดพลาด DTC (Mode 03/07)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Connection Status Chip
                ConnectionStatusChip(
                    state = connectionState,
                    isDemo = isDemoMode,
                    deviceName = connectedDeviceName
                )
            }
        }

        // Bluetooth LE Scan & Connection Management
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = if (connectionState == BleConnectionState.CONNECTED) Icons.Default.BluetoothConnected else Icons.Default.Bluetooth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "อุปกรณ์ Bluetooth OBD-II",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Demo Mode Toggle Button
                        OutlinedButton(
                            onClick = { viewModel.toggleDemoMode() },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (isDemoMode) "ปิด Demo Mode" else "เปิด Demo Mode",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Device Scan Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.startScan() },
                            enabled = connectionState != BleConnectionState.SCANNING && !isDemoMode,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_ble_start_scan"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.BluetoothSearching, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (connectionState == BleConnectionState.SCANNING) "กำลังค้นหา..." else "ค้นหาบลูทูธ",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (connectionState == BleConnectionState.CONNECTED) {
                            OutlinedButton(
                                onClick = { viewModel.disconnect() },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("ตัดการเชื่อมต่อ", color = Color(0xFFE53935))
                            }
                        }
                    }

                    // Discovered Bluetooth Devices List
                    if (discoveredDevices.isNotEmpty() && connectionState != BleConnectionState.CONNECTED) {
                        Text(
                            text = "พบอุปกรณ์บลูทูธ (${discoveredDevices.size}):",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            discoveredDevices.forEach { dev ->
                                BleDeviceItem(
                                    device = dev,
                                    onConnect = { viewModel.connectDevice(dev) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Diagnostic Scan Controls (Run Scan & Clear DTCs)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ble_dtc_scan_panel"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "สั่งอ่านรหัสข้อผิดพลาด (Mode 03 / Mode 07)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (isScanningDtcs) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.scanDtcCodes() },
                            enabled = !isScanningDtcs && (connectionState == BleConnectionState.CONNECTED || isDemoMode),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_fetch_dtc_ble"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (isScanningDtcs) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("เริ่มสแกนอ่านโค้ด DTC", fontWeight = FontWeight.Bold)
                            }
                        }

                        OutlinedButton(
                            onClick = { showClearConfirmDialog = true },
                            enabled = !isScanningDtcs && dtcList.isNotEmpty(),
                            modifier = Modifier.testTag("btn_clear_dtc_ble"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ลบโค้ด (Mode 04)", color = Color(0xFFE53935))
                        }
                    }
                }
            }
        }

        // DTC List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "รายการรหัสความผิดปกติที่พบ (${dtcList.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Display DTC Items List
        if (dtcList.isEmpty() && !isScanningDtcs) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(40.dp))
                        Text("ไม่พบรหัสข้อผิดพลาดในขณะนี้", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            text = "กดปุ่ม 'เริ่มสแกนอ่านโค้ด DTC' เพื่อส่งคำสั่งอ่านค่าจากกล่อง ECU ผ่าน Bluetooth LE",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(dtcList) { dtc ->
                BleDtcCardItem(
                    dtc = dtc,
                    onAskAi = {
                        val prompt = "ช่วยอธิบายรหัสปัญหา ${dtc.code} (${dtc.thTitle}) และขั้นตอนการตรวจเช็คแก้ไขปัญหาสำหรับรถยนต์ให้ทีครับ"
                        onConsultAi?.invoke(prompt)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Clear DTC Confirmation Dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("ยืนยันการลบรหัสข้อผิดพลาด?", fontWeight = FontWeight.Bold) },
            text = { Text("การส่งคำสั่ง Mode 04 ผ่าน Bluetooth LE จะลบรหัส DTC และสั่งดับไฟ Check Engine คุณต้องการดำเนินการต่อหรือไม่?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearDtcCodes()
                        showClearConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("ลบโค้ดทันที")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }
}

@Composable
private fun ConnectionStatusChip(
    state: BleConnectionState,
    isDemo: Boolean,
    deviceName: String?
) {
    val (label, bg, fg) = when {
        isDemo -> Triple("DEMO MODE", Color(0xFFE0F7FA), Color(0xFF00838F))
        state == BleConnectionState.CONNECTED -> Triple(deviceName ?: "CONNECTED", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        state == BleConnectionState.CONNECTING -> Triple("CONNECTING...", Color(0xFFFFF3E0), Color(0xFFE65100))
        state == BleConnectionState.SCANNING -> Triple("SCANNING...", Color(0xFFE3F2FD), Color(0xFF1565C0))
        else -> Triple("DISCONNECTED", Color(0xFFFFEAEA), Color(0xFFC62828))
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg
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
                    .background(fg)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = fg
            )
        }
    }
}

@Composable
private fun BleDeviceItem(
    device: BluetoothObdDevice,
    onConnect: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onConnect() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Bluetooth, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Column {
                    Text(text = device.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(text = device.address, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Button(
                onClick = onConnect,
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("เชื่อมต่อ", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun BleDtcCardItem(
    dtc: DtcCode,
    onAskAi: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val severityColor = when (dtc.severity) {
        DtcSeverity.CRITICAL -> Color(0xFFE53935)
        DtcSeverity.WARNING -> Color(0xFFFF9800)
        DtcSeverity.INFO -> Color(0xFF2196F3)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dtc_item_ble_${dtc.code}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, severityColor.copy(alpha = 0.5f))
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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = dtc.thTitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = dtc.thDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AnimatedVisibility(visible = expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("สาเหตุที่พบบ่อย:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    dtc.commonCausesTh.forEach { cause ->
                        Text("• $cause", style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("อาการของรถ:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    dtc.symptomsTh.forEach { symptom ->
                        Text("• $symptom", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Toggle Details"
                    )
                }

                OutlinedButton(
                    onClick = onAskAi,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ปรึกษาช่าง AI", fontSize = 11.sp)
                }
            }
        }
    }
}
