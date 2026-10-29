package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.data.model.HudColorTheme
import com.example.data.model.HudLayoutMode
import com.example.data.model.UnitConverter
import com.example.ui.MainViewModel

@Composable
fun HudDisplayDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val isThai by viewModel.language.collectAsState()
    val isTh = isThai == AppLanguage.TH

    val liveSensors by viewModel.liveSensors.collectAsState()
    val isMirrored by viewModel.isHudMirrored.collectAsState()
    val colorTheme by viewModel.hudColorTheme.collectAsState()
    val layoutMode by viewModel.hudLayoutMode.collectAsState()

    val speedUnit by viewModel.speedUnit.collectAsState()
    val tempUnit by viewModel.temperatureUnit.collectAsState()
    val pressUnit by viewModel.pressureUnit.collectAsState()

    val displaySpeed = UnitConverter.convertSpeed(liveSensors.speedKmh.toFloat(), speedUnit)
    val displayTemp = UnitConverter.convertTemp(liveSensors.coolantTempC.toFloat(), tempUnit)
    val displayBoost = UnitConverter.convertPressure(liveSensors.boostPressureBar, pressUnit)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Control Bar (Non-mirrored for easy touch configuration)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF121212), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(colorTheme.primaryColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTh) "โหมดสะท้อนกระจก HUD" else "Windshield HUD Mode",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Toggle Mirror Button
                        Button(
                            onClick = { viewModel.toggleHudMirror() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isMirrored) colorTheme.primaryColor else Color.DarkGray,
                                contentColor = if (isMirrored) Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Flip, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isMirrored) (if (isTh) "สะท้อนภาพอยู่" else "Mirrored") else (if (isTh) "ภาพปกติ" else "Normal"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.background(Color(0xFF2A2A2A), CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close HUD", tint = Color.White)
                        }
                    }
                }

                // Main Mirrorable HUD Display Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .graphicsLayer {
                            scaleX = if (isMirrored) -1f else 1f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when (layoutMode) {
                        HudLayoutMode.MINIMAL_SPEED -> {
                            MinimalSpeedHudContent(
                                speed = displaySpeed.toInt(),
                                speedSymbol = if (isTh) speedUnit.symbolTh else speedUnit.symbolEn,
                                colorTheme = colorTheme,
                                isEngineRunning = liveSensors.isEngineRunning
                            )
                        }
                        HudLayoutMode.DASHBOARD_TRIO -> {
                            TrioDashboardHudContent(
                                speed = displaySpeed.toInt(),
                                speedSymbol = if (isTh) speedUnit.symbolTh else speedUnit.symbolEn,
                                rpm = liveSensors.rpm,
                                temp = displayTemp,
                                tempSymbol = tempUnit.symbolTh,
                                boost = displayBoost,
                                boostSymbol = pressUnit.symbolTh,
                                voltage = liveSensors.batteryVoltage,
                                gear = liveSensors.currentGear,
                                colorTheme = colorTheme
                            )
                        }
                        HudLayoutMode.FULL_TELEMETRY -> {
                            FullTelemetryHudContent(
                                speed = displaySpeed.toInt(),
                                speedSymbol = if (isTh) speedUnit.symbolTh else speedUnit.symbolEn,
                                rpm = liveSensors.rpm,
                                temp = displayTemp,
                                tempSymbol = tempUnit.symbolTh,
                                boost = displayBoost,
                                boostSymbol = pressUnit.symbolTh,
                                voltage = liveSensors.batteryVoltage,
                                gear = liveSensors.currentGear,
                                fuelRailBar = UnitConverter.convertPressure(liveSensors.fuelRailPressureBar, pressUnit),
                                colorTheme = colorTheme
                            )
                        }
                    }
                }

                // Bottom Control Bar (Theme & Layout Selectors)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF121212), RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Color Theme Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Palette, contentDescription = null, tint = colorTheme.primaryColor, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isTh) "ธีมสีสะท้อน:" else "Color Theme:",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(HudColorTheme.values()) { theme ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(theme.primaryColor, CircleShape)
                                        .border(
                                            width = if (colorTheme == theme) 2.dp else 0.dp,
                                            color = if (colorTheme == theme) Color.White else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { viewModel.setHudColorTheme(theme) }
                                )
                            }
                        }
                    }

                    // Layout Mode Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ViewCarousel, contentDescription = null, tint = colorTheme.primaryColor, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isTh) "รูปแบบการแสดงผล:" else "HUD Layout:",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(HudLayoutMode.values()) { mode ->
                                val isSelected = layoutMode == mode
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSelected) colorTheme.primaryColor else Color(0xFF2A2A2A),
                                    modifier = Modifier.clickable { viewModel.setHudLayoutMode(mode) }
                                ) {
                                    Text(
                                        text = if (isTh) mode.titleTh else mode.titleEn,
                                        color = if (isSelected) Color.Black else Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MinimalSpeedHudContent(
    speed: Int,
    speedSymbol: String,
    colorTheme: HudColorTheme,
    isEngineRunning: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$speed",
            color = colorTheme.primaryColor,
            fontSize = 130.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            lineHeight = 130.sp
        )
        Text(
            text = speedSymbol.uppercase(),
            color = colorTheme.accentColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isEngineRunning) "• READY TO DRIVE •" else "• ENGINE STOPPED •",
            color = colorTheme.primaryColor.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun TrioDashboardHudContent(
    speed: Int,
    speedSymbol: String,
    rpm: Int,
    temp: Float,
    tempSymbol: String,
    boost: Float,
    boostSymbol: String,
    voltage: Float,
    gear: String,
    colorTheme: HudColorTheme
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Gear & Voltage Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "GEAR: $gear",
                color = colorTheme.accentColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = String.format("%.1fV", voltage),
                color = colorTheme.primaryColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Large Speed
        Text(
            text = "$speed",
            color = colorTheme.primaryColor,
            fontSize = 110.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            lineHeight = 110.sp
        )
        Text(
            text = speedSymbol.uppercase(),
            color = colorTheme.primaryColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(16.dp))

        // RPM Bar
        val rpmPercent = (rpm / 6000f).coerceIn(0f, 1f)
        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "RPM", color = colorTheme.accentColor, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Text(text = "$rpm", color = colorTheme.primaryColor, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(Color(0xFF222222), RoundedCornerShape(6.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(rpmPercent)
                        .height(12.dp)
                        .background(colorTheme.primaryColor, RoundedCornerShape(6.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Temp & Boost Row
        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = "COOLANT", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                Text(
                    text = String.format("%.0f%s", temp, tempSymbol),
                    color = colorTheme.primaryColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "BOOST", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                Text(
                    text = String.format("%.2f %s", boost, boostSymbol),
                    color = colorTheme.accentColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun FullTelemetryHudContent(
    speed: Int,
    speedSymbol: String,
    rpm: Int,
    temp: Float,
    tempSymbol: String,
    boost: Float,
    boostSymbol: String,
    voltage: Float,
    gear: String,
    fuelRailBar: Float,
    colorTheme: HudColorTheme
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Shift Light Bar
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(10.dp)
                .background(Color(0xFF111111), RoundedCornerShape(5.dp)),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val step = rpm / 600
            for (i in 1..10) {
                val stepColor = when {
                    i <= 6 -> colorTheme.primaryColor
                    i <= 8 -> colorTheme.accentColor
                    else -> Color.Red
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .background(if (step >= i) stepColor else Color(0xFF222222), RoundedCornerShape(3.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column Metrics
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                HudMetricBox("GEAR", gear, colorTheme.accentColor)
                HudMetricBox("VOLT", String.format("%.1fV", voltage), colorTheme.primaryColor)
                HudMetricBox("RAIL", String.format("%.0f Bar", fuelRailBar), colorTheme.primaryColor)
            }

            // Center Giant Speed
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$speed",
                    color = colorTheme.primaryColor,
                    fontSize = 100.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 100.sp
                )
                Text(
                    text = speedSymbol.uppercase(),
                    color = colorTheme.primaryColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Right Column Metrics
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HudMetricBox("RPM", "$rpm", colorTheme.primaryColor)
                HudMetricBox("COOL", String.format("%.0f%s", temp, tempSymbol), colorTheme.accentColor)
                HudMetricBox("BOOST", String.format("%.2f %s", boost, boostSymbol), colorTheme.accentColor)
            }
        }
    }
}

@Composable
private fun HudMetricBox(label: String, value: String, color: Color) {
    Column {
        Text(text = label, color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        Text(text = value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}
