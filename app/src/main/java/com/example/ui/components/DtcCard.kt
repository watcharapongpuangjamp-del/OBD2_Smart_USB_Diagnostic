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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DtcCategory
import com.example.data.model.DtcCode
import com.example.data.model.DtcSeverity
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed

@Composable
fun DtcCodeCard(
    dtc: DtcCode,
    isThai: Boolean = true,
    onConsultAi: (DtcCode) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val (sevColor, sevTextTh, sevTextEn) = when (dtc.severity) {
        DtcSeverity.CRITICAL -> Triple(AutoRed, "อันตรายสูง (Critical)", "Critical")
        DtcSeverity.WARNING -> Triple(AutoAmber, "เตือนเช็คระยะ (Warning)", "Warning")
        DtcSeverity.INFO -> Triple(AutoCyan, "ข้อมูลทั่วไป (Info)", "Info")
    }

    val categoryBadge = when (dtc.category) {
        DtcCategory.POWERTRAIN -> "P (เครื่องยนต์/ส่งกำลัง)"
        DtcCategory.CHASSIS -> "C (ช่วงล่าง/ABS)"
        DtcCategory.BODY -> "B (ตัวถัง/Airbag)"
        DtcCategory.NETWORK -> "U (เครือข่าย CAN)"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
        ),
        border = BorderStroke(1.5.dp, sevColor.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(sevColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = dtc.code,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            ),
                            color = sevColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = categoryBadge,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isThai) sevTextTh else sevTextEn,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = sevColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // DTC Title
            Text(
                text = if (isThai) dtc.thTitle else dtc.enTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (isThai) dtc.thDescription else dtc.enDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Price and DIY info bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isThai) "ประเมินค่าซ่อม: " else "Est. Cost: ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = dtc.estimatedCostThb,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AutoGreen
                    )
                }

                Text(
                    text = dtc.diyDifficulty,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = AutoCyan
                )
            }

            // Expanded Details
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isThai) "🔍 สาเหตุที่พบบ่อย (Common Causes):" else "🔍 Common Causes:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AutoAmber
                    )
                    val causes = if (isThai) dtc.commonCausesTh else dtc.commonCausesEn
                    causes.forEach { cause ->
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Text(text = "• ", color = AutoAmber, style = MaterialTheme.typography.bodySmall)
                            Text(text = cause, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isThai) "⚠️ อาการที่เกิดขึ้น (Symptoms):" else "⚠️ Symptoms:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AutoCyan
                    )
                    val symptoms = if (isThai) dtc.symptomsTh else dtc.symptomsEn
                    symptoms.forEach { symptom ->
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Text(text = "• ", color = AutoCyan, style = MaterialTheme.typography.bodySmall)
                            Text(text = symptom, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    var showGuideDialog by remember { mutableStateOf(false) }

                    Button(
                        onClick = { showGuideDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AutoCyan
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isThai) "📋 คู่มือตรวจเช็กทีละขั้นตอน & มิเตอร์" else "📋 Step-by-Step Diagnostic & Multimeter Guide",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { onConsultAi(dtc) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AutoCyan
                        ),
                        border = BorderStroke(1.dp, AutoCyan)
                    ) {
                        Icon(imageVector = Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (isThai) "ปรึกษาช่าง AI สำหรับรหัสนี้" else "Consult AI Mechanic on this DTC")
                    }

                    if (showGuideDialog) {
                        val guide = com.example.data.repository.DtcGuideDatabase.getGuideForDtc(dtc.code)
                        if (guide != null) {
                            DtcDiagnosticGuideDialog(
                                guide = guide,
                                isThai = isThai,
                                onDismiss = { showGuideDialog = false }
                            )
                        }
                    }
                }
            }
        }
    }
}
