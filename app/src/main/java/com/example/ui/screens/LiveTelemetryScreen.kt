package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.service.DrivingMode
import com.example.ui.MainViewModel
import com.example.ui.PerfTestState
import com.example.ui.components.MiniArcGauge
import com.example.ui.components.SensorMetricCard
import com.example.ui.components.SpeedometerGauge
import com.example.ui.components.TachometerGauge
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed
import com.example.ui.theme.getGaugeFontFamily

enum class TelemetryCategory {
    ALL,
    ENGINE,
    TRANSMISSION,
    ABS_CHASSIS,
    AIRBAG_SRS,
    BODY_ELECTRICAL
}

@Composable
fun LiveTelemetryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isThai by viewModel.language.collectAsState()
    val isTh = isThai == AppLanguage.TH
    val sensors by viewModel.liveSensors.collectAsState()
    val currentMode by viewModel.drivingMode.collectAsState()
    val rpmHistory by viewModel.rpmHistory.collectAsState()

    val uiCustomization by viewModel.uiCustomizationState.collectAsState()
    val gaugeFont = getGaugeFontFamily(uiCustomization.gaugeFont)

    var selectedTelemetryCategory by remember { mutableStateOf(TelemetryCategory.ALL) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Quick Mode Action Bar (HUD Display & OEM Baseline Comparison)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.setShowHudDialog(true) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTh) "โหมด HUD สะท้อนกระจก" else "HUD Mirror Mode",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { viewModel.setShowBaselineDialog(true) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AutoGreen)
                ) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTh) "เปรียบเทียบค่า OEM" else "OEM Baseline",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Smart Alerts
        item {
            val hasOverheat = sensors.coolantTempC > 100f
            val hasLowBattery = sensors.batteryVoltage < 12.0f && sensors.batteryVoltage > 0f
            val hasHighRpm = sensors.rpm > 5000

            AnimatedVisibility(visible = hasOverheat || hasLowBattery || hasHighRpm) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AutoRed.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, AutoRed.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WarningAmber, contentDescription = "Alert", tint = AutoRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTh) "ระบบแจ้งเตือนอัจฉริยะ (Smart Alerts)" else "Smart Alerts",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = AutoRed
                            )
                        }
                        if (hasOverheat) {
                            Text(text = if (isTh) "• อุณหภูมิเครื่องยนต์สูงเกิน 100°C" else "• Engine coolant temperature exceeds 100°C", style = MaterialTheme.typography.bodySmall, color = AutoRed)
                        }
                        if (hasLowBattery) {
                            Text(text = if (isTh) "• แรงดันแบตเตอรี่ต่ำกว่า 12V" else "• Battery voltage is critically low (< 12V)", style = MaterialTheme.typography.bodySmall, color = AutoRed)
                        }
                        if (hasHighRpm) {
                            Text(text = if (isTh) "• รอบเครื่องยนต์สูงเกินระดับปลอดภัย (> 5000 RPM)" else "• Engine RPM is critically high (> 5000 RPM)", style = MaterialTheme.typography.bodySmall, color = AutoRed)
                        }
                    }
                }
            }
        }

        // Driving Mode Simulator Selector
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = if (isTh) "โหมดจำลองการขับขี่ (Driving Profile Simulation)" else "Driving Condition Simulation",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DrivingModeChip(
                            label = if (isTh) "เดินเบา (Idle)" else "Idle",
                            isSelected = currentMode == DrivingMode.IDLE,
                            color = AutoGreen,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.emulatorService.setDrivingMode(DrivingMode.IDLE) }
                        )
                        DrivingModeChip(
                            label = if (isTh) "วิ่งปกติ (Cruise)" else "Cruise",
                            isSelected = currentMode == DrivingMode.CRUISE,
                            color = AutoCyan,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.emulatorService.setDrivingMode(DrivingMode.CRUISE) }
                        )
                        DrivingModeChip(
                            label = if (isTh) "เร่งแซง (Boost)" else "Hard Accel",
                            isSelected = currentMode == DrivingMode.HARD_ACCEL,
                            color = AutoAmber,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.emulatorService.setDrivingMode(DrivingMode.HARD_ACCEL) }
                        )
                        DrivingModeChip(
                            label = if (isTh) "เครื่องฮีต (Heat)" else "Overheat",
                            isSelected = currentMode == DrivingMode.OVERHEAT_WARNING,
                            color = AutoRed,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.emulatorService.setDrivingMode(DrivingMode.OVERHEAT_WARNING) }
                        )
                    }
                }
            }
        }

        // Performance Testing (0-100 km/h)
        item {
            val perfState by viewModel.perfTestState.collectAsState()
            val perfTime by viewModel.perf0to100Time.collectAsState()

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timer, contentDescription = "Performance", tint = AutoCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTh) "จับเวลา 0-100 กม./ชม." else "0-100 km/h Timer",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (perfState == PerfTestState.IDLE || perfState == PerfTestState.COMPLETED) {
                            Button(
                                onClick = { viewModel.startPerformanceTest() },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AutoGreen)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isTh) "เริ่ม" else "Start", fontSize = 12.sp)
                            }
                        } else {
                            Button(
                                onClick = { viewModel.stopPerformanceTest() },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AutoRed)
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isTh) "หยุด" else "Stop", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (perfState) {
                            PerfTestState.IDLE -> {
                                Text(
                                    text = if (isTh) "กด 'เริ่ม' เพื่อเตรียมพร้อม" else "Press 'Start' to ready",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            PerfTestState.READY -> {
                                Text(
                                    text = if (isTh) "พร้อม! เหยียบคันเร่งเพื่อเริ่มจับเวลา" else "Ready! Accelerate to start",
                                    color = AutoAmber,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            PerfTestState.ACCELERATING -> {
                                Text(
                                    text = if (isTh) "กำลังจับเวลา... ความเร็ว: ${sensors.speedKmh.toInt()} km/h" else "Timing... Speed: ${sensors.speedKmh.toInt()} km/h",
                                    color = AutoCyan,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            PerfTestState.COMPLETED -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (isTh) "เวลา 0-100 กม./ชม." else "0-100 km/h Time:",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = String.format("%.2f วินาที (sec)", perfTime ?: 0f),
                                        color = AutoGreen,
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dual Gauge Cluster Card
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SpeedometerGauge(
                            speedKmh = sensors.speedKmh,
                            fontFamily = gaugeFont,
                            fontScale = uiCustomization.gaugeFontScale
                        )
                        TachometerGauge(
                            rpm = sensors.rpm,
                            fontFamily = gaugeFont,
                            fontScale = uiCustomization.gaugeFontScale
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Arc Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MiniArcGauge(
                            title = if (isTh) "อุณหภูมิน้ำ" else "Coolant",
                            valueStr = "${sensors.coolantTempC}",
                            unit = "°C",
                            progress = sensors.coolantTempC / 120f,
                            barColor = if (sensors.coolantTempC > 100) AutoRed else AutoCyan,
                            modifier = Modifier.weight(1f)
                        )
                        MiniArcGauge(
                            title = if (isTh) "ไฟแบตเตอรี่" else "Battery",
                            valueStr = String.format("%.1f", sensors.batteryVoltage),
                            unit = "V",
                            progress = (sensors.batteryVoltage - 11f) / 4f,
                            barColor = AutoGreen,
                            modifier = Modifier.weight(1f)
                        )
                        MiniArcGauge(
                            title = if (isTh) "บูสต์เทอร์โบ" else "Boost",
                            valueStr = String.format("%.2f", sensors.boostPressureBar),
                            unit = "Bar",
                            progress = sensors.boostPressureBar / 2.0f,
                            barColor = AutoAmber,
                            modifier = Modifier.weight(1f)
                        )
                        MiniArcGauge(
                            title = if (isTh) "ตำแหน่งลิ้นเร่ง" else "Throttle",
                            valueStr = "${sensors.throttlePercent.toInt()}",
                            unit = "%",
                            progress = sensors.throttlePercent / 100f,
                            barColor = AutoCyan,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Live RPM Oscilloscope Graph
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C121E)),
                border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.3f))
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
                            text = if (isTh) "กราฟคลื่นสัญญาณ RPM แบบเรียลไทม์ (Live PID Trend)" else "Live RPM Oscilloscope",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AutoCyan
                        )
                        Text(
                            text = "${sensors.rpm} RPM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            ),
                            color = AutoGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    ) {
                        val points = rpmHistory
                        if (points.size < 2) return@Canvas

                        val maxVal = 4500f
                        val minVal = 500f
                        val stepX = size.width / (points.size - 1)

                        val path = Path()
                        points.forEachIndexed { index, rpm ->
                            val normalizedY = size.height - ((rpm - minVal) / (maxVal - minVal)).coerceIn(0f, 1f) * size.height
                            val x = index * stepX
                            if (index == 0) path.moveTo(x, normalizedY)
                            else path.lineTo(x, normalizedY)
                        }

                        // Grid lines
                        drawLine(
                            color = Color.White.copy(alpha = 0.1f),
                            start = Offset(0f, size.height * 0.25f),
                            end = Offset(size.width, size.height * 0.25f),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawLine(
                            color = Color.White.copy(alpha = 0.1f),
                            start = Offset(0f, size.height * 0.75f),
                            end = Offset(size.width, size.height * 0.75f),
                            strokeWidth = 1.dp.toPx()
                        )

                        drawPath(
                            path = path,
                            color = AutoCyan,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
        }

        // Module Category Tabs for Telemetry
        item {
            ScrollableTabRow(
                selectedTabIndex = TelemetryCategory.values().indexOf(selectedTelemetryCategory),
                edgePadding = 0.dp,
                divider = {},
                containerColor = Color.Transparent
            ) {
                TelemetryCategory.values().forEach { cat ->
                    val isSelected = selectedTelemetryCategory == cat
                    val title = when (cat) {
                        TelemetryCategory.ALL -> if (isTh) "ทั้งหมด (All)" else "All PIDs"
                        TelemetryCategory.ENGINE -> if (isTh) "เครื่องยนต์ (ECM)" else "Engine"
                        TelemetryCategory.TRANSMISSION -> if (isTh) "เกียร์ (TCM)" else "Transmission"
                        TelemetryCategory.ABS_CHASSIS -> if (isTh) "เบรก ABS" else "ABS & Chassis"
                        TelemetryCategory.AIRBAG_SRS -> if (isTh) "ถุงลม (SRS)" else "Airbag SRS"
                        TelemetryCategory.BODY_ELECTRICAL -> if (isTh) "ตัวถัง (BCM)" else "Body & Elec"
                    }
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTelemetryCategory = cat },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Module-specific telemetry metrics
        if (selectedTelemetryCategory == TelemetryCategory.ALL || selectedTelemetryCategory == TelemetryCategory.ENGINE) {
            item {
                SensorMetricCard(
                    titleTh = "แรงดันรางคอมมอนเรล",
                    titleEn = "Common Rail Fuel Pressure",
                    value = "${sensors.fuelRailPressureBar.toInt()}",
                    unit = "Bar",
                    icon = Icons.Default.LocalGasStation,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "อัตราไหลมวลอากาศ (MAF)",
                    titleEn = "Mass Air Flow (MAF)",
                    value = String.format("%.2f", sensors.mafGps),
                    unit = "g/s",
                    icon = Icons.Default.Tune,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "แรงดันน้ำมันเครื่อง (Oil Pressure)",
                    titleEn = "Engine Oil Pressure",
                    value = String.format("%.1f", sensors.engineOilPressureBar),
                    unit = "Bar",
                    icon = Icons.Default.Thermostat,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "ค่าชดเชยน้ำมันสั้น (STFT B1)",
                    titleEn = "Short Term Fuel Trim B1",
                    value = String.format("%.1f", sensors.stftPercent),
                    unit = "%",
                    isWarning = sensors.stftPercent > 8f,
                    icon = Icons.Default.Tune,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "ค่าชดเชยน้ำมันยาว (LTFT B1)",
                    titleEn = "Long Term Fuel Trim B1",
                    value = String.format("%.1f", sensors.ltftPercent),
                    unit = "%",
                    isWarning = sensors.ltftPercent > 10f,
                    icon = Icons.Default.Tune,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "เซนเซอร์ออกซิเจนตัวหน้า (O2)",
                    titleEn = "Upstream Oxygen Sensor Voltage",
                    value = String.format("%.3f", sensors.o2Sensor1Voltage),
                    unit = "V",
                    icon = Icons.Default.ElectricBolt,
                    isThai = isTh
                )
            }
        }

        if (selectedTelemetryCategory == TelemetryCategory.ALL || selectedTelemetryCategory == TelemetryCategory.TRANSMISSION) {
            item {
                SensorMetricCard(
                    titleTh = "อุณหภูมิน้ำมันเกียร์อัตโนมัติ (ATF/CVTF)",
                    titleEn = "Transmission Fluid Temperature",
                    value = "${sensors.transmissionFluidTempC}",
                    unit = "°C",
                    icon = Icons.Default.Settings,
                    isWarning = sensors.transmissionFluidTempC > 100,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "ตำแหน่งเกียร์ / อัตราทด (Current Gear)",
                    titleEn = "Current Gear & Ratio",
                    value = "${sensors.currentGear} (${sensors.gearRatio})",
                    unit = "",
                    icon = Icons.Default.Settings,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "สลิปทอร์คคอนเวอร์เตอร์ (TCC Slip)",
                    titleEn = "Torque Converter Clutch Slip",
                    value = "${sensors.torqueConverterSlipRpm}",
                    unit = "RPM",
                    icon = Icons.Default.Settings,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "แรงดันไฮดรอลิกเกียร์ (Line Pressure)",
                    titleEn = "TCM Hydraulic Line Pressure",
                    value = String.format("%.1f", sensors.tcmLinePressurePsi),
                    unit = "PSI",
                    icon = Icons.Default.Settings,
                    isThai = isTh
                )
            }
        }

        if (selectedTelemetryCategory == TelemetryCategory.ALL || selectedTelemetryCategory == TelemetryCategory.ABS_CHASSIS) {
            item {
                SensorMetricCard(
                    titleTh = "ความเร็วล้อหน้าซ้าย/ขวา (FL/FR Speed)",
                    titleEn = "Front Wheel Speeds (FL / FR)",
                    value = "${String.format("%.1f", sensors.wheelSpeedFlKmh)} / ${String.format("%.1f", sensors.wheelSpeedFrKmh)}",
                    unit = "km/h",
                    icon = Icons.Default.Security,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "ความเร็วล้อหลังซ้าย/ขวา (RL/RR Speed)",
                    titleEn = "Rear Wheel Speeds (RL / RR)",
                    value = "${String.format("%.1f", sensors.wheelSpeedRlKmh)} / ${String.format("%.1f", sensors.wheelSpeedRrKmh)}",
                    unit = "km/h",
                    icon = Icons.Default.Security,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "แรงดันน้ำมันเบรกไฮดรอลิก (Brake Pressure)",
                    titleEn = "Brake Line Hydraulic Pressure",
                    value = String.format("%.1f", sensors.brakeFluidPressureBar),
                    unit = "Bar",
                    icon = Icons.Default.Security,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "องศาพวงมาลัย (Steering Angle)",
                    titleEn = "Steering Angle Sensor (SAS)",
                    value = String.format("%.1f", sensors.steeringAngleDeg),
                    unit = "°",
                    icon = Icons.Default.Security,
                    isThai = isTh
                )
            }
        }

        if (selectedTelemetryCategory == TelemetryCategory.ALL || selectedTelemetryCategory == TelemetryCategory.AIRBAG_SRS) {
            item {
                SensorMetricCard(
                    titleTh = "ความต้านทานสายแพรถุงลมคนขับ (Driver Squib)",
                    titleEn = "Driver Airbag Squib Resistance",
                    value = String.format("%.2f", sensors.driverAirbagLoopResistanceOhm),
                    unit = "Ω",
                    icon = Icons.Default.WarningAmber,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "ความต้านทานถุงลมผู้โดยสาร (Passenger Squib)",
                    titleEn = "Passenger Airbag Squib Resistance",
                    value = String.format("%.2f", sensors.passengerAirbagLoopResistanceOhm),
                    unit = "Ω",
                    icon = Icons.Default.WarningAmber,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "เซนเซอร์ตรวจจับการชนหน้ารถ (Impact Sensor)",
                    titleEn = "Front Crash Impact Sensor",
                    value = sensors.frontImpactSensor1Status,
                    unit = "",
                    icon = Icons.Default.WarningAmber,
                    isThai = isTh
                )
            }
        }

        if (selectedTelemetryCategory == TelemetryCategory.ALL || selectedTelemetryCategory == TelemetryCategory.BODY_ELECTRICAL) {
            item {
                SensorMetricCard(
                    titleTh = "แรงดันไฟกล่องควบคุมตัวถัง (BCM Voltage)",
                    titleEn = "Body Control Module Main Supply",
                    value = String.format("%.2f", sensors.bcmSupplyVoltage),
                    unit = "V",
                    icon = Icons.Default.ElectricBolt,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "รอบการทำงานไดชาร์จ (Alternator Duty)",
                    titleEn = "Alternator Load Duty Cycle",
                    value = String.format("%.0f", sensors.alternatorDutyPercent),
                    unit = "%",
                    icon = Icons.Default.ElectricBolt,
                    isThai = isTh
                )
            }
            item {
                SensorMetricCard(
                    titleTh = "สัญญาณกุญแจ Smart Key RSSI",
                    titleEn = "Smart Key Transponder RSSI",
                    value = "${sensors.smartKeySignalRssi}",
                    unit = "dBm",
                    icon = Icons.Default.ElectricBolt,
                    isThai = isTh
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun DrivingModeChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                if (isSelected) color.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
            ),
            color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
