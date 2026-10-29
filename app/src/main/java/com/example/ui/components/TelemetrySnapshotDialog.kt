package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.ui.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TelemetrySnapshotDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH
    val snapshots by viewModel.telemetrySnapshots.collectAsState()

    var showNewSnapshotForm by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isTh) "ภาพถ่ายข้อมูลระบบ (Telemetry Snapshots)" else "Telemetry Snapshots",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                HorizontalDivider()

                if (showNewSnapshotForm) {
                    NewSnapshotForm(
                        isTh = isTh,
                        onSave = { title, reason, notes ->
                            viewModel.captureTelemetrySnapshot(title, reason, notes)
                            showNewSnapshotForm = false
                        },
                        onCancel = { showNewSnapshotForm = false }
                    )
                } else {
                    Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isTh) "บันทึกข้อมูลล่าสุด" else "Recent Snapshots",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Button(onClick = { showNewSnapshotForm = true }) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (isTh) "สร้าง Snapshot ใหม่" else "New Snapshot")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if (snapshots.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(if (isTh) "ยังไม่มีข้อมูล Snapshot" else "No snapshots found")
                            }
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(snapshots) { snap ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(snap.title, fontWeight = FontWeight.Bold)
                                                Row {
                                                    val context = androidx.compose.ui.platform.LocalContext.current
                                                    IconButton(onClick = {
                                                        val csvData = """
                                                            Title,${snap.title}
                                                            Date,${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(snap.timestamp))}
                                                            Vehicle,${snap.vehicleName}
                                                            Reason,${snap.reason}
                                                            Speed (km/h),${snap.speedKmh}
                                                            RPM,${snap.rpm}
                                                            Coolant Temp (°C),${snap.coolantTempC}
                                                            Intake Air Temp (°C),${snap.intakeAirTempC}
                                                            Boost (Bar),${snap.boostPressureBar}
                                                            MAF (g/s),${snap.mafGps}
                                                            Throttle (%),${snap.throttlePercent}
                                                            Battery (V),${snap.batteryVoltage}
                                                            DTCs,${snap.activeDtcs}
                                                            Notes,${snap.notes}
                                                        """.trimIndent()
                                                        
                                                        val sendIntent: android.content.Intent = android.content.Intent().apply {
                                                            action = android.content.Intent.ACTION_SEND
                                                            putExtra(android.content.Intent.EXTRA_TEXT, csvData)
                                                            type = "text/csv"
                                                        }
                                                        val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                                                        context.startActivity(shareIntent)
                                                    }, modifier = Modifier.size(24.dp)) {
                                                        Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.primary)
                                                    }
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    IconButton(onClick = { viewModel.deleteSnapshot(snap.id) }, modifier = Modifier.size(24.dp)) {
                                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                                    }
                                                }
                                            }
                                            val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(snap.timestamp))
                                            Text(text = dateStr, style = MaterialTheme.typography.bodySmall)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = "Vehicle: ${snap.vehicleName}", style = MaterialTheme.typography.bodySmall)
                                            Text(text = "Speed: ${snap.speedKmh} km/h | RPM: ${snap.rpm}", style = MaterialTheme.typography.bodySmall)
                                            Text(text = "Coolant: ${snap.coolantTempC}°C | Batt: ${snap.batteryVoltage}V", style = MaterialTheme.typography.bodySmall)
                                            if (snap.activeDtcs.isNotEmpty()) {
                                                Text(text = "DTCs: ${snap.activeDtcs}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (snapshots.isNotEmpty()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center) {
                            TextButton(onClick = { viewModel.clearAllSnapshots() }) {
                                Text(if (isTh) "ลบทั้งหมด" else "Clear All", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewSnapshotForm(isTh: Boolean, onSave: (String, String, String) -> Unit, onCancel: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(if (isTh) "ชื่อ Snapshot" else "Snapshot Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text(if (isTh) "สาเหตุที่บันทึก" else "Reason for Capture") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text(if (isTh) "หมายเหตุ" else "Additional Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onCancel) {
                Text(if (isTh) "ยกเลิก" else "Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onSave(title.ifEmpty { "Manual Snapshot" }, reason, notes) }) {
                Text(if (isTh) "บันทึกข้อมูล" else "Save Snapshot")
            }
        }
    }
}
