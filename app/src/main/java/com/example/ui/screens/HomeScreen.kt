package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.UsbOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.DiagnosticSystem
import com.example.data.model.FeatureCategory
import com.example.data.model.ShortcutFeature
import com.example.data.model.ShortcutFeatureRegistry
import com.example.data.model.Vehicle
import com.example.ui.MainViewModel
import com.example.ui.components.AddVehicleDialog
import com.example.ui.components.MiniArcGauge
import com.example.ui.components.ObdConnectionDialog
import com.example.ui.components.SpeedometerGauge
import com.example.ui.components.TachometerGauge
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isThai by viewModel.language.collectAsState()
    val isTh = isThai == AppLanguage.TH
    val selectedVehicle by viewModel.selectedVehicle.collectAsState()
    val allVehicles by viewModel.allVehicles.collectAsState()
    val liveSensors by viewModel.liveSensors.collectAsState()
    val activeDtcs by viewModel.activeDtcs.collectAsState()
    val predictiveHealth by viewModel.predictiveHealth.collectAsState()
    val isCloudSyncing by viewModel.isCloudSyncing.collectAsState()
    val cloudSyncStatus by viewModel.cloudSyncStatus.collectAsState()
    val recentNotifications by viewModel.recentNotifications.collectAsState()
    val favoriteFeatureIds by viewModel.favoriteFeatureIds.collectAsState()

    var showAddVehicleDialog by remember { mutableStateOf(false) }
    var showConnectionDialog by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("all") }

    val operationMode by viewModel.operationMode.collectAsState()
    val isUsbConnected by viewModel.isUsbConnected.collectAsState()
    val isUsbConnecting by viewModel.isUsbConnecting.collectAsState()
    val selectedUsbDevice by viewModel.selectedUsbDevice.collectAsState()
    val baudRate by viewModel.selectedBaudRate.collectAsState()

    val favoriteFeatures = remember(favoriteFeatureIds) {
        ShortcutFeatureRegistry.ALL_FEATURES.filter { favoriteFeatureIds.contains(it.id) }
    }

    if (showAddVehicleDialog) {
        AddVehicleDialog(
            isThai = isTh,
            onAdd = { make, model, yr, eng, plate, prov, vin, mil, fuel ->
                viewModel.addNewVehicle(make, model, yr, eng, plate, prov, vin, mil, fuel)
            },
            onDismiss = { showAddVehicleDialog = false }
        )
    }

    if (showConnectionDialog) {
        ObdConnectionDialog(
            viewModel = viewModel,
            onDismiss = { showConnectionDialog = false }
        )
    }

    val launchFeature: (ShortcutFeature) -> Unit = { feature ->
        when (feature.id) {
            "hud_display" -> viewModel.setShowHudDialog(true)
            "oem_baseline" -> viewModel.setShowBaselineDialog(true)
            "active_test", "special_functions" -> viewModel.setTab(1)
            "usb_terminal" -> showConnectionDialog = true
            else -> viewModel.setTab(feature.targetTab)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Active Vehicle Information & Health Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(AutoCyan.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = AutoCyan,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = selectedVehicle?.let { "${it.make} ${it.model}" } ?: "Toyota Hilux Revo",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = selectedVehicle?.let { "${it.licensePlate} ${it.provinceTh}" } ?: "8กบ 9421 กรุงเทพฯ",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = AutoCyan
                                )
                            }
                        }

                        // Health Score Badge
                        val score = predictiveHealth.overallHealthScore
                        val scoreColor = if (score > 85) AutoGreen else if (score > 65) AutoAmber else AutoRed
                        Box(
                            modifier = Modifier
                                .background(scoreColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .clickable { viewModel.setTab(2) }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "$score%",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = scoreColor
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isTh) "สุขภาพรถ" else "Health",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Vehicle Telemetry Info (VIN, Mileage, Engine)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isTh) "เลขไมล์สะสม" else "Mileage",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${selectedVehicle?.currentMileageKm ?: 68500} km",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isTh) "เครื่องยนต์" else "Engine",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = selectedVehicle?.engine ?: "2.4 GD Turbo",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isTh) "หมายเลขตัวถัง (VIN)" else "VIN Code",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = (selectedVehicle?.vin ?: "MR0BA3CD2M").take(10) + "...",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = AutoCyan
                            )
                        }
                    }

                    // Multi-vehicle switch row
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(allVehicles) { v ->
                            val isSel = v.id == selectedVehicle?.id
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSel) AutoCyan.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.selectVehicle(v) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${v.make} ${v.licensePlate}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isSel) AutoCyan else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        item {
                            IconButton(
                                onClick = { showAddVehicleDialog = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Car", tint = AutoCyan)
                            }
                        }
                    }
                }
            }
        }

        // OBD2 USB Hardware Connection & Port Management Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUsbConnected) {
                        AutoGreen.copy(alpha = 0.08f)
                    } else if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) {
                        AutoAmber.copy(alpha = 0.08f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                border = BorderStroke(
                    1.dp,
                    if (isUsbConnected) AutoGreen.copy(alpha = 0.5f) else if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) AutoAmber.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (isUsbConnected) AutoGreen.copy(alpha = 0.2f) else if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) AutoAmber.copy(alpha = 0.2f) else AutoCyan.copy(alpha = 0.15f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isUsbConnected) Icons.Default.Usb else if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) Icons.Default.Build else Icons.Default.UsbOff,
                                    contentDescription = "USB OBD2",
                                    tint = if (isUsbConnected) AutoGreen else if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) AutoAmber else AutoCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) {
                                            if (isTh) "โหมดจำลองระบบรถยนต์ (Simulator Demo)" else "Virtual CAN Simulator Demo"
                                        } else {
                                            if (isTh) "การเชื่อมต่อ OBD2 USB ฮาร์ดแวร์จริง" else "Real Hardware OBD2 USB Link"
                                        },
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) {
                                        if (isTh) "กำลังจำลองสัญญาณเซนเซอร์ CAN Engine สำหรับทดสอบ" else "Simulating CAN Bus Engine Telemetry"
                                    } else if (isUsbConnected) {
                                        "${selectedUsbDevice?.chipsetName ?: "FTDI / OBDLink"} • ${baudRate} bps • ตรวจพบ ECU"
                                    } else {
                                        if (isTh) "ยังไม่ได้เชื่อมต่อสาย USB OTG (กด 'เชื่อมต่อ USB')" else "USB OTG not connected (Tap 'Connect USB')"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isUsbConnected) AutoGreen else if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) AutoAmber else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(onClick = { showConnectionDialog = true }) {
                            Icon(imageVector = Icons.Default.Terminal, contentDescription = "Terminal", tint = AutoCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (operationMode == com.example.data.model.AppOperationMode.SIMULATOR) {
                            Button(
                                onClick = { showConnectionDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AutoAmber)
                            ) {
                                Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isTh) "ปรับแต่งตัวจำลอง" else "Simulator Settings", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else if (isUsbConnected) {
                            OutlinedButton(
                                onClick = { viewModel.disconnectUsbObd() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, AutoRed.copy(alpha = 0.5f))
                            ) {
                                Text(if (isTh) "ตัดสาย USB" else "Disconnect", color = AutoRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = { viewModel.connectUsbObd() },
                                enabled = !isUsbConnecting,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                            ) {
                                if (isUsbConnecting) {
                                    CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.Black, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isTh) "กำลังเชื่อมต่อ..." else "Connecting...", color = Color.Black, fontSize = 12.sp)
                                } else {
                                    Icon(imageVector = Icons.Default.Usb, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isTh) "เชื่อมต่อ USB OTG" else "Connect USB", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = { showConnectionDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (isTh) "สลับโหมด / ตั้งค่า" else "Modes & Settings", fontSize = 12.sp, color = AutoCyan)
                        }
                    }
                }
            }
        }

        // ==================== SECTION 1: FAVORITES (รายการโปรด) ====================
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AutoAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTh) "⭐ รายการโปรดของคุณ (${favoriteFeatures.size})" else "⭐ My Favorites (${favoriteFeatures.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = if (isTh) "กดติดดาวเพิ่มได้" else "Tap star to add",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (favoriteFeatures.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isTh) "ยังไม่มีรายการโปรด กดดาว ⭐ ที่ฟังก์ชันด้านล่างเพื่อเพิ่มเป็นทางลัดโปรด" else "No favorites added yet. Tap star on any feature below to pin here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 2.dp)
                    ) {
                        items(favoriteFeatures) { feat ->
                            FavoriteCardItem(
                                feature = feat,
                                isTh = isTh,
                                onRemove = { viewModel.toggleFavoriteFeature(feat.id) },
                                onClick = { launchFeature(feat) }
                            )
                        }
                    }
                }
            }
        }

        // ==================== SECTION 2: QUICK ACTION SHORTCUTS (ทางลัดด่วน) ====================
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (isTh) "⚡ ทางลัดระบบด่วน (Quick Action Shortcuts)" else "⚡ Quick Action Shortcuts",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        ShortcutChip(
                            title = if (isTh) "🔍 สแกน DTC" else "🔍 Scan DTC",
                            icon = Icons.Default.QrCodeScanner,
                            accentColor = AutoCyan,
                            onClick = { viewModel.setTab(1) }
                        )
                    }
                    item {
                        ShortcutChip(
                            title = if (isTh) "🚀 เคลียร์โค้ด" else "🚀 Clear Codes",
                            icon = Icons.Default.Build,
                            accentColor = AutoRed,
                            onClick = { viewModel.setTab(1) }
                        )
                    }
                    item {
                        ShortcutChip(
                            title = if (isTh) "📊 เซนเซอร์สด" else "📊 Live Gauges",
                            icon = Icons.Default.Speed,
                            accentColor = AutoGreen,
                            onClick = { viewModel.setTab(3) }
                        )
                    }
                    item {
                        ShortcutChip(
                            title = if (isTh) "🤖 ถาม AI ช่าง" else "🤖 Ask AI",
                            icon = Icons.Default.AutoAwesome,
                            accentColor = AutoAmber,
                            onClick = { viewModel.setTab(4) }
                        )
                    }
                    item {
                        ShortcutChip(
                            title = if (isTh) "🚗 สเปกรถไทย" else "🚗 Thai Specs",
                            icon = Icons.Default.DirectionsCar,
                            accentColor = Color(0xFFB388FF),
                            onClick = { viewModel.setTab(5) }
                        )
                    }
                    item {
                        ShortcutChip(
                            title = if (isTh) "⚡ โหมด HUD" else "⚡ HUD Mode",
                            icon = Icons.Default.Speed,
                            accentColor = AutoCyan,
                            onClick = { viewModel.setShowHudDialog(true) }
                        )
                    }
                }
            }
        }

        // ==================== SECTION 3: CATEGORIZED MODULE HUBS (แยกหมวดหลัก หมวดย่อย) ====================
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (isTh) "🗂️ ศูนย์รวมฟังก์ชันแยกตามหมวดหมู่" else "🗂️ Categorized Function Hubs",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Category Filter Chips
                val categoryFilterOptions = listOf(
                    "all" to if (isTh) "ทั้งหมด (${ShortcutFeatureRegistry.ALL_FEATURES.size})" else "All (${ShortcutFeatureRegistry.ALL_FEATURES.size})",
                    "diag" to if (isTh) "🚗 สแกนวิเคราะห์" else "🚗 Diagnostic",
                    "telemetry" to if (isTh) "⚡ เซนเซอร์สด" else "⚡ Telemetry",
                    "maintenance" to if (isTh) "🛠️ บำรุงรักษา & สเปก" else "🛠️ Maintenance",
                    "ai" to if (isTh) "🤖 AI ช่างเครื่อง" else "🤖 AI Mechanic"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(categoryFilterOptions) { (id, label) ->
                        val isSelected = selectedCategoryFilter == id
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) AutoCyan else MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCategoryFilter = id }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Render Filtered Category Sections with Subcategories
        val filteredCategories = if (selectedCategoryFilter == "all") {
            ShortcutFeatureRegistry.CATEGORIES
        } else {
            ShortcutFeatureRegistry.CATEGORIES.filter { it.id == selectedCategoryFilter }
        }

        items(filteredCategories) { category ->
            CategoryDimensionalHubCard(
                category = category,
                isTh = isTh,
                favoriteIds = favoriteFeatureIds,
                onToggleFavorite = { viewModel.toggleFavoriteFeature(it) },
                onLaunchFeature = launchFeature
            )
        }

        // 4-System Diagnostic Status Matrix (Engine, Powertrain, Chassis, Electrical)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isTh) "สถานะการวินิจฉัย 4 ระบบหลัก" else "4-System Diagnostic Status",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isTh) "ตรวจเช็คแยกโมดูล" else "Open Scanner",
                            style = MaterialTheme.typography.labelSmall,
                            color = AutoCyan,
                            modifier = Modifier.clickable { viewModel.setTab(1) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SystemStatusChip(
                            title = if (isTh) "เครื่องยนต์" else "Engine",
                            code = "ECM",
                            isFault = activeDtcs.any { it.code.startsWith("P01") || it.code.startsWith("P00") || it.code.startsWith("P02") || it.code.startsWith("P03") },
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.selectDiagnosticSystem(DiagnosticSystem.ENGINE)
                                viewModel.setTab(1)
                            }
                        )
                        SystemStatusChip(
                            title = if (isTh) "ระบบส่งกำลัง" else "Powertrain",
                            code = "TCM",
                            isFault = activeDtcs.any { it.code.startsWith("P07") || it.code.startsWith("P08") },
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.selectDiagnosticSystem(DiagnosticSystem.POWERTRAIN)
                                viewModel.setTab(1)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SystemStatusChip(
                            title = if (isTh) "เบรก/แชสซีส์" else "Chassis",
                            code = "ABS",
                            isFault = activeDtcs.any { it.code.startsWith("C0") },
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.selectDiagnosticSystem(DiagnosticSystem.CHASSIS)
                                viewModel.setTab(1)
                            }
                        )
                        SystemStatusChip(
                            title = if (isTh) "ระบบไฟฟ้า" else "Electrical",
                            code = "BCM",
                            isFault = activeDtcs.any { it.code.startsWith("B") || it.code.startsWith("U") },
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.selectDiagnosticSystem(DiagnosticSystem.ELECTRICAL)
                                viewModel.setTab(1)
                            }
                        )
                    }
                }
            }
        }

        // Live Telemetry Mini Gauge Hub
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isTh) "มาตรวัดสด (Live Telemetry)" else "Live Telemetry Dashboard",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = if (isTh) "HUD สะท้อนกระจก" else "HUD Mode",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AutoCyan,
                                modifier = Modifier
                                    .background(AutoCyan.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .clickable { viewModel.setShowHudDialog(true) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Text(
                                text = if (isTh) "เทียบค่า OEM" else "OEM Baseline",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AutoGreen,
                                modifier = Modifier
                                    .background(AutoGreen.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .clickable { viewModel.setShowBaselineDialog(true) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SpeedometerGauge(speedKmh = liveSensors.speedKmh)
                        TachometerGauge(rpm = liveSensors.rpm)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mini Arcs for Coolant, Battery, Boost
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MiniArcGauge(
                            title = if (isTh) "ความร้อนน้ำ" else "Coolant",
                            valueStr = "${liveSensors.coolantTempC}",
                            unit = "°C",
                            progress = liveSensors.coolantTempC / 120f,
                            barColor = if (liveSensors.coolantTempC > 100) AutoRed else AutoCyan,
                            modifier = Modifier.weight(1f)
                        )
                        MiniArcGauge(
                            title = if (isTh) "แบตเตอรี่" else "Battery",
                            valueStr = String.format("%.1f", liveSensors.batteryVoltage),
                            unit = "V",
                            progress = (liveSensors.batteryVoltage - 11f) / 4f,
                            barColor = AutoGreen,
                            modifier = Modifier.weight(1f)
                        )
                        MiniArcGauge(
                            title = if (isTh) "บูสต์เทอร์โบ" else "Boost",
                            valueStr = String.format("%.2f", liveSensors.boostPressureBar),
                            unit = "Bar",
                            progress = liveSensors.boostPressureBar / 2.0f,
                            barColor = AutoAmber,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Upcoming Scheduled Service Reminders
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = AutoAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTh) "กำหนดการเช็คระยะที่กำลังมาถึง" else "Upcoming Service Reminders",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = if (isTh) "ดูบันทึกซ่อม" else "Service Log",
                            style = MaterialTheme.typography.labelSmall,
                            color = AutoCyan,
                            modifier = Modifier.clickable { viewModel.setTab(5) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ServiceReminderItem(
                        title = if (isTh) "น้ำมันเครื่องสังเคราะห์ 5W-30 + กรอง" else "Engine Oil 5W-30 & Filter",
                        dueMileage = "70,000 km",
                        remainingKm = if (isTh) "อีก 1,500 กม. (ใกล้ถึงกำหนด)" else "In 1,500 km (Soon)",
                        isUrgent = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ServiceReminderItem(
                        title = if (isTh) "ไส้กรองน้ำมันโซล่า (Fuel Filter)" else "Diesel Fuel Filter",
                        dueMileage = "70,000 km",
                        remainingKm = if (isTh) "อีก 1,500 กม." else "In 1,500 km",
                        isUrgent = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ServiceReminderItem(
                        title = if (isTh) "ผ้าเบรกหน้า & น้ำมันเบรก DOT4" else "Front Brake Pads & DOT4 Fluid",
                        dueMileage = "80,000 km",
                        remainingKm = if (isTh) "อีก 11,500 กม." else "In 11,500 km",
                        isUrgent = false
                    )
                }
            }
        }

        // Push Notification System Center
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, AutoAmber.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = AutoAmber,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTh) "ระบบการแจ้งเตือนพุช (Push Notifications)" else "Push Notification System",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Text(
                        text = if (isTh) {
                            "ส่งการแจ้งเตือนเข้าอุปกรณ์ของคุณเมื่อถึงกำหนดเช็คระยะ, เกิดความผิดปกติวิกฤต, หรือผลสแกนวินิจฉัยใหม่"
                        } else {
                            "Alerts for scheduled maintenance, critical vehicle alerts, and diagnostic findings."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.triggerMaintenanceNotification() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = if (isTh) "🔔 เช็คระยะ" else "🔔 Service", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { viewModel.triggerCriticalAlertNotification() },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AutoRed),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = if (isTh) "🚨 วิกฤต" else "🚨 Critical", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { viewModel.triggerDiagnosticFindingsNotification() },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AutoCyan),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = if (isTh) "🔍 ผลสแกน" else "🔍 Scan Alert", fontSize = 11.sp)
                        }
                    }

                    if (recentNotifications.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isTh) "ประวัติการแจ้งเตือนล่าสุด:" else "Recent Alerts Sent:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        recentNotifications.take(2).forEach { item ->
                            Text(
                                text = item.second,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                modifier = Modifier.padding(top = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Secure Cloud Database Vault Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, AutoGreen.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(AutoGreen.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCloudSyncing) Icons.Default.CloudSync else Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = AutoGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isTh) "ฐานข้อมูลคลาวด์ปลอดภัย (Cloud Vault)" else "Secure Cloud Database",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = cloudSyncStatus,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = AutoGreen
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.syncDatabaseWithCloud() },
                        enabled = !isCloudSyncing,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AutoGreen)
                    ) {
                        if (isCloudSyncing) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text(text = if (isTh) "ซิงค์คลาวด์" else "Sync", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun FavoriteCardItem(
    feature: ShortcutFeature,
    isTh: Boolean,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, AutoAmber.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(AutoAmber.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getFeatureIcon(feature.iconName),
                        contentDescription = null,
                        tint = AutoAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Unstar",
                        tint = AutoAmber,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            feature.badgeTextTh?.let { badge ->
                Box(
                    modifier = Modifier
                        .background(AutoCyan.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isTh) badge else (feature.badgeTextEn ?: badge),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = AutoCyan
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = if (isTh) feature.titleTh else feature.titleEn,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Text(
                text = if (isTh) feature.descriptionTh else feature.descriptionEn,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun ShortcutChip(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CategoryDimensionalHubCard(
    category: FeatureCategory,
    isTh: Boolean,
    favoriteIds: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onLaunchFeature: (ShortcutFeature) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Category Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(AutoCyan.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getFeatureIcon(category.iconName),
                            contentDescription = null,
                            tint = AutoCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isTh) category.titleTh else category.titleEn,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isTh) category.descriptionTh else category.descriptionEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Subcategories and Features
            category.subcategories.forEach { sub ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isTh) sub.titleTh else sub.titleEn,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AutoCyan
                        )
                    }

                    sub.features.forEach { feat ->
                        val isFav = favoriteIds.contains(feat.id)
                        DimensionalFeatureRowItem(
                            feature = feat,
                            isTh = isTh,
                            isFavorite = isFav,
                            onToggleFavorite = { onToggleFavorite(feat.id) },
                            onClick = { onLaunchFeature(feat) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DimensionalFeatureRowItem(
    feature: ShortcutFeature,
    isTh: Boolean,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(AutoCyan.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getFeatureIcon(feature.iconName),
                    contentDescription = null,
                    tint = AutoCyan,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isTh) feature.titleTh else feature.titleEn,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    feature.badgeTextTh?.let { badge ->
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(AutoAmber.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = if (isTh) badge else (feature.badgeTextEn ?: badge),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                color = AutoAmber
                            )
                        }
                    }
                }
                Text(
                    text = if (isTh) feature.descriptionTh else feature.descriptionEn,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) AutoAmber else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun getFeatureIcon(iconName: String): ImageVector {
    return when (iconName) {
        "qr_code_scanner" -> Icons.Default.QrCodeScanner
        "speed" -> Icons.Default.Speed
        "psychology" -> Icons.Default.Psychology
        "build" -> Icons.Default.Build
        "directions_car" -> Icons.Default.DirectionsCar
        "history" -> Icons.Default.History
        "auto_awesome" -> Icons.Default.AutoAwesome
        "terminal" -> Icons.Default.Terminal
        "hourglass_top" -> Icons.Default.HourglassTop
        "electrical_services" -> Icons.Default.ElectricalServices
        "security" -> Icons.Default.Security
        else -> Icons.Default.Build
    }
}

@Composable
private fun SystemStatusChip(
    title: String,
    code: String,
    isFault: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val statusColor = if (isFault) AutoRed else AutoGreen
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFault) AutoRed.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Module: $code",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(statusColor, CircleShape)
            )
        }
    }
}

@Composable
private fun ServiceReminderItem(
    title: String,
    dueMileage: String,
    remainingKm: String,
    isUrgent: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isUrgent) AutoAmber.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "รอบ $dueMileage",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = remainingKm,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (isUrgent) AutoAmber else AutoGreen
            )
        )
    }
}
