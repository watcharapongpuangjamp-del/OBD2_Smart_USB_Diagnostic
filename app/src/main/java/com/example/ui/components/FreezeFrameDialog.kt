package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FreezeFrameData
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FreezeFrameDialog(
    freezeFrame: FreezeFrameData?,
    carSpec: com.example.data.model.ThaiCarSpec? = null,
    isThai: Boolean = true,
    onDismiss: () -> Unit
) {
    if (freezeFrame == null) return

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(freezeFrame.timestamp))
    val vehicleAnalysis = remember(freezeFrame, carSpec) {
        com.example.data.repository.ThaiCarDatabase.analyzeFreezeFrameForVehicle(carSpec, freezeFrame)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Title Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(AutoRed.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "FREEZE FRAME",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AutoRed
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = freezeFrame.triggerDtc,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = if (isThai) "บันทึกค่าเซนเซอร์เมื่อเกิด Fault Code (${vehicleAnalysis.vehicleName} - $formattedDate)"
                    else "Engine telemetry snapshot recorded for ${vehicleAnalysis.vehicleName} ($formattedDate)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Vehicle Specific AI Analysis Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(AutoCyan.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = if (isThai) "🔍 วิเคราะห์พารามิเตอร์เทียบค่ามาตรฐานรถรุ่นนี้:" else "🔍 Baseline Parameter Analysis:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AutoCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isThai) vehicleAnalysis.overallDiagnosisTh else vehicleAnalysis.overallDiagnosisEn,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable parameters table
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FreezeFrameRow(
                        labelTh = "รอบเครื่องยนต์ (Engine RPM)",
                        labelEn = "Engine Speed (RPM)",
                        value = "${freezeFrame.engineRpm} RPM",
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "ความเร็วรถ (Vehicle Speed)",
                        labelEn = "Vehicle Speed",
                        value = "${freezeFrame.vehicleSpeedKmh} km/h",
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "ภาระโหลดเครื่องยนต์ (Engine Load)",
                        labelEn = "Calculated Engine Load",
                        value = "${freezeFrame.engineLoadPercent}%",
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "อุณหภูมิน้ำหล่อเย็น (Coolant Temp)",
                        labelEn = "Coolant Temperature",
                        value = "${freezeFrame.coolantTempCelsius} °C",
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "อุณหภูมิอากาศไอดี (IAT)",
                        labelEn = "Intake Air Temp (IAT)",
                        value = "${freezeFrame.intakeAirTempCelsius} °C",
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "ค่าชดเชยน้ำมันสั้น (STFT Bank 1)",
                        labelEn = "Short Term Fuel Trim B1",
                        value = "${freezeFrame.shortTermFuelTrimPercent}%",
                        highlight = freezeFrame.shortTermFuelTrimPercent > 10f,
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "ค่าชดเชยน้ำมันยาว (LTFT Bank 1)",
                        labelEn = "Long Term Fuel Trim B1",
                        value = "${freezeFrame.longTermFuelTrimPercent}%",
                        highlight = freezeFrame.longTermFuelTrimPercent > 15f,
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "มวลอากาศไอดี (MAF)",
                        labelEn = "Mass Air Flow (MAF)",
                        value = "${freezeFrame.massAirFlowGps} g/s",
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "ตำแหน่งลิ้นเร่ง (Throttle Pos)",
                        labelEn = "Throttle Position",
                        value = "${freezeFrame.throttlePositionPercent}%",
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "แรงดันรางคอมมอนเรล (Fuel Rail)",
                        labelEn = "Fuel Rail Pressure",
                        value = "${freezeFrame.fuelRailPressureBar} Bar",
                        isThai = isThai
                    )
                    FreezeFrameRow(
                        labelTh = "แรงดันไฟแบตเตอรี่ (Battery V)",
                        labelEn = "Module Voltage",
                        value = "${freezeFrame.batteryVoltage} V",
                        isThai = isThai
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                ) {
                    Text(
                        text = if (isThai) "ปิดหน้าต่าง" else "Close Snapshot",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FreezeFrameRow(
    labelTh: String,
    labelEn: String,
    value: String,
    highlight: Boolean = false,
    isThai: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (highlight) AutoAmber.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isThai) labelTh else labelEn,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            ),
            color = if (highlight) AutoAmber else AutoCyan
        )
    }
}
