package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.MaintenanceLog
import com.example.ui.MainViewModel
import com.example.ui.components.AddMaintenanceDialog
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MaintenanceScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isThai by viewModel.language.collectAsState()
    val isTh = isThai == AppLanguage.TH
    val selectedVehicle by viewModel.selectedVehicle.collectAsState()
    val maintenanceLogs by viewModel.maintenanceLogs.collectAsState()

    var activeSubTab by remember { mutableStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showSpecialFunctionsDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddMaintenanceDialog(
            currentVehicleMileage = selectedVehicle?.currentMileageKm ?: 68500,
            isThai = isTh,
            onAdd = { typeTh, typeEn, mil, nextMil, cost, garage, notes ->
                viewModel.addMaintenanceRecord(typeTh, typeEn, mil, nextMil, cost, garage, notes)
            },
            onDismiss = { showAddDialog = false }
        )
    }

    if (showSpecialFunctionsDialog) {
        com.example.ui.components.SpecialFunctionsDialog(
            viewModel = viewModel,
            onDismiss = { showSpecialFunctionsDialog = false }
        )
    }

    val currentKm = selectedVehicle?.currentMileageKm ?: 68500
    val totalSpentThb = maintenanceLogs.sumOf { it.costThb }
    val currencyFormat = DecimalFormat("#,##0")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            if (activeSubTab == 0) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = AutoCyan,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Service Log")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Sub Tabs
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = activeSubTab == 0,
                    onClick = { activeSubTab = 0 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text(
                                text = if (isTh) "สมุดบันทึกซ่อม" else "Service Logs",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (activeSubTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
                Tab(
                    selected = activeSubTab == 1,
                    onClick = { activeSubTab = 1 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text(
                                text = if (isTh) "สเปกรถ & VIN" else "Car Specs & VIN",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (activeSubTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
                Tab(
                    selected = activeSubTab == 2,
                    onClick = { activeSubTab = 2 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text(
                                text = if (isTh) "รหัสอะไหล่แท้ OEM" else "OEM Parts",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (activeSubTab == 2) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
            }

            Crossfade(targetState = activeSubTab) { subTab ->
                when (subTab) {
                    1 -> VehicleSpecsScreen(viewModel = viewModel)
                    2 -> OemPartsCatalogScreen(viewModel = viewModel)
                    else -> LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(4.dp)) }

                        // Overview Summary Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                border = BorderStroke(1.dp, AutoGreen.copy(alpha = 0.4f))
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
                                        Column {
                                            Text(
                                                text = if (isTh) "สมุดบันทึกประวัติการบำรุงรักษา" else "Vehicle Maintenance Logbook",
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = selectedVehicle?.let { "${it.make} ${it.model} • ${it.licensePlate}" } ?: "Toyota Hilux Revo",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = AutoGreen
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Button(
                                                onClick = { showSpecialFunctionsDialog = true },
                                                shape = RoundedCornerShape(10.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                                            ) {
                                                Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(text = if (isTh) "ลงทะเบียนอะไหล่" else "Register Part", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }

                                            OutlinedButton(
                                                onClick = { viewModel.triggerServiceNotificationReminder() },
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = AutoAmber, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(text = if (isTh) "เตือนเช็คระยะ" else "Remind", fontSize = 11.sp)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = if (isTh) "เลขไมล์ล่าสุด" else "Current Odo", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text(
                                                text = "$currentKm km",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                                                color = AutoCyan
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = if (isTh) "บันทึกแล้ว" else "Entries", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text(
                                                text = "${maintenanceLogs.size} ครั้ง",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = if (isTh) "ค่าใช้จ่ายรวม" else "Total Cost", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text(
                                                text = "฿${currencyFormat.format(totalSpentThb)}",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                                                color = AutoGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Next Service Countdown Cards
                        item {
                            Text(
                                text = if (isTh) "กำหนดการเช็คระยะครั้งถัดไป (Upcoming Services)" else "Upcoming Service Intervals",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        item {
                            ServiceIntervalItem(
                                titleTh = "เปลี่ยนถ่ายน้ำมันเครื่อง & ไส้กรอง",
                                titleEn = "Engine Oil & Filter Service",
                                dueKm = 70000,
                                currentKm = currentKm,
                                isThai = isTh
                            )
                        }

                        item {
                            ServiceIntervalItem(
                                titleTh = "เปลี่ยนน้ำมันเกียร์ออโต้ (AT/CVT Fluid)",
                                titleEn = "Automatic Transmission Fluid Flush",
                                dueKm = 80000,
                                currentKm = currentKm,
                                isThai = isTh
                            )
                        }

                        item {
                            ServiceIntervalItem(
                                titleTh = "เปลี่ยนน้ำยาหล่อเย็นหม้อน้ำ & น้ำมันเบรก",
                                titleEn = "Coolant & Brake Fluid Refresh",
                                dueKm = 100000,
                                currentKm = currentKm,
                                isThai = isTh
                            )
                        }

                        // Timeline History
                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isTh) "ประวัติการเข้าศูนย์ / อู่ซ่อม (Service Timeline)" else "Maintenance History Timeline",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (maintenanceLogs.isEmpty()) {
                            item {
                                Text(
                                    text = if (isTh) "ยังไม่มีประวัติการเช็คระยะ แตะปุ่ม + เพื่อเพิ่มบันทึกใหม่" else "No service records logged yet. Tap + to add.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            items(maintenanceLogs) { log ->
                                MaintenanceTimelineCard(log = log, isThai = isTh)
                            }
                        }

                        item { Spacer(modifier = Modifier.height(72.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceIntervalItem(
    titleTh: String,
    titleEn: String,
    dueKm: Int,
    currentKm: Int,
    isThai: Boolean
) {
    val remainingKm = dueKm - currentKm
    val isOverdue = remainingKm <= 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (isOverdue) AutoRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(if (isOverdue) AutoRed.copy(alpha = 0.2f) else AutoCyan.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = if (isOverdue) AutoRed else AutoCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isThai) titleTh else titleEn,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isThai) "กำหนดที่ $dueKm กม." else "Due at $dueKm km",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(
                        if (isOverdue) AutoRed.copy(alpha = 0.2f) else AutoGreen.copy(alpha = 0.2f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isOverdue) {
                        if (isThai) "ถึงกำหนดแล้ว!" else "Due Now!"
                    } else {
                        if (isThai) "อีก $remainingKm กม." else "in $remainingKm km"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isOverdue) AutoRed else AutoGreen
                    )
                )
            }
        }
    }
}

@Composable
private fun MaintenanceTimelineCard(
    log: MaintenanceLog,
    isThai: Boolean
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateStr = dateFormat.format(Date(log.serviceDate))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                Text(
                    text = if (isThai) log.serviceTypeTh else log.serviceTypeEn,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "฿${DecimalFormat("#,##0").format(log.costThb)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = AutoGreen
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📍 ${log.garageNameTh}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "📅 $dateStr • ${log.mileageAtServiceKm} km",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = AutoCyan
                )
            }

            if (log.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📝 ${log.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
