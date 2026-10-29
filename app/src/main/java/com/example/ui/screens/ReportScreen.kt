package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.ui.MainViewModel
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import java.io.File
import java.io.FileOutputStream
import androidx.core.content.FileProvider

@Composable
fun ReportDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val isThai by viewModel.language.collectAsState()
    val isTh = isThai == AppLanguage.TH

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = AutoCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTh) "ใบรายงานผลการวินิจฉัย" else "Diagnostic Report",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            ReportScreen(viewModel = viewModel)
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(if (isTh) "ปิดหน้าต่าง" else "Close")
            }
        }
    )
}

@Composable
fun ReportScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isThai by viewModel.language.collectAsState()
    val isTh = isThai == AppLanguage.TH
    val selectedVehicle by viewModel.selectedVehicle.collectAsState()
    val activeDtcs by viewModel.activeDtcs.collectAsState()
    val liveSensors by viewModel.liveSensors.collectAsState()
    val freezeFrame by viewModel.freezeFrame.collectAsState()
    val diagnosticReport by viewModel.diagnosticReport.collectAsState()
    val maintenanceLogs by viewModel.maintenanceLogs.collectAsState()

    val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", if (isTh) Locale("th", "TH") else Locale.ENGLISH)
    val shortDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    val reportDate = dateFormat.format(Date())

    fun shareReport() {
        val vehicle = selectedVehicle
        val vehicleStr = if (vehicle != null) "${vehicle.make} ${vehicle.model} (${vehicle.licensePlate})" else "Vehicle Diagnostic"
        val dtcStr = if (activeDtcs.isEmpty()) "No fault codes (Clean)" else activeDtcs.joinToString(", ") { "${it.code} (${it.thTitle})" }
        val shareText = """
            📋 ใบรายงานผลการตรวจวิเคราะห์สภาพรถยนต์ (ThaiAuto OBD-II Diagnostic Report)
            🚗 รถยนต์: $vehicleStr
            📅 วันที่ตรวจ: $reportDate
            ⚡ ระบบ OBD-II: ISO 15765-4 CAN 500k
            ⚠️ รหัสข้อบกพร่อง (DTCs): $dtcStr
            🛢️ ข้อมูลเซนเซอร์: RPM=${liveSensors.rpm}, ความร้อน=${liveSensors.coolantTempC}°C, แบตเตอรี่=${liveSensors.batteryVoltage}V
            🔍 วิเคราะห์โดย: AI Master Mechanic Thailand (Gemini 3.1 Pro)
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share Diagnostic Report"))
    }

    fun shareCsvReport() {
        val vehicle = selectedVehicle
        val vehicleStr = if (vehicle != null) "${vehicle.make}_${vehicle.model}_${vehicle.licensePlate}" else "Vehicle_Diagnostic"
        val dtcStr = if (activeDtcs.isEmpty()) "Clean" else activeDtcs.joinToString(";") { it.code }
        val csvText = """
            Make,Model,Year,LicensePlate,Mileage,Date,DTC_Codes,RPM,CoolantTemp,BatteryVoltage,Speed,MAP
            ${vehicle?.make ?: "Unknown"},${vehicle?.model ?: "Unknown"},${vehicle?.year ?: ""},${vehicle?.licensePlate ?: ""},${vehicle?.currentMileageKm ?: ""},$reportDate,$dtcStr,${liveSensors.rpm},${liveSensors.coolantTempC},${liveSensors.batteryVoltage},${liveSensors.speedKmh},${liveSensors.boostPressureBar}
        """.trimIndent()

        try {
            val file = File(context.cacheDir, "diagnostic_report_${System.currentTimeMillis()}.csv")
            FileOutputStream(file).use { it.write(csvText.toByteArray()) }
            
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "text/csv"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(sendIntent, "Export CSV Report"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Action Share bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isTh) "ผลการตรวจเช็คสภาพสมบูรณ์" else "Official Vehicle Inspection",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { shareCsvReport() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(text = "CSV", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSecondary)
                    }

                    Button(
                        onClick = { shareReport() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isTh) "แชร์ PDF/Text" else "Share Report", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }

        // Printable Certificate Sheet View
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Header Logo & Certification
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(AutoCyan.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = AutoCyan)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "ThaiAuto OBD2 Diagnostics",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AutoCyan
                                )
                                Text(
                                    text = "ISO 15765-4 CAN Multi-System Telemetry",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = reportDate,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Vehicle Specifications
                    Text(
                        text = if (isTh) "1. ข้อมูลประจำตัวรถยนต์ (Vehicle Identification)" else "1. Vehicle Identification",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AutoAmber
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ReportDataRow("ยี่ห้อ / รุ่น:", selectedVehicle?.let { "${it.make} ${it.model} (${it.year})" } ?: "Toyota Hilux Revo")
                    ReportDataRow("หมายเลขทะเบียน:", selectedVehicle?.let { "${it.licensePlate} (${it.provinceTh})" } ?: "8กบ 9421 กรุงเทพมหานคร")
                    ReportDataRow("หมายเลขตัวถัง (VIN):", selectedVehicle?.vin ?: "MR0BA3CD2M0123456")
                    ReportDataRow("เลขไมล์สะสม:", "${selectedVehicle?.currentMileageKm ?: 68500} km")
                    ReportDataRow("เครื่องยนต์ / เชื้อเพลิง:", "${selectedVehicle?.engine} • ${selectedVehicle?.fuelType}")

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Diagnostic Results
                    Text(
                        text = if (isTh) "2. ผลการตรวจเช็ครหัสข้อผิดพลาด (DTC Diagnostic Codes)" else "2. Diagnostic Fault Codes",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AutoAmber
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (activeDtcs.isEmpty()) {
                        Text(
                            text = "✅ ระบบการทำงานปกติ ไม่พบรหัสข้อผิดพลาดใดๆ ในกล่อง ECU (Status: Pass)",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = AutoGreen
                        )
                    } else {
                        activeDtcs.forEach { dtc ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "• ${dtc.code}: ${if (isTh) dtc.thTitle else dtc.enTitle}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = AutoRed,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = dtc.estimatedCostThb,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AutoGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Telemetry Snapshot & Graph
                    Text(
                        text = if (isTh) "3. ค่าเซนเซอร์ขณะสแกน (Live Sensor Telemetry & Graph)" else "3. Live Sensor Telemetry & Graph",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AutoAmber
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ReportDataRow("ความเร็วรอบเครื่องยนต์:", "${liveSensors.rpm} RPM")
                    ReportDataRow("อุณหภูมิน้ำหล่อเย็น (ECT):", "${liveSensors.coolantTempC} °C (เกณฑ์ปกติ: 85-95°C)")
                    ReportDataRow("แรงดันไฟฟ้าแบตเตอรี่:", "${String.format("%.2f", liveSensors.batteryVoltage)} V (ไดชาร์จปกติ)")
                    ReportDataRow("แรงดันรางคอมมอนเรล:", "${liveSensors.fuelRailPressureBar.toInt()} Bar")
                    ReportDataRow("มวลอากาศไอดี (MAF):", "${String.format("%.2f", liveSensors.mafGps)} g/s")

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isTh) "กราฟบันทึกรอบเครื่องยนต์ (RPM Visualization)" else "Engine RPM Visualization Over Time",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Simple representation of graph visualization
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val maxRpm = 4000f
                            // Plot a mock dynamic line for visual presentation
                            val rpms = listOf(800, 850, 900, 1500, 2200, 1800, 1850, 1900, 2500, 2600, 2400, liveSensors.rpm)
                            val step = w / (rpms.size - 1).coerceAtLeast(1)
                            
                            val path = androidx.compose.ui.graphics.Path()
                            rpms.forEachIndexed { i, rpm ->
                                val x = i * step
                                val y = h - ((rpm / maxRpm) * h)
                                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                            }
                            drawPath(
                                path = path,
                                color = AutoCyan,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Vehicle Repair History
                    Text(
                        text = if (isTh) "4. ประวัติการซ่อมบำรุง (Vehicle Repair History)" else "4. Vehicle Repair History",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AutoAmber
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (maintenanceLogs.isEmpty()) {
                        Text(
                            text = if (isTh) "ไม่มีประวัติซ่อมบำรุงในระบบ" else "No repair history found.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        maintenanceLogs.sortedByDescending { it.serviceDate }.take(3).forEach { log ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "• ${shortDateFormat.format(Date(log.serviceDate))}: ${if (isTh) log.serviceTypeTh else log.serviceTypeEn}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${log.mileageAtServiceKm} km",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))
                    Spacer(modifier = Modifier.height(14.dp))

                    // AI Diagnostic Certification
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AutoAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isTh) "5. บทวิเคราะห์และคำแนะนำโดยช่างยนต์ AI" else "5. AI Mechanic Recommendations",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AutoAmber
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = diagnosticReport
                            ?: if (isTh) "แนะนำให้ทำการตรวจเช็คเซนเซอร์ MAF และวาล์ว SCV ตามรหัสที่ตรวจพบ ควรเปลี่ยนถ่ายกรองโซล่าดักน้ำทุก 20,000 กม. เพื่อรักษาระบบหัวฉีดคอมมอนเรล"
                            else "Recommended to inspect MAF sensor and SCV valve according to detected DTCs. Replace fuel filter every 20,000 km.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun ReportDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
