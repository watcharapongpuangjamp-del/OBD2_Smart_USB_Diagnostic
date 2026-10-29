package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.ui.MainViewModel

data class ActiveTestItem(
    val id: String,
    val titleTh: String,
    val titleEn: String,
    val category: String,
    val descriptionTh: String,
    val descriptionEn: String,
    val icon: ImageVector,
    val commands: List<String>,
    val options: List<String>
)

val activeTestItems = listOf(
    ActiveTestItem(
        id = "rad_fan",
        titleTh = "สั่งทดสอบพัดลมหม้อน้ำ (Radiator Fan PWM)",
        titleEn = "Radiator Fan PWM Test",
        category = "Cooling System",
        descriptionTh = "สั่งจ่ายสัญญาณ PWM 25% / 50% / 100% สั่งพัดลมหม้อน้ำทำงานเพื่อเช็กมอเตอร์และกล่องรีเลย์",
        descriptionEn = "Send PWM duty cycle to test cooling fan high/low speed relay & motor state.",
        icon = Icons.Default.AcUnit,
        commands = listOf("30 01 02 01", "30 01 02 02", "30 01 02 00"),
        options = listOf("LOW (25%)", "MED (50%)", "HIGH (100%)", "STOP")
    ),
    ActiveTestItem(
        id = "fuel_pump",
        titleTh = "สั่งเปิด/ปิดปั๊มน้ำมันเชื้อเพลิง (Fuel Pump Relay)",
        titleEn = "Fuel Pump Relay Control",
        category = "Fuel System",
        descriptionTh = "สั่งต่อรีเลย์ปั๊มติ๊กเพื่อสร้างแรงดันน้ำมันล่วงหน้าและเช็กเสียงมอเตอร์ปั๊ม",
        descriptionEn = "Actuate fuel pump relay to test fuel rail priming & pump motor continuity.",
        icon = Icons.Default.LocalGasStation,
        commands = listOf("2F 11 05 03 01", "2F 11 05 03 00"),
        options = listOf("PUMP ON", "PUMP OFF")
    ),
    ActiveTestItem(
        id = "injector_cut",
        titleTh = "สั่งตัดหัวฉีดแยกสูบ (Fuel Injector Cut-Off Test)",
        titleEn = "Cylinder Cut-Off Test",
        category = "Engine ECM",
        descriptionTh = "สั่งหยุดจ่ายน้ำมันหัวฉีดทีละสูบ (Cylinder 1-4) เพื่อวิเคราะห์สูบที่เดินไม่เต็มร้อย Misfire",
        descriptionEn = "Selectively disable individual fuel injectors to isolate misfiring cylinder.",
        icon = Icons.Default.Build,
        commands = listOf("30 02 01 00", "30 02 02 00", "30 02 03 00", "30 02 04 00"),
        options = listOf("Cut Cyl #1", "Cut Cyl #2", "Cut Cyl #3", "Cut Cyl #4", "Restore All")
    ),
    ActiveTestItem(
        id = "egr_valve",
        titleTh = "สั่งทดสอบวาล์ว EGR (EGR Actuator Sweep)",
        titleEn = "EGR Valve Sweep Test",
        category = "Emissions",
        descriptionTh = "สั่งกวาดวาล์ว EGR จาก 0% ถึง 100% เพื่อเช็กอาการวาล์วติดขัดจากคราบเขม่า",
        descriptionEn = "Actuate EGR stepper motor position to detect carbon fouling or mechanical sticking.",
        icon = Icons.Default.Sync,
        commands = listOf("2F 21 00 03 00", "2F 21 00 03 32", "2F 21 00 03 64"),
        options = listOf("Close 0%", "Open 50%", "Open 100%")
    ),
    ActiveTestItem(
        id = "abs_motor",
        titleTh = "สั่งปั๊มและโซลินอยด์ ABS (ABS Pump & Solenoid Cycle)",
        titleEn = "ABS Pump Motor & Solenoid Test",
        category = "Chassis ABS",
        descriptionTh = "สั่งปั๊มแรงดัน ABS ทำงานและเปิดโซลินอยด์วาล์วเพื่อไล่ลมเบรกหรือทดสอบระบบเบรก",
        descriptionEn = "Run ABS hydraulic motor pump and sequence valves for brake bleeding.",
        icon = Icons.Default.ReportProblem,
        commands = listOf("30 08 01 01", "30 08 01 00"),
        options = listOf("Cycle ABS Pump", "Stop Test")
    ),
    ActiveTestItem(
        id = "ac_clutch",
        titleTh = "สั่งต่อหน้าคลัตช์คอมแอร์ (A/C Clutch Relay Test)",
        titleEn = "A/C Compressor Clutch Test",
        category = "HVAC Climate",
        descriptionTh = "สั่งจ่ายไฟเข้าโซลินอยด์หน้าคลัตช์คอมเพรสเซอร์แอร์เพื่อทดสอบระบบความเย็น",
        descriptionEn = "Directly engage/disengage A/C compressor magnetic clutch relay.",
        icon = Icons.Default.AcUnit,
        commands = listOf("2F 12 01 03 01", "2F 12 01 03 00"),
        options = listOf("Engage Clutch", "Disengage Clutch")
    ),
    ActiveTestItem(
        id = "evap_purge",
        titleTh = "สั่งทดสอบวาล์ว EVAP Purge (Evaporative Solenoid)",
        titleEn = "EVAP Purge Valve Test",
        category = "Emissions",
        descriptionTh = "สั่งเปิดวาล์วดักไอระเหยน้ำมันเชื้อเพลิงถังดักไอ เพื่อเช็กการรั่วซึม vacuum leak",
        descriptionEn = "Pulsate EVAP canister purge solenoid valve to verify vacuum seal.",
        icon = Icons.Default.Compress,
        commands = listOf("2F 30 01 03 32", "2F 30 01 03 00"),
        options = listOf("Purge ON (50%)", "Purge OFF")
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveTestDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH

    val isExecuting by viewModel.isExecutingActiveTest.collectAsState()
    val testLogs by viewModel.activeTestLog.collectAsState()

    var selectedTest by remember { mutableStateOf(activeTestItems.first()) }
    var selectedOption by remember { mutableStateOf(selectedTest.options.first()) }

    Dialog(
        onDismissRequest = { if (!isExecuting) onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFF6D00).copy(alpha = 0.15f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = Color(0xFFFF6D00),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isTh) "การทดสอบสั่งการอุปกรณ์ (Active Test)" else "Active Test (Bi-Directional)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isTh) "สั่งการอุปกรณ์ฮาร์ดแวร์โดยตรงผ่าน ECU" else "Direct ECU Actuator Control Mode",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, enabled = !isExecuting) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider()

                // Safety Warning Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF3E0),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB74D))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(20.dp))
                        Text(
                            text = if (isTh) "ข้อควรระวัง: จอดรถในที่ปลอดภัย เข้าเกียร์ P ดึงเบรกมือ ก่อนเริ่มการสั่งการอุปกรณ์"
                            else "Caution: Park vehicle safely in 'P' with parking brake engaged before running tests.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFE65100)
                        )
                    }
                }

                // Test Selector & Console
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Left Column: Test Items List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(activeTestItems) { test ->
                            val isSelected = test.id == selectedTest.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !isExecuting) {
                                        selectedTest = test
                                        selectedOption = test.options.first()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = test.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Column {
                                        Text(
                                            text = if (isTh) test.titleTh else test.titleEn,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = test.category,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Right Column: Controls & Console Logs
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (isTh) selectedTest.titleTh else selectedTest.titleEn,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = if (isTh) selectedTest.descriptionTh else selectedTest.descriptionEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )

                        // Option Selector Chips
                        Text(
                            text = if (isTh) "เลือกรูปแบบการสั่งการ:" else "Select Command Mode:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            selectedTest.options.take(3).forEach { opt ->
                                val isOptSelected = opt == selectedOption
                                FilterChip(
                                    selected = isOptSelected,
                                    onClick = { selectedOption = opt },
                                    label = { Text(opt, fontSize = 10.sp) },
                                    enabled = !isExecuting
                                )
                            }
                        }

                        // Execute Action Button
                        Button(
                            onClick = {
                                viewModel.runActiveTest(
                                    testNameTh = "${selectedTest.titleTh} ($selectedOption)",
                                    testNameEn = "${selectedTest.titleEn} ($selectedOption)",
                                    commands = selectedTest.commands
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isExecuting,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00))
                        ) {
                            if (isExecuting) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (isTh) "กำลังส่งคำสั่ง..." else "Executing...", fontSize = 12.sp)
                            } else {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isTh) "สั่งการอุปกรณ์ ($selectedOption)" else "Run Test ($selectedOption)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // UDS Terminal Console Log
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1E1E1E)
                        ) {
                            LazyColumn(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (testLogs.isEmpty()) {
                                    item {
                                        Text(
                                            text = if (isTh) "> รอรับคำสั่งสั่งการอุปกรณ์..." else "> Awaiting Active Test command...",
                                            color = Color.Gray,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp
                                        )
                                    }
                                } else {
                                    items(testLogs) { log ->
                                        Text(
                                            text = log,
                                            color = if (log.startsWith("✔")) Color(0xFF69F0AE) else if (log.startsWith("▶")) Color(0xFFFFD54F) else Color(0xFF81D4FA),
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp
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
}
