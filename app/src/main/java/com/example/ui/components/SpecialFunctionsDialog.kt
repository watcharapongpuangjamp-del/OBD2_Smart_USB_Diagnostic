package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.ui.MainViewModel

data class SpecialFunctionItem(
    val id: String,
    val titleTh: String,
    val titleEn: String,
    val category: String,
    val descriptionTh: String,
    val descriptionEn: String,
    val icon: ImageVector,
    val requiresInput: Boolean,
    val inputLabelTh: String = "",
    val inputLabelEn: String = "",
    val defaultInput: String = ""
)

val specialFunctionItems = listOf(
    SpecialFunctionItem(
        id = "injector_coding",
        titleTh = "ลงทะเบียนรหัสหัวฉีดใหม่ (Injector IMA Coding)",
        titleEn = "Fuel Injector IMA Coding",
        category = "Fuel / Engine",
        descriptionTh = "ป้อนรหัส IMA Code (16-20 หลัก) บนหัวฉีดใหม่ เพื่อบันทึกค่าการชดเชยปริมาณน้ำมันลงกล่อง ECU",
        descriptionEn = "Register 16-20 digit IMA QR codes into ECM EEPROM after replacing fuel injectors.",
        icon = Icons.Default.QrCode,
        requiresInput = true,
        inputLabelTh = "รหัส IMA Code หัวฉีด (สูบ 1):",
        inputLabelEn = "Injector IMA Code (Cyl #1):",
        defaultInput = "A7D9K201M4P9X2"
    ),
    SpecialFunctionItem(
        id = "battery_registration",
        titleTh = "ลงทะเบียนเปลี่ยนแบตเตอรี่ใหม่ (Battery Reset / BMS)",
        titleEn = "Battery Registration & BMS Reset",
        category = "Electrical System",
        descriptionTh = "รีเซ็ตตารางอายุแบตเตอรี่ในกล่อง BMS และลงทะเบียนชนิด/ความจุแบตใหม่ (AGM / EFB / Lead-Acid)",
        descriptionEn = "Reset Battery Monitoring System (BMS) age table and register new battery capacity & chemistry.",
        icon = Icons.Default.BatteryChargingFull,
        requiresInput = true,
        inputLabelTh = "ชนิด & ความจุ (เช่น 70Ah AGM):",
        inputLabelEn = "Type & Capacity (e.g. 70Ah AGM):",
        defaultInput = "75Ah AGM / Sealed Lead-Acid"
    ),
    SpecialFunctionItem(
        id = "throttle_relearn",
        titleTh = "รีเซ็ตลิ้นปีกผีเสื้อ (Throttle Body Re-learn / TPS)",
        titleEn = "Throttle Body Adaptation / Idle Re-learn",
        category = "Engine ECM",
        descriptionTh = "สั่ง Calibrate ค่าตำแหน่งเปิด-ปิดลิ้นปีกผีเสื้อใหม่ หลังล้างลิ้นปีกผีเสื้อหรือเปลี่ยนลิ้นใหม่",
        descriptionEn = "Calibrate minimum/maximum throttle valve stop angles after cleaning or replacement.",
        icon = Icons.Default.SettingsSuggest,
        requiresInput = false
    ),
    SpecialFunctionItem(
        id = "dpf_regen",
        titleTh = "สั่งเผาเขม่า DPF & Reset ค่าสะสม (DPF Regen & Reset)",
        titleEn = "DPF Service Regeneration & Reset",
        category = "Exhaust / DPF",
        descriptionTh = "สั่งเผาเขม่ากรอง DPF แบบจอด (Stationary Forced Regen) และรีเซ็ตค่า Soot Mass หลังเปลี่ยน DPF",
        descriptionEn = "Initiate forced stationary DPF thermal regen and reset particulate soot mass counter.",
        icon = Icons.Default.LocalFireDepartment,
        requiresInput = false
    ),
    SpecialFunctionItem(
        id = "sas_calibration",
        titleTh = "ตั้งศูนย์องศาพวงมาลัย (SAS Zero Point Calibration)",
        titleEn = "Steering Angle Sensor Zero Calibration",
        category = "Chassis Steering",
        descriptionTh = "ปรับตั้งค่าศูนย์องศาพวงมาลัย (0 Degrees) เข้ากล่อง ESP/ABS หลังตั้งศูนย์ถ่วงล้อหรือเปลี่ยนลูกหมาก",
        descriptionEn = "Reset Steering Angle Sensor (SAS) zero degree position after wheel alignment.",
        icon = Icons.Default.Tune,
        requiresInput = false
    ),
    SpecialFunctionItem(
        id = "epb_service",
        titleTh = "โหมดเปลี่ยนผ้าเบรกมือไฟฟ้า (EPB Service Mode Retract)",
        titleEn = "EPB Service Mode & Brake Pad Reset",
        category = "Braking System",
        descriptionTh = "สั่งมอเตอร์เบรกมือไฟฟ้าถอยสุดเพื่อถอดเปลี่ยนผ้าเบรกหลัง และสั่ง Calibrate ระยะเบรกใหม่",
        descriptionEn = "Retract electronic parking brake calipers for rear pad replacement & re-engage calibration.",
        icon = Icons.Default.Build,
        requiresInput = false
    ),
    SpecialFunctionItem(
        id = "trans_adaptation",
        titleTh = "รีเซ็ตตารางชดเชยเกียร์ (Transmission Adaptation Reset)",
        titleEn = "Transmission Clutch Adaptation Reset",
        category = "Powertrain / CVT",
        descriptionTh = "รีเซ็ตค่าแรงดันโซลินอยด์และระยะจับคลัตช์เกียร์ออโต้ / CVT หลังเปลี่ยนน้ำมันเกียร์หรือสมองเกียร์",
        descriptionEn = "Reset automatic / CVT clutch engagement pressure offsets after ATF drain & refill.",
        icon = Icons.Default.SettingsInputComponent,
        requiresInput = false
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialFunctionsDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH

    val isExecuting by viewModel.isExecutingSpecialFunction.collectAsState()
    val specialLogs by viewModel.specialFunctionLog.collectAsState()

    var selectedFunction by remember { mutableStateOf(specialFunctionItems.first()) }
    var parameterInput by remember { mutableStateOf(selectedFunction.defaultInput) }

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
                            color = Color(0xFF00E676).copy(alpha = 0.15f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = Color(0xFF00C853),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isTh) "การลงทะเบียนอุปกรณ์ & รีเซ็ตระบบ (Special Functions)" else "Component Registration & Resets",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isTh) "ป้อนรหัสอุปกรณ์ใหม่ & Calibrate ค่ากล่อง ECU" else "Register new parts & adaptation relearn",
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

                // Main Container Split Layout
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(390.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Left Column: Special Functions List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(specialFunctionItems) { item ->
                            val isSelected = item.id == selectedFunction.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !isExecuting) {
                                        selectedFunction = item
                                        parameterInput = item.defaultInput
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Column {
                                        Text(
                                            text = if (isTh) item.titleTh else item.titleEn,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = item.category,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Right Column: Configuration & Execution Log
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (isTh) selectedFunction.titleTh else selectedFunction.titleEn,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = if (isTh) selectedFunction.descriptionTh else selectedFunction.descriptionEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )

                        // Parameter Input Field (if function requires user input e.g. IMA Code)
                        if (selectedFunction.requiresInput) {
                            OutlinedTextField(
                                value = parameterInput,
                                onValueChange = { parameterInput = it },
                                label = {
                                    Text(
                                        text = if (isTh) selectedFunction.inputLabelTh else selectedFunction.inputLabelEn,
                                        fontSize = 10.sp
                                    )
                                },
                                singleLine = true,
                                enabled = !isExecuting,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        // Execute Button
                        Button(
                            onClick = {
                                viewModel.runComponentRegistration(
                                    functionNameTh = selectedFunction.titleTh,
                                    functionNameEn = selectedFunction.titleEn,
                                    parameterData = if (selectedFunction.requiresInput) parameterInput else "N/A - System Re-learn"
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isExecuting,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                        ) {
                            if (isExecuting) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (isTh) "กำลังลงทะเบียน..." else "Registering...", fontSize = 12.sp)
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isTh) "เริ่มกระบวนการลงทะเบียน" else "Execute Registration", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // UDS Protocol Terminal Console
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
                                if (specialLogs.isEmpty()) {
                                    item {
                                        Text(
                                            text = if (isTh) "> พร้อมเริ่มกระบวนการลงทะเบียนกล่อง ECU..." else "> Ready for ECU Component Registration...",
                                            color = Color.Gray,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp
                                        )
                                    }
                                } else {
                                    items(specialLogs) { log ->
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
