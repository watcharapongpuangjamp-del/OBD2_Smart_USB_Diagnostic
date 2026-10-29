package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.data.model.AppOperationMode
import com.example.data.model.ObdConnectionType
import com.example.data.model.ObdProtocol
import com.example.data.model.TerminalLogEntry
import com.example.data.model.UsbSerialDeviceInfo
import com.example.service.DrivingMode
import com.example.ui.MainViewModel
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObdConnectionDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH
    val operationMode by viewModel.operationMode.collectAsState()

    val usbDevices by viewModel.availableUsbDevices.collectAsState()
    val selectedDevice by viewModel.selectedUsbDevice.collectAsState()
    val baudRate by viewModel.selectedBaudRate.collectAsState()
    val selectedProtocol by viewModel.selectedProtocol.collectAsState()
    val isUsbConnected by viewModel.isUsbConnected.collectAsState()
    val isUsbConnecting by viewModel.isUsbConnecting.collectAsState()
    val usbStats by viewModel.usbStats.collectAsState()
    val terminalLogs by viewModel.terminalLogs.collectAsState()
    val drivingMode by viewModel.drivingMode.collectAsState()

    var activeDialogTab by remember { mutableStateOf(if (operationMode == AppOperationMode.REAL_HARDWARE) 0 else 1) }
    var connectionErrorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.94f)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp,
            border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (operationMode == AppOperationMode.REAL_HARDWARE) AutoCyan.copy(alpha = 0.15f) else AutoAmber.copy(alpha = 0.15f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (operationMode == AppOperationMode.REAL_HARDWARE) Icons.Default.Usb else Icons.Default.Build,
                                contentDescription = null,
                                tint = if (operationMode == AppOperationMode.REAL_HARDWARE) AutoCyan else AutoAmber,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isTh) "การเชื่อมต่อ & โหมดการทำงาน" else "Connection & Operation Mode",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (operationMode == AppOperationMode.REAL_HARDWARE) {
                                    if (isTh) "🔌 โหมดฮาร์ดแวร์จริง (Real Hardware OTG)" else "🔌 Real Hardware OTG Mode"
                                } else {
                                    if (isTh) "🧪 โหมดจำลองข้อมูล (CAN Simulator Engine)" else "🧪 Simulated CAN Engine Mode"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (operationMode == AppOperationMode.REAL_HARDWARE) AutoCyan else AutoAmber
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mode Tabs (Real USB vs Simulator vs Terminal)
                TabRow(
                    selectedTabIndex = activeDialogTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    contentColor = AutoCyan,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = activeDialogTab == 0,
                        onClick = {
                            activeDialogTab = 0
                            viewModel.setOperationMode(AppOperationMode.REAL_HARDWARE)
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Usb, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isTh) "ฮาร์ดแวร์ USB จริง" else "Real USB Hardware", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    )
                    Tab(
                        selected = activeDialogTab == 1,
                        onClick = {
                            activeDialogTab = 1
                            viewModel.setOperationMode(AppOperationMode.SIMULATOR)
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isTh) "โหมดจำลอง (Demo)" else "Simulator Demo", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    )
                    Tab(
                        selected = activeDialogTab == 2,
                        onClick = {
                            activeDialogTab = 2
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Terminal, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isTh) "เทอร์มินัล AT" else "AT Terminal", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Error Message banner if any
                if (connectionErrorMessage != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AutoRed.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, AutoRed)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = AutoRed, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = connectionErrorMessage ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { connectionErrorMessage = null },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Dismiss", tint = AutoRed, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                when (activeDialogTab) {
                    0 -> {
                        // TAB 0: REAL HARDWARE USB OTG
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Status Banner
                            item {
                                UsbStatusBannerCard(
                                    isTh = isTh,
                                    isConnected = isUsbConnected,
                                    isConnecting = isUsbConnecting,
                                    stats = usbStats,
                                    selectedDevice = selectedDevice
                                )
                            }

                            // Detected USB Devices List
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(imageVector = Icons.Default.Usb, contentDescription = null, tint = AutoCyan, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = if (isTh) "อุปกรณ์ OBD2 USB ที่เสียบอยู่จริง:" else "Physical USB OBD2 Devices:",
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            OutlinedButton(
                                                onClick = {
                                                    connectionErrorMessage = null
                                                    viewModel.refreshUsbDevices()
                                                },
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(if (isTh) "สแกนพอร์ตใหม่" else "Rescan USB", fontSize = 11.sp)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        if (usbDevices.isEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(AutoAmber.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                                                    .border(BorderStroke(1.dp, AutoAmber.copy(alpha = 0.3f)), RoundedCornerShape(10.dp))
                                                    .padding(12.dp)
                                            ) {
                                                Column {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = AutoAmber, modifier = Modifier.size(18.dp))
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            text = if (isTh) "ไม่พบอุปกรณ์ OBD2 USB บนโทรศัพท์" else "No USB Device Connected",
                                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                            color = AutoAmber
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = if (isTh) {
                                                            "• เสียบหัวต่อ USB OTG เข้ากับโทรศัพท์\n• ต่อสายสแกนเนอร์ OBD2 USB (FTDI / ELM327 / CP2102) เข้ากับพอร์ต OTG\n• เสียบปลายสาย OBD-II เข้ากับพอร์ตใต้พวงมาลัยรถยนต์\n• บิดกุญแจรถยนต์ไปที่ตำแหน่ง ON แล้วกด 'สแกนพอร์ตใหม่'"
                                                        } else {
                                                            "• Plug USB OTG adapter into phone\n• Attach OBD2 USB scanner (FTDI / ELM327 / CP2102)\n• Plug 16-pin connector into vehicle port and turn ignition ON"
                                                        },
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        } else {
                                            usbDevices.forEach { dev ->
                                                val isSelected = selectedDevice?.deviceId == dev.deviceId
                                                UsbDeviceItemRow(
                                                    device = dev,
                                                    isSelected = isSelected,
                                                    onSelect = { viewModel.selectUsbDevice(dev) }
                                                )
                                                Spacer(modifier = Modifier.height(6.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            // Baud Rate & Protocol Config
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = if (isTh) "การตั้งค่า Baud Rate & Protocol:" else "Baud Rate & Protocol Settings:",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = if (isTh) "Baud Rate (ความเร็วสื่อสาร USB):" else "Baud Rate:",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        val baudOptions = listOf(115200, 38400, 230400, 500000)
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            items(baudOptions) { b ->
                                                FilterChip(
                                                    selected = baudRate == b,
                                                    onClick = { viewModel.setUsbBaudRate(b) },
                                                    label = {
                                                        Text(
                                                            text = when (b) {
                                                                115200 -> "115.2k (แนะนำ)"
                                                                38400 -> "38.4k (ELM327)"
                                                                230400 -> "230.4k"
                                                                500000 -> "500k"
                                                                else -> "$b"
                                                            },
                                                            fontSize = 11.sp,
                                                            fontWeight = if (baudRate == b) FontWeight.Bold else FontWeight.Normal
                                                        )
                                                    },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        selectedContainerColor = AutoCyan,
                                                        selectedLabelColor = Color.Black
                                                    )
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = if (isTh) "โปรโตคอลสื่อสาร (OBD Protocol):" else "Protocol:",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        var protocolExpanded by remember { mutableStateOf(false) }
                                        ExposedDropdownMenuBox(
                                            expanded = protocolExpanded,
                                            onExpandedChange = { protocolExpanded = !protocolExpanded }
                                        ) {
                                            OutlinedTextField(
                                                value = if (isTh) selectedProtocol.displayNameTh else selectedProtocol.displayNameEn,
                                                onValueChange = {},
                                                readOnly = true,
                                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = protocolExpanded) },
                                                modifier = Modifier
                                                    .menuAnchor()
                                                    .fillMaxWidth(),
                                                textStyle = MaterialTheme.typography.bodySmall,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            ExposedDropdownMenu(
                                                expanded = protocolExpanded,
                                                onDismissRequest = { protocolExpanded = false }
                                            ) {
                                                ObdProtocol.values().forEach { proto ->
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                text = if (isTh) proto.displayNameTh else proto.displayNameEn,
                                                                style = MaterialTheme.typography.bodySmall
                                                            )
                                                        },
                                                        onClick = {
                                                            viewModel.setUsbProtocol(proto)
                                                            protocolExpanded = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Connect / Disconnect Action Button
                            item {
                                if (isUsbConnected) {
                                    Button(
                                        onClick = {
                                            viewModel.disconnectUsbObd()
                                            connectionErrorMessage = null
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("btn_disconnect_usb"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AutoRed)
                                    ) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isTh) "ตัดการเชื่อมต่อพอร์ต USB (Disconnect)" else "Disconnect USB OBD-II",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            connectionErrorMessage = null
                                            viewModel.connectUsbObd(
                                                onSuccess = {
                                                    connectionErrorMessage = null
                                                },
                                                onError = { err ->
                                                    connectionErrorMessage = err
                                                }
                                            )
                                        },
                                        enabled = !isUsbConnecting,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("btn_connect_usb"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                                    ) {
                                        if (isUsbConnecting) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(18.dp),
                                                color = Color.Black,
                                                strokeWidth = 2.dp
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (isTh) "กำลังเชื่อมต่อพอร์ต USB..." else "Opening USB Port...",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color.Black
                                            )
                                        } else {
                                            Icon(imageVector = Icons.Default.Usb, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (isTh) "เชื่อมต่อสาย OBD2 USB จริง" else "Connect Real OBD2 USB",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // TAB 1: SIMULATOR MODE
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = AutoAmber.copy(alpha = 0.12f)),
                                    border = BorderStroke(1.dp, AutoAmber.copy(alpha = 0.4f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = AutoAmber, modifier = Modifier.size(22.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (isTh) "โหมดจำลองระบบรถยนต์ (Virtual CAN Simulator)" else "Virtual CAN Simulator Engine",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = AutoAmber
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = if (isTh) {
                                                "โหมดนี้จำลองสัญญาณเซนเซอร์ ECM, TCM, ABS, BCM และสร้างสถานการณ์โค้ดความผิดปกติ เพื่อทดสอบการทำงานของแอปและ AI Mechanic โดยไม่ต้องต่อสายกับรถยนต์จริง"
                                            } else {
                                                "Generates virtual ECM, TCM, ABS, BCM CAN telemetry for testing without physical vehicle."
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            // Simulator Profiles
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = if (isTh) "เลือกสถานการณ์จำลองการขับขี่:" else "Select Driving Profile Simulation:",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        val modes = listOf(
                                            Triple(DrivingMode.IDLE, "รอบเดินเบา (Idle 750 RPM)", "Idle (750 RPM)"),
                                            Triple(DrivingMode.CRUISE, "ขับขี่ปกติ (Cruise 90 km/h)", "Cruise (90 km/h)"),
                                            Triple(DrivingMode.HARD_ACCEL, "เร่งแซงเต็มกำลัง (Hard Accel)", "Full Acceleration"),
                                            Triple(DrivingMode.OVERHEAT_WARNING, "จำลองความร้อนสูง (Overheat)", "Engine Overheat Test")
                                        )

                                        modes.forEach { (m, titleTh, titleEn) ->
                                            val isSelected = drivingMode == m
                                            Surface(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .clickable { viewModel.setDrivingMode(m) },
                                                color = if (isSelected) AutoCyan.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                                border = BorderStroke(1.dp, if (isSelected) AutoCyan else Color.Transparent)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(
                                                            imageVector = when (m) {
                                                                DrivingMode.IDLE -> Icons.Default.DirectionsCar
                                                                DrivingMode.CRUISE -> Icons.Default.Speed
                                                                DrivingMode.HARD_ACCEL -> Icons.Default.PlayArrow
                                                                DrivingMode.OVERHEAT_WARNING -> Icons.Default.Warning
                                                            },
                                                            contentDescription = null,
                                                            tint = if (isSelected) AutoCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(10.dp))
                                                        Text(
                                                            text = if (isTh) titleTh else titleEn,
                                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                            ),
                                                            color = if (isSelected) AutoCyan else MaterialTheme.colorScheme.onSurface
                                                        )
                                                    }
                                                    if (isSelected) {
                                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AutoCyan, modifier = Modifier.size(18.dp))
                                                    }
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                        }
                                    }
                                }
                            }

                            // Fault Injection Simulation
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = if (isTh) "จำลองการสร้างรหัสความผิดปกติ (DTC Fault Injection):" else "DTC Fault Injection:",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            OutlinedButton(
                                                onClick = { viewModel.triggerSimulatedFault("P0171") },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("P0171 บาง", fontSize = 11.sp)
                                            }
                                            OutlinedButton(
                                                onClick = { viewModel.triggerSimulatedFault("P0087") },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("P0087 ราง", fontSize = 11.sp)
                                            }
                                            OutlinedButton(
                                                onClick = { viewModel.clearDtcFaultCodes() },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("ล้างโค้ด", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // TAB 2: AT / CAN TERMINAL
                        UsbTerminalView(
                            isTh = isTh,
                            terminalLogs = terminalLogs,
                            onSendCommand = { cmd ->
                                viewModel.sendTerminalCommand(cmd)
                            },
                            onClearLogs = { viewModel.clearTerminalLogs() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UsbStatusBannerCard(
    isTh: Boolean,
    isConnected: Boolean,
    isConnecting: Boolean,
    stats: com.example.data.model.UsbConnectionStats,
    selectedDevice: UsbSerialDeviceInfo?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isConnected) AutoGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, if (isConnected) AutoGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(if (isConnected) AutoGreen else if (isConnecting) AutoAmber else AutoRed, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isConnected) {
                            if (isTh) "สถานะ: เชื่อมต่อสาย USB OBD2 สำเร็จ" else "Status: USB OBD2 Connected"
                        } else if (isConnecting) {
                            if (isTh) "สถานะ: กำลังเชื่อมต่อฮาร์ดแวร์..." else "Status: Connecting Hardware..."
                        } else {
                            if (isTh) "สถานะ: ยังไม่ได้เชื่อมต่อ (Disconnected)" else "Status: Disconnected"
                        },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isConnected) AutoGreen else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isConnected) {
                            "${stats.activeChipset} • ${stats.baudRate} bps • ${stats.totalPacketsReceived} pkts"
                        } else {
                            if (selectedDevice != null) "ตรวจพบ: ${selectedDevice.chipsetName}" else if (isTh) "ไม่พบอุปกรณ์ที่เชื่อมต่อ" else "No physical device attached"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun UsbDeviceItemRow(
    device: UsbSerialDeviceInfo,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() },
        color = if (isSelected) AutoCyan.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, if (isSelected) AutoCyan else Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Default.Usb,
                    contentDescription = null,
                    tint = if (isSelected) AutoCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = device.chipsetName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isSelected) AutoCyan else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${device.productName} • ${device.deviceName} (VID:${Integer.toHexString(device.vendorId)})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isSelected) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AutoCyan, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun UsbTerminalView(
    isTh: Boolean,
    terminalLogs: List<TerminalLogEntry>,
    onSendCommand: (String) -> Unit,
    onClearLogs: () -> Unit
) {
    var commandInput by remember { mutableStateOf("") }
    val quickPresets = listOf("ATZ", "ATE0", "ATRV", "0100", "010C", "010D", "0105", "03", "04")

    Column(modifier = Modifier.fillMaxSize()) {
        // Quick preset chips
        Text(
            text = if (isTh) "คำสั่งด่วน (Quick OBD-II Commands):" else "Quick Commands:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(quickPresets) { cmd ->
                FilterChip(
                    selected = false,
                    onClick = {
                        commandInput = cmd
                        onSendCommand(cmd)
                    },
                    label = { Text(cmd, fontSize = 10.sp, fontFamily = FontFamily.Monospace) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Terminal Log Output Window
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117)),
            border = BorderStroke(1.dp, Color(0xFF30363D))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CAN/OBD-II Console Log (${terminalLogs.size} lines)",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                        color = Color(0xFF8B949E)
                    )
                    IconButton(
                        onClick = onClearLogs,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = "Clear", tint = Color(0xFF8B949E), modifier = Modifier.size(16.dp))
                    }
                }

                HorizontalDivider(color = Color(0xFF21262D), modifier = Modifier.padding(vertical = 4.dp))

                if (terminalLogs.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isTh) "ไม่มีประวัติการส่งคำสั่ง (พิมพ์คำสั่ง AT/OBD ด้านล่าง)" else "No terminal logs. Send AT/OBD command below.",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                            color = Color(0xFF484F58)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        reverseLayout = true
                    ) {
                        items(terminalLogs.reversed()) { log ->
                            TerminalLogRow(log = log, isTh = isTh)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Command Input Field
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_terminal_command"),
                placeholder = {
                    Text(
                        "พิมพ์คำสั่ง เช่น ATZ, ATRV, 010C",
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = AutoCyan),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (commandInput.isNotBlank()) {
                        onSendCommand(commandInput)
                        commandInput = ""
                    }
                })
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (commandInput.isNotBlank()) {
                        onSendCommand(commandInput)
                        commandInput = ""
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.Black, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun TerminalLogRow(log: TerminalLogEntry, isTh: Boolean) {
    val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)
    val timeStr = timeFormat.format(Date(log.timestamp))

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "[$timeStr] TX > ${log.command}",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold),
                color = AutoCyan
            )
            if (log.latencyMs > 0) {
                Text(
                    text = "${log.latencyMs} ms",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = Color(0xFF8B949E)
                )
            }
        }
        if (log.rawResponse.isNotBlank()) {
            Text(
                text = "RX < ${log.rawResponse}",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                color = if (log.isSuccess) AutoGreen else AutoRed
            )
        }
        val meaning = if (isTh) log.parsedMeaningTh else log.parsedMeaningEn
        if (meaning.isNotBlank()) {
            Text(
                text = "ℹ $meaning",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color(0xFFA5D6FF)
            )
        }
    }
}
