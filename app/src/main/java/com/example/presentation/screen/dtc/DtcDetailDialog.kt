package com.example.presentation.screen.dtc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.DiagnosticCode
import com.example.domain.model.DomainDtcCategory
import com.example.domain.model.DomainDtcSeverity
import com.example.presentation.common.components.CarbonCard
import com.example.presentation.common.components.CyberAmber
import com.example.presentation.common.components.CyberCyan
import com.example.presentation.common.components.CyberGreen
import com.example.presentation.common.components.CyberRed
import com.example.presentation.common.components.GlassmorphicCard
import com.example.presentation.common.components.TextMuted

@Composable
fun DtcDetailDialog(
    dtc: DiagnosticCode,
    isThaiLanguage: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showThai by remember { mutableStateOf(isThaiLanguage) }

    val severityColor = when (dtc.severity) {
        DomainDtcSeverity.CRITICAL -> CyberRed
        DomainDtcSeverity.WARNING -> CyberAmber
        DomainDtcSeverity.INFO -> CyberCyan
    }

    val categoryLabel = when (dtc.category) {
        DomainDtcCategory.POWERTRAIN -> if (showThai) "P - เครื่องยนต์และระบบส่งกำลัง" else "P - Powertrain"
        DomainDtcCategory.CHASSIS -> if (showThai) "C - แชสซี ระบบเบรก และช่วงล่าง" else "C - Chassis & ABS"
        DomainDtcCategory.BODY -> if (showThai) "B - ตัวถัง ระบบแอร์ และถุงลมนิรภัย" else "B - Body & SRS"
        DomainDtcCategory.NETWORK -> if (showThai) "U - เครือข่ายการสื่อสาร CAN Bus" else "U - Network & CAN Bus"
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, severityColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .testTag("dtc_detail_dialog"),
            color = CarbonCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = severityColor.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, severityColor)
                        ) {
                            Text(
                                text = dtc.code,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = severityColor,
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (dtc.severity) {
                                DomainDtcSeverity.CRITICAL -> "CRITICAL"
                                DomainDtcSeverity.WARNING -> "WARNING"
                                DomainDtcSeverity.INFO -> "INFO"
                            },
                            color = severityColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showThai = !showThai },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("btn_toggle_dialog_lang")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Toggle Language",
                                tint = CyberCyan
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("btn_close_dtc_detail")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title & Category
                Text(
                    text = if (showThai) dtc.thTitle else dtc.enTitle,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = categoryLabel,
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                if (dtc.affectedSystem.isNotBlank()) {
                    Text(
                        text = "ระบบที่เกี่ยวข้อง: ${dtc.affectedSystem}",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = Color(0x22FFFFFF))
                Spacer(modifier = Modifier.height(14.dp))

                // Description
                Text(
                    text = if (showThai) "คำอธิบายอาการ / รายละเอียด:" else "Description:",
                    color = CyberCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (showThai) dtc.thDescription else dtc.enDescription,
                    color = Color(0xFFE0E0E0),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                // Symptoms
                val symptoms = if (showThai) dtc.symptomsTh else dtc.symptomsEn
                if (symptoms.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (showThai) "อาการที่สังเกตได้ (Symptoms):" else "Symptoms:",
                        color = CyberAmber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    symptoms.forEach { symptom ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = CyberAmber,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = symptom,
                                color = Color(0xFFDDDDDD),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Causes
                val causes = if (showThai) dtc.commonCausesTh else dtc.commonCausesEn
                if (causes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (showThai) "สาเหตุที่พบบ่อย (Common Causes):" else "Common Causes:",
                        color = CyberGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    causes.forEach { cause ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CyberGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = cause,
                                color = Color(0xFFDDDDDD),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Repair Estimates & DIY Difficulty
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (dtc.estimatedCostThb.isNotBlank()) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2836))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = CyberGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (showThai) "ราคาซ่อมโดยประมาณ" else "Estimated Cost",
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = dtc.estimatedCostThb,
                                    color = CyberGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    if (dtc.diyDifficulty.isNotBlank()) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2836))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Build,
                                        contentDescription = null,
                                        tint = CyberCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (showThai) "ความยากในการซ่อม" else "DIY Difficulty",
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = dtc.diyDifficulty,
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_dismiss_dtc_dialog"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (showThai) "เข้าใจแล้ว ปิดหน้าต่าง" else "Got It, Close",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
