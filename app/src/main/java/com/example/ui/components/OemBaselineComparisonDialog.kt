package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.data.model.BaselineCategory
import com.example.data.model.EvaluatedSensorResult
import com.example.data.model.OemBaselineDatabase
import com.example.data.model.SensorComparisonStatus
import com.example.ui.MainViewModel
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed

@Composable
fun OemBaselineComparisonDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val isThai by viewModel.language.collectAsState()
    val isTh = isThai == AppLanguage.TH

    val liveSensors by viewModel.liveSensors.collectAsState()
    val speedUnit by viewModel.speedUnit.collectAsState()
    val tempUnit by viewModel.temperatureUnit.collectAsState()
    val pressUnit by viewModel.pressureUnit.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf<BaselineCategory?>(null) }

    // Evaluate all baseline parameters against live sensor values
    val evaluatedResults = remember(liveSensors, speedUnit, tempUnit, pressUnit) {
        OemBaselineDatabase.allBaselineParameters.map { param ->
            val liveRaw = when (param.id) {
                "coolant_temp" -> liveSensors.coolantTempC.toFloat()
                "battery_voltage" -> liveSensors.batteryVoltage
                "turbo_boost" -> liveSensors.boostPressureBar
                "fuel_rail_pressure" -> liveSensors.fuelRailPressureBar
                "engine_oil_pressure" -> liveSensors.engineOilPressureBar
                "transmission_temp" -> liveSensors.transmissionFluidTempC.toFloat()
                "short_term_fuel_trim" -> liveSensors.stftPercent
                "long_term_fuel_trim" -> liveSensors.ltftPercent
                "intake_air_temp" -> liveSensors.intakeAirTempC.toFloat()
                "srs_loop_resistance" -> liveSensors.driverAirbagLoopResistanceOhm
                else -> 0f
            }

            OemBaselineDatabase.evaluate(
                param = param,
                rawValue = liveRaw,
                speedUnit = speedUnit,
                tempUnit = tempUnit,
                pressUnit = pressUnit
            )
        }
    }

    val filteredResults = remember(evaluatedResults, selectedCategoryFilter) {
        if (selectedCategoryFilter == null) evaluatedResults
        else evaluatedResults.filter { it.parameter.category == selectedCategoryFilter }
    }

    val normalCount = evaluatedResults.count { it.status == SensorComparisonStatus.NORMAL }
    val warningCount = evaluatedResults.count { it.status == SensorComparisonStatus.LOW_WARNING || it.status == SensorComparisonStatus.HIGH_WARNING }
    val criticalCount = evaluatedResults.count { it.status == SensorComparisonStatus.CRITICAL }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = AutoCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isTh) "วิเคราะห์เปรียบเทียบค่าสดกับค่ามาตรฐาน OEM" else "OEM Baseline Comparison",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = if (isTh) "เปรียบเทียบเซนเซอร์สดกับเกณฑ์แนะนำของโรงงานผู้ผลิต" else "Live telemetry deviation vs Factory OEM specs",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Status Summary Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BaselineSummaryBadge(
                        label = if (isTh) "ค่าปกติ" else "Normal",
                        count = normalCount,
                        color = AutoGreen,
                        modifier = Modifier.weight(1f)
                    )
                    BaselineSummaryBadge(
                        label = if (isTh) "ต่างจากเกณฑ์" else "Deviated",
                        count = warningCount,
                        color = AutoAmber,
                        modifier = Modifier.weight(1f)
                    )
                    BaselineSummaryBadge(
                        label = if (isTh) "วิกฤต" else "Critical",
                        count = criticalCount,
                        color = AutoRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Category Filter Tabs
                ScrollableTabRow(
                    selectedTabIndex = when (selectedCategoryFilter) {
                        null -> 0
                        BaselineCategory.POWERTRAIN -> 1
                        BaselineCategory.TRANSMISSION -> 2
                        BaselineCategory.BODY_ELECTRICAL -> 3
                        else -> 0
                    },
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent,
                    contentColor = AutoCyan
                ) {
                    Tab(
                        selected = selectedCategoryFilter == null,
                        onClick = { selectedCategoryFilter = null },
                        text = { Text(if (isTh) "ทั้งหมด (${evaluatedResults.size})" else "All") }
                    )
                    Tab(
                        selected = selectedCategoryFilter == BaselineCategory.POWERTRAIN,
                        onClick = { selectedCategoryFilter = BaselineCategory.POWERTRAIN },
                        text = { Text(if (isTh) "เครื่องยนต์ (Engine)" else "Engine") }
                    )
                    Tab(
                        selected = selectedCategoryFilter == BaselineCategory.TRANSMISSION,
                        onClick = { selectedCategoryFilter = BaselineCategory.TRANSMISSION },
                        text = { Text(if (isTh) "เกียร์ (TCM)" else "Transmission") }
                    )
                    Tab(
                        selected = selectedCategoryFilter == BaselineCategory.BODY_ELECTRICAL,
                        onClick = { selectedCategoryFilter = BaselineCategory.BODY_ELECTRICAL },
                        text = { Text(if (isTh) "ระบบไฟฟ้า (Electrical)" else "Electrical") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // List of Baseline Comparisons
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredResults) { result ->
                        BaselineComparisonCard(result = result, isTh = isTh)
                    }
                }
            }
        }
    }
}

@Composable
private fun BaselineSummaryBadge(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$count", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = color)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = color)
        }
    }
}

@Composable
private fun BaselineComparisonCard(
    result: EvaluatedSensorResult,
    isTh: Boolean
) {
    var isExpanded by remember { mutableStateOf(false) }

    val statusColor = when (result.status) {
        SensorComparisonStatus.NORMAL -> AutoGreen
        SensorComparisonStatus.LOW_WARNING -> AutoAmber
        SensorComparisonStatus.HIGH_WARNING -> AutoAmber
        SensorComparisonStatus.CRITICAL -> AutoRed
    }

    val statusText = when (result.status) {
        SensorComparisonStatus.NORMAL -> if (isTh) "ค่าปกติอยู่ในเกณฑ์ OEM" else "Normal (Within OEM Specs)"
        SensorComparisonStatus.LOW_WARNING -> if (isTh) "ต่ำกว่าค่ามาตรฐาน OEM" else "Below Standard Range"
        SensorComparisonStatus.HIGH_WARNING -> if (isTh) "สูงกว่าค่ามาตรฐาน OEM" else "Above Standard Range"
        SensorComparisonStatus.CRITICAL -> if (isTh) "วิกฤต! นอกเกณฑ์ความปลอดภัย" else "Critical Deviation!"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isTh) result.parameter.titleTh else result.parameter.titleEn,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isTh) result.parameter.descriptionTh else result.parameter.descriptionEn,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Values Comparison Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Live Reading
                Column {
                    Text(text = if (isTh) "ค่าสดปัจจุบัน (Live)" else "Current Live", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${result.displayLiveValueFormatted} ${result.unitSymbol}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = statusColor
                    )
                }

                // Ideal Standard Target
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = if (isTh) "ค่ากลางมาตรฐาน (Ideal)" else "Ideal Baseline", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${result.displayIdealFormatted} ${result.unitSymbol}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AutoCyan
                    )
                }

                // Standard Range (Min - Max)
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = if (isTh) "ช่วงเกณฑ์ OEM (Range)" else "OEM Range", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${result.displayMinFormatted} - ${result.displayMaxFormatted} ${result.unitSymbol}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Visual Range Bar
            val liveVal = result.liveRawValue
            val minVal = result.parameter.minStandard
            val maxVal = result.parameter.maxStandard
            val range = (maxVal - minVal).coerceAtLeast(0.1f)
            val fraction = ((liveVal - minVal) / range).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
            ) {
                // Ideal Box
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AutoGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                )
                // Live Indicator Dot
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(10.dp)
                            .background(statusColor, CircleShape)
                    )
                }
            }

            // AI Tip / Advice Accordion
            AnimatedVisibility(visible = isExpanded || result.status != SensorComparisonStatus.NORMAL) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AutoCyan.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = AutoCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTh) result.parameter.aiTipTh else result.parameter.aiTipEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
