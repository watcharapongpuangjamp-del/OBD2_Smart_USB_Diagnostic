package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.ui.MainViewModel
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed

@Composable
fun SettingsDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH

    val isDriveConnected by viewModel.googleDriveConnected.collectAsState()
    val driveEmail by viewModel.googleDriveAccountEmail.collectAsState()
    val isDriveSyncing by viewModel.isGoogleDriveSyncing.collectAsState()
    val lastBackupTime by viewModel.lastCloudBackupTime.collectAsState()

    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccessMessage by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(AutoCyan.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = AutoCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isTh) "การตั้งค่า & Google Drive" else "Settings & Google Drive",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isTh) "จัดการภาษา หน่วยวัด และสำรองข้อมูลบนคลาวด์" else "Manage app preferences & cloud sync",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status Message Banner
                if (statusMessage != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSuccessMessage) AutoGreen.copy(alpha = 0.15f) else AutoRed.copy(alpha = 0.15f)
                        ),
                        border = BorderStroke(1.dp, if (isSuccessMessage) AutoGreen else AutoRed)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSuccessMessage) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isSuccessMessage) AutoGreen else AutoRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = statusMessage ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { statusMessage = null }, modifier = Modifier.size(20.dp)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }

                // Settings Content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // SECTION 1: LANGUAGE & GENERAL
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = AutoCyan, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isTh) "ภาษาการแสดงผล (Display Language)" else "Display Language",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = { if (!isTh) viewModel.toggleLanguage() },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isTh) AutoCyan else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Text(
                                            text = "ภาษาไทย (TH)",
                                            color = if (isTh) Color.Black else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Button(
                                        onClick = { if (isTh) viewModel.toggleLanguage() },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (!isTh) AutoCyan else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Text(
                                            text = "English (EN)",
                                            color = if (!isTh) Color.Black else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 1.5: THEME MODE
                    item {
                        val themeMode by viewModel.themeMode.collectAsState()
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = AutoAmber, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isTh) "ธีมแอปพลิเคชัน (Theme Mode)" else "Theme Mode",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    com.example.data.model.AppThemeMode.values().forEach { mode ->
                                        val isSelected = themeMode == mode
                                        val title = when(mode) {
                                            com.example.data.model.AppThemeMode.SYSTEM -> if(isTh) "ระบบ" else "System"
                                            com.example.data.model.AppThemeMode.LIGHT -> if(isTh) "สว่าง" else "Light"
                                            com.example.data.model.AppThemeMode.DARK -> if(isTh) "มืด" else "Dark"
                                            com.example.data.model.AppThemeMode.AUTO_TIME -> if(isTh) "ตามเวลา" else "Auto"
                                        }

                                        Button(
                                            onClick = { viewModel.setThemeMode(mode) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelected) AutoAmber else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Text(
                                                text = title,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 1.6: MEASUREMENT UNIT SYSTEM
                    item {
                        val currentSpeedUnit by viewModel.speedUnit.collectAsState()
                        val currentTempUnit by viewModel.temperatureUnit.collectAsState()
                        val currentPressUnit by viewModel.pressureUnit.collectAsState()
                        val currentFuelUnit by viewModel.fuelEconomyUnit.collectAsState()

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = AutoGreen, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isTh) "ตั้งค่าหน่วยวัด (Measurement Units)" else "Measurement Units",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                // Speed Unit Row
                                Text(
                                    text = if (isTh) "ความเร็ว (Speed):" else "Speed Unit:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    com.example.data.model.SpeedUnit.values().forEach { unit ->
                                        val isSelected = currentSpeedUnit == unit
                                        Button(
                                            onClick = { viewModel.setSpeedUnit(unit) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelected) AutoGreen else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Text(
                                                text = if (isTh) unit.symbolTh else unit.symbolEn,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }

                                // Temp Unit Row
                                Text(
                                    text = if (isTh) "อุณหภูมิ (Temperature):" else "Temperature Unit:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    com.example.data.model.TemperatureUnit.values().forEach { unit ->
                                        val isSelected = currentTempUnit == unit
                                        Button(
                                            onClick = { viewModel.setTemperatureUnit(unit) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelected) AutoGreen else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Text(
                                                text = unit.symbolTh,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }

                                // Pressure Unit Row
                                Text(
                                    text = if (isTh) "แรงดันบูสท์/เชื้อเพลิง (Pressure):" else "Pressure Unit:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    com.example.data.model.PressureUnit.values().forEach { unit ->
                                        val isSelected = currentPressUnit == unit
                                        Button(
                                            onClick = { viewModel.setPressureUnit(unit) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelected) AutoGreen else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Text(
                                                text = unit.symbolTh,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }

                                // Fuel Economy Unit Row
                                Text(
                                    text = if (isTh) "อัตราสิ้นเปลือง (Fuel Economy):" else "Fuel Economy Unit:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    com.example.data.model.FuelEconomyUnit.values().forEach { unit ->
                                        val isSelected = currentFuelUnit == unit
                                        Button(
                                            onClick = { viewModel.setFuelEconomyUnit(unit) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelected) AutoGreen else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Text(
                                                text = if (isTh) unit.symbolTh else unit.symbolEn,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 2: GOOGLE DRIVE CLOUD BACKUP & SYNC
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Cloud, contentDescription = null, tint = AutoGreen, modifier = Modifier.size(22.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isTh) "Google Drive คลาวด์ซิงค์" else "Google Drive Cloud Sync",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isDriveConnected) AutoGreen.copy(alpha = 0.15f) else AutoAmber.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = if (isDriveConnected) (if (isTh) "เชื่อมต่อแล้ว" else "Connected") else (if (isTh) "ยังไม่เชื่อมต่อ" else "Disconnected"),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (isDriveConnected) AutoGreen else AutoAmber,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (isDriveConnected) {
                                        if (isTh) "บัญชี: $driveEmail" else "Account: $driveEmail"
                                    } else {
                                        if (isTh) "เชื่อมต่อ Google Account เพื่อสำรองประวัติการซ่อม DTC และข้อมูลรถยนต์อัตโนมัติ" else "Connect Google account to backup vehicle logs and DTC history."
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                if (isDriveConnected) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = if (isTh) "สำรองล่าสุด: $lastBackupTime" else "Last Backup: $lastBackupTime",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    viewModel.backupToGoogleDrive { success, msg ->
                                                        isSuccessMessage = success
                                                        statusMessage = msg
                                                    }
                                                },
                                                enabled = !isDriveSyncing,
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(10.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = AutoGreen)
                                            ) {
                                                if (isDriveSyncing) {
                                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black, strokeWidth = 2.dp)
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                } else {
                                                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                }
                                                Text(if (isTh) "สำรองข้อมูลขึ้น Drive" else "Backup Now", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    viewModel.restoreFromGoogleDrive { success, msg ->
                                                        isSuccessMessage = success
                                                        statusMessage = msg
                                                    }
                                                },
                                                enabled = !isDriveSyncing,
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.CloudDownload, contentDescription = null, tint = AutoCyan, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(if (isTh) "กู้คืนข้อมูล" else "Restore", color = AutoCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                            }
                                        }

                                        OutlinedButton(
                                            onClick = { viewModel.disconnectGoogleDrive() },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(if (isTh) "ตัดการเชื่อมต่อ Google Account" else "Disconnect Google Account", color = AutoRed, fontSize = 11.sp)
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            viewModel.connectGoogleDrive("watcharapong.puangjamp@gmail.com")
                                            isSuccessMessage = true
                                            statusMessage = if (isTh) "เชื่อมต่อ Google Drive สำเร็จ" else "Google Drive connected successfully"
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                                    ) {
                                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(if (isTh) "เชื่อมต่อด้วย Google Sign-In" else "Sign in with Google Drive", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                    // SECTION 3: SYSTEM FEATURES (Voice Alerts, Snapshots, Logs)
                    item {
                        val isVoiceEnabled by viewModel.isVoiceAlertEnabled.collectAsState()
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (isTh) "คุณสมบัติระบบเพิ่มเติม" else "Advanced Features",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(if (isTh) "ระบบเสียงเตือนอัจฉริยะ" else "Smart Voice Alerts", fontWeight = FontWeight.Bold)
                                        Text(if (isTh) "แจ้งเตือนความร้อนและแบตเตอรี่ต่ำ" else "Audible warnings for engine heat & battery", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Switch(
                                        checked = isVoiceEnabled,
                                        onCheckedChange = { viewModel.setVoiceAlertEnabled(it) },
                                        colors = SwitchDefaults.colors(checkedThumbColor = AutoGreen, checkedTrackColor = AutoGreen.copy(alpha=0.3f))
                                    )
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                                OutlinedButton(
                                    onClick = { 
                                        onDismiss()
                                        viewModel.setShowSnapshotDialog(true) 
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (isTh) "ดูประวัติ Snapshot" else "View Snapshots")
                                }
                                
                                OutlinedButton(
                                    onClick = { 
                                        onDismiss()
                                        viewModel.setShowSystemLogsDialog(true) 
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Terminal, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (isTh) "ดูบันทึกการทำงานของระบบ (Logs)" else "View System Logs")
                                }
                            }
                        }
                    }

                    // SECTION 4: DEVELOPER CONTACT
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(if (isTh) "ติดต่อนักพัฒนาแอปพลิเคชัน" else "Developer Contact", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Email: watcharapong.puangjamp@gmail.com", style = MaterialTheme.typography.bodySmall)
                                Text("Tel: 0991546800", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(if (isTh) "ปิดหน้าต่าง" else "Close", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
