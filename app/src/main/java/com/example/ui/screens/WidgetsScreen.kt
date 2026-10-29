package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.AppLanguage
import com.example.ui.MainViewModel
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed
import com.example.ui.theme.getGaugeFontFamily

@Composable
fun WidgetsScreen(viewModel: MainViewModel) {
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH
    val liveSensors by viewModel.liveSensors.collectAsState()
    val activeDtcs by viewModel.activeDtcs.collectAsState()

    val uiCustomization by viewModel.uiCustomizationState.collectAsState()
    val gaugeFont = getGaugeFontFamily(uiCustomization.gaugeFont)

    val wSpeedVisible by viewModel.widgetSpeedVisible.collectAsState()
    val wBatteryVisible by viewModel.widgetBatteryVisible.collectAsState()
    val wCoolantVisible by viewModel.widgetCoolantVisible.collectAsState()
    val wDtcVisible by viewModel.widgetDtcVisible.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AutoCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dashboard,
                                contentDescription = null,
                                tint = AutoCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = if (isTh) "จัดการและแสดงผลวิดเจ็ตสด" else "Live Widgets Hub",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isTh) "เปิด/ปิด และปรับแต่งมินิวิดเจ็ตเรียลไทม์" else "Configure and toggle real-time mini widgets",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.toggleAllWidgets(true) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AutoCyan.copy(alpha = 0.2f))
                        ) {
                            Text(if (isTh) "แสดงทั้งหมด" else "Show All", color = AutoCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        Button(
                            onClick = { viewModel.toggleAllWidgets(false) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(if (isTh) "ซ่อนทั้งหมด" else "Hide All", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Widget Toggle 1: Speed & RPM
        item {
            WidgetConfigCard(
                title = if (isTh) "วิดเจ็ตความเร็วและรอบเครื่อง (Speed & RPM)" else "Speed & RPM Widget",
                isVisible = wSpeedVisible,
                onToggle = { viewModel.toggleWidgetSpeed() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "${liveSensors.speedKmh.toInt()} km/h", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontFamily = gaugeFont), color = AutoCyan)
                        Text(text = "RPM: ${liveSensors.rpm.toInt()} RPM", style = MaterialTheme.typography.bodySmall.copy(fontFamily = gaugeFont), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = AutoCyan, modifier = Modifier.size(36.dp))
                }
            }
        }

        // Widget Toggle 2: Battery Voltage
        item {
            WidgetConfigCard(
                title = if (isTh) "วิดเจ็ตแรงดันแบตเตอรี่ (Battery Voltage)" else "Battery Voltage Widget",
                isVisible = wBatteryVisible,
                onToggle = { viewModel.toggleWidgetBattery() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val isGood = liveSensors.batteryVoltage >= 13.2f
                        Text(text = "%.1f V".format(liveSensors.batteryVoltage), style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = if (isGood) AutoGreen else AutoAmber)
                        Text(text = if (isGood) (if (isTh) "ไดชาร์จทำงานปกติ" else "Alternator Normal") else (if (isTh) "แรงดันต่ำกว่าปกติ" else "Low Voltage"), style = MaterialTheme.typography.bodySmall, color = if (isGood) AutoGreen else AutoAmber)
                    }
                    Icon(imageVector = Icons.Default.BatteryChargingFull, contentDescription = null, tint = if (liveSensors.batteryVoltage >= 13.2f) AutoGreen else AutoAmber, modifier = Modifier.size(36.dp))
                }
            }
        }

        // Widget Toggle 3: Coolant Temperature
        item {
            WidgetConfigCard(
                title = if (isTh) "วิดเจ็ตอุณหภูมิน้ำหล่อเย็น (Coolant Temp)" else "Coolant Temperature Widget",
                isVisible = wCoolantVisible,
                onToggle = { viewModel.toggleWidgetCoolant() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val isHot = liveSensors.coolantTempC > 98
                        Text(text = "${liveSensors.coolantTempC} °C", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = if (isHot) AutoRed else AutoGreen)
                        Text(text = if (isHot) (if (isTh) "ระวัง! เครื่องยนต์ร้อนเกินไป" else "Warning! Overheating") else (if (isTh) "อุณหภูมิปกติ" else "Normal Temp"), style = MaterialTheme.typography.bodySmall, color = if (isHot) AutoRed else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(imageVector = Icons.Default.DeviceThermostat, contentDescription = null, tint = if (liveSensors.coolantTempC > 98) AutoRed else AutoGreen, modifier = Modifier.size(36.dp))
                }
            }
        }

        // Widget Toggle 4: Active DTCs
        item {
            WidgetConfigCard(
                title = if (isTh) "วิดเจ็ตโค้ดข้อผิดพลาด (Active DTCs)" else "Active DTCs Widget",
                isVisible = wDtcVisible,
                onToggle = { viewModel.toggleWidgetDtc() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "${activeDtcs.size} " + if (isTh) "รายการปัญหา" else "Faults Found", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = if (activeDtcs.isEmpty()) AutoGreen else AutoRed)
                        Text(text = if (activeDtcs.isEmpty()) (if (isTh) "ระบบปกติ ไม่มีโค้ดเสีย" else "No DTC codes active") else (if (isTh) "พบโค้ดที่ต้องตรวจสอบ" else "Requires attention"), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(imageVector = if (activeDtcs.isEmpty()) Icons.Default.CheckCircle else Icons.Default.Warning, contentDescription = null, tint = if (activeDtcs.isEmpty()) AutoGreen else AutoRed, modifier = Modifier.size(36.dp))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun WidgetConfigCard(
    title: String,
    isVisible: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
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
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = isVisible,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(checkedThumbColor = AutoCyan)
                )
            }

            AnimatedVisibility(visible = isVisible) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    content()
                }
            }
        }
    }
}
