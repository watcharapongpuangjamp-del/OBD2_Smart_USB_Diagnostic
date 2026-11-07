package com.example.presentation.screen.dtc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DiagnosticCode
import com.example.domain.model.DomainDtcCategory
import com.example.domain.model.DomainDtcSeverity
import com.example.presentation.common.components.CarbonCard
import com.example.presentation.common.components.CarbonDark
import com.example.presentation.common.components.CyberAmber
import com.example.presentation.common.components.CyberCyan
import com.example.presentation.common.components.CyberGreen
import com.example.presentation.common.components.CyberRed
import com.example.presentation.common.components.GlassmorphicCard
import com.example.presentation.common.components.TextMuted

@Composable
fun DtcSearchScreen(
    state: DtcSearchUiState,
    onEvent: (DtcSearchUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonDark)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("dtc_search_screen")
    ) {
        // Top Offline Database Status Pill
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF131D28),
            border = BorderStroke(1.dp, CyberGreen.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CyberGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (state.isThaiLanguage) "ฐานข้อมูลออฟไลน์ Room SQLite" else "Offline Room SQLite DB",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (state.isThaiLanguage) "ค้นหาได้ 100% โดยไม่ต้องเชื่อมต่อเน็ต" else "100% Offline Ready • No Internet Needed",
                            color = CyberGreen,
                            fontSize = 10.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CyberCyan.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "${state.totalDatabaseCount} รหัส",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // Search Input Bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { onEvent(DtcSearchUiEvent.SearchQueryChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dtc_search_input"),
            placeholder = {
                Text(
                    text = if (state.isThaiLanguage) "ค้นหารหัส เช่น P0300, หัวฉีด, ABS, CAN..." else "Search code e.g. P0300, Misfire, O2...",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = CyberCyan
                )
            },
            trailingIcon = {
                if (state.searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onEvent(DtcSearchUiEvent.SearchQueryChanged("")) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("btn_clear_search")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Search",
                            tint = Color.White
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CarbonCard,
                unfocusedContainerColor = CarbonCard,
                focusedBorderColor = CyberCyan,
                unfocusedBorderColor = Color(0x3300E5FF),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips Row
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = state.selectedCategory == null,
                    onClick = { onEvent(DtcSearchUiEvent.CategorySelected(null)) },
                    label = { Text(if (state.isThaiLanguage) "ทั้งหมด" else "All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberCyan.copy(alpha = 0.25f),
                        selectedLabelColor = CyberCyan,
                        containerColor = CarbonCard,
                        labelColor = TextMuted
                    ),
                    modifier = Modifier.testTag("dtc_chip_all")
                )
            }
            item {
                FilterChip(
                    selected = state.selectedCategory == DomainDtcCategory.POWERTRAIN,
                    onClick = { onEvent(DtcSearchUiEvent.CategorySelected(DomainDtcCategory.POWERTRAIN)) },
                    label = { Text("P - เครื่องยนต์") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberCyan.copy(alpha = 0.25f),
                        selectedLabelColor = CyberCyan,
                        containerColor = CarbonCard,
                        labelColor = TextMuted
                    ),
                    modifier = Modifier.testTag("dtc_chip_powertrain")
                )
            }
            item {
                FilterChip(
                    selected = state.selectedCategory == DomainDtcCategory.CHASSIS,
                    onClick = { onEvent(DtcSearchUiEvent.CategorySelected(DomainDtcCategory.CHASSIS)) },
                    label = { Text("C - แชสซี/ABS") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberAmber.copy(alpha = 0.25f),
                        selectedLabelColor = CyberAmber,
                        containerColor = CarbonCard,
                        labelColor = TextMuted
                    ),
                    modifier = Modifier.testTag("dtc_chip_chassis")
                )
            }
            item {
                FilterChip(
                    selected = state.selectedCategory == DomainDtcCategory.BODY,
                    onClick = { onEvent(DtcSearchUiEvent.CategorySelected(DomainDtcCategory.BODY)) },
                    label = { Text("B - ตัวถัง/แอร์") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFB388FF).copy(alpha = 0.25f),
                        selectedLabelColor = Color(0xFFB388FF),
                        containerColor = CarbonCard,
                        labelColor = TextMuted
                    ),
                    modifier = Modifier.testTag("dtc_chip_body")
                )
            }
            item {
                FilterChip(
                    selected = state.selectedCategory == DomainDtcCategory.NETWORK,
                    onClick = { onEvent(DtcSearchUiEvent.CategorySelected(DomainDtcCategory.NETWORK)) },
                    label = { Text("U - CAN Bus") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberGreen.copy(alpha = 0.25f),
                        selectedLabelColor = CyberGreen,
                        containerColor = CarbonCard,
                        labelColor = TextMuted
                    ),
                    modifier = Modifier.testTag("dtc_chip_network")
                )
            }
        }

        // Severity Sub-Filters & Result Counter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (state.selectedSeverity == DomainDtcSeverity.CRITICAL) CyberRed.copy(alpha = 0.25f) else Color.Transparent,
                    border = BorderStroke(1.dp, if (state.selectedSeverity == DomainDtcSeverity.CRITICAL) CyberRed else Color(0x33FFFFFF)),
                    modifier = Modifier
                        .clickable { onEvent(DtcSearchUiEvent.SeveritySelected(DomainDtcSeverity.CRITICAL)) }
                        .testTag("dtc_filter_critical")
                ) {
                    Text(
                        text = "วิกฤต (Critical)",
                        color = if (state.selectedSeverity == DomainDtcSeverity.CRITICAL) CyberRed else TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (state.selectedSeverity == DomainDtcSeverity.WARNING) CyberAmber.copy(alpha = 0.25f) else Color.Transparent,
                    border = BorderStroke(1.dp, if (state.selectedSeverity == DomainDtcSeverity.WARNING) CyberAmber else Color(0x33FFFFFF)),
                    modifier = Modifier
                        .clickable { onEvent(DtcSearchUiEvent.SeveritySelected(DomainDtcSeverity.WARNING)) }
                        .testTag("dtc_filter_warning")
                ) {
                    Text(
                        text = "เตือน (Warning)",
                        color = if (state.selectedSeverity == DomainDtcSeverity.WARNING) CyberAmber else TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "พบ ${state.filteredCount} รหัส",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Results List
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CyberCyan)
            }
        } else if (state.dtcList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (state.isThaiLanguage) "ไม่พบรหัสที่ตรงกับคำค้นหา" else "No matching trouble codes found",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = if (state.isThaiLanguage) "ลองค้นหาด้วยรหัสอื่น หรือกดรีเซ็ตตัวกรอง" else "Try another keyword or reset filters",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onEvent(DtcSearchUiEvent.ClearFilters) },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_reset_filters")
                    ) {
                        Text(
                            text = if (state.isThaiLanguage) "ล้างการค้นหา" else "Reset Search",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                items(state.dtcList, key = { it.code }) { dtc ->
                    DtcListItemCard(
                        dtc = dtc,
                        isThai = state.isThaiLanguage,
                        onClick = { onEvent(DtcSearchUiEvent.DtcClicked(dtc)) }
                    )
                }
            }
        }
    }

    // Detail Dialog
    state.selectedDtc?.let { dtc ->
        DtcDetailDialog(
            dtc = dtc,
            isThaiLanguage = state.isThaiLanguage,
            onDismiss = { onEvent(DtcSearchUiEvent.DismissDetail) }
        )
    }
}

@Composable
fun DtcListItemCard(
    dtc: DiagnosticCode,
    isThai: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val severityColor = when (dtc.severity) {
        DomainDtcSeverity.CRITICAL -> CyberRed
        DomainDtcSeverity.WARNING -> CyberAmber
        DomainDtcSeverity.INFO -> CyberCyan
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("dtc_item_card_${dtc.code}"),
        color = CarbonCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, severityColor.copy(alpha = 0.35f))
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = severityColor.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, severityColor)
                    ) {
                        Text(
                            text = dtc.code,
                            color = severityColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = when (dtc.category) {
                            DomainDtcCategory.POWERTRAIN -> "Powertrain (P)"
                            DomainDtcCategory.CHASSIS -> "Chassis (C)"
                            DomainDtcCategory.BODY -> "Body (B)"
                            DomainDtcCategory.NETWORK -> "Network (U)"
                        },
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = severityColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = when (dtc.severity) {
                            DomainDtcSeverity.CRITICAL -> "CRITICAL"
                            DomainDtcSeverity.WARNING -> "WARNING"
                            DomainDtcSeverity.INFO -> "INFO"
                        },
                        color = severityColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isThai) dtc.thTitle else dtc.enTitle,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            if (isThai && dtc.enTitle.isNotBlank()) {
                Text(
                    text = dtc.enTitle,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dtc.affectedSystem,
                    color = CyberCyan,
                    fontSize = 11.sp,
                    maxLines = 1
                )

                if (dtc.estimatedCostThb.isNotBlank()) {
                    Text(
                        text = "ค่าซ่อม: ${dtc.estimatedCostThb}",
                        color = CyberGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
