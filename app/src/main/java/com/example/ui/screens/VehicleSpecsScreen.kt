package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.MaintenanceLog
import com.example.data.model.ThaiCarSpec
import com.example.data.repository.ThaiCarDatabase
import com.example.ui.MainViewModel
import com.example.ui.components.AddMaintenanceDialog
import com.example.ui.components.VinScannerDialog
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed

@Composable
fun VehicleSpecsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isThai by viewModel.language.collectAsState()
    val isTh = isThai == AppLanguage.TH
    val vinInput by viewModel.vinInput.collectAsState()
    val decodedVin by viewModel.decodedVinResult.collectAsState()
    val selectedSpec by viewModel.selectedCarSpec.collectAsState()
    val selectedVehicle by viewModel.selectedVehicle.collectAsState()
    val maintenanceLogs by viewModel.maintenanceLogs.collectAsState()
    val isCloudSyncing by viewModel.isCloudSyncing.collectAsState()
    val cloudSyncStatus by viewModel.cloudSyncStatus.collectAsState()

    var showVinScanner by remember { mutableStateOf(false) }
    var showAddMaintenanceDialog by remember { mutableStateOf(false) }

    if (showVinScanner) {
        VinScannerDialog(
            initialVin = vinInput,
            isThai = isTh,
            onVinScanned = { scannedVin ->
                viewModel.setVinInput(scannedVin)
            },
            onDismiss = { showVinScanner = false }
        )
    }

    if (showAddMaintenanceDialog) {
        AddMaintenanceDialog(
            currentVehicleMileage = selectedVehicle?.currentMileageKm ?: 68500,
            isThai = isTh,
            onAdd = { typeTh, typeEn, mileage, nextMileage, cost, garage, notes ->
                viewModel.addMaintenanceLog(typeTh, typeEn, mileage, nextMileage, cost, garage, notes)
                showAddMaintenanceDialog = false
            },
            onDismiss = { showAddMaintenanceDialog = false }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // VIN Decoder & Scanner Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, AutoCyan.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isTh) "สแกน VIN & ลงทะเบียนรถยนต์" else "VIN Scanning & Vehicle Registration",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isTh) "ถอดรหัสตัวถังอัตโนมัติ & เก็บประวัติซ่อมลงฐานข้อมูลคลาวด์" else "Auto-decode VIN & sync service records to Cloud DB",
                                style = MaterialTheme.typography.labelSmall,
                                color = AutoCyan
                            )
                        }

                        Button(
                            onClick = { showVinScanner = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                        ) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (isTh) "สแกนกล้อง" else "Scan VIN", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = vinInput,
                        onValueChange = { viewModel.setVinInput(it.uppercase()) },
                        label = { Text(if (isTh) "ป้อนหมายเลขตัวถัง VIN 17 หลัก" else "Enter 17-digit VIN") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Decoded Details & Quick Register Button
                    if (decodedVin.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "ผู้ผลิต / แบรนด์:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = decodedVin["make"] ?: "Unknown",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AutoCyan
                                )
                            }
                            Column {
                                Text(text = "ประเทศผู้ผลิต:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = decodedVin["country"] ?: "Thailand",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Column {
                                Text(text = "ปีรุ่น (Year):", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = decodedVin["modelYear"] ?: "2021",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AutoGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val make = decodedVin["make"] ?: "Toyota"
                                val year = (decodedVin["modelYear"] ?: "2021").toIntOrNull() ?: 2021
                                viewModel.addNewVehicle(
                                    make = make,
                                    model = "Vehicle-$year",
                                    year = year,
                                    engine = "Engine Standard",
                                    plate = "กข 1234",
                                    province = "กรุงเทพมหานคร",
                                    vin = vinInput,
                                    mileage = 68500,
                                    fuel = "ดีเซล (Diesel)"
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AutoGreen)
                        ) {
                            Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (isTh) "บันทึกลงทะเบียนรถคันนี้เข้าอู่ (Register Car)" else "Register Vehicle into Cloud DB", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Secure Cloud-Based Database & Repair History Manager
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, AutoGreen.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(AutoGreen.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isCloudSyncing) Icons.Default.CloudSync else Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = AutoGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isTh) "ฐานข้อมูลประวัติซ่อมบนคลาวด์ (Cloud History DB)" else "Cloud Service History Database",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = cloudSyncStatus,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = AutoGreen
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.syncDatabaseWithCloud() },
                            enabled = !isCloudSyncing,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AutoGreen)
                        ) {
                            if (isCloudSyncing) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text(text = if (isTh) "ซิงค์คลาวด์" else "Sync", fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isTh) "ประวัติการซ่อมบำรุง (${maintenanceLogs.size} รายการ):" else "Logged Service Records (${maintenanceLogs.size}):",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Button(
                            onClick = { showAddMaintenanceDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isTh) "เพิ่มบันทึกซ่อม" else "Log Service", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (maintenanceLogs.isEmpty()) {
                        Text(
                            text = if (isTh) "ยังไม่มีประวัติการซ่อม กดปุ่ม 'เพิ่มบันทึกซ่อม' เพื่อบันทึก" else "No service records yet. Tap 'Log Service' to add.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        maintenanceLogs.forEach { log ->
                            ServiceLogItemCard(log = log, isTh = isTh, onDelete = { viewModel.deleteMaintenanceLog(log) })
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }

        // Popular Thai Car Selector Chips & Search
        item {
            var vehicleSearchQuery by remember { mutableStateOf("") }
            var selectedBrandFilter by remember { mutableStateOf("All") }

            val brands = remember {
                listOf("All", "Toyota", "Honda", "Isuzu", "Mitsubishi", "Ford", "Nissan", "Mazda", "MG", "Suzuki", "Chevrolet", "Hyundai", "BMW", "BYD", "GWM", "Subaru")
            }

            val filteredCars = remember(vehicleSearchQuery, selectedBrandFilter) {
                ThaiCarDatabase.popularThaiCars.filter { car ->
                    val matchesBrand = selectedBrandFilter == "All" || car.make.equals(selectedBrandFilter, ignoreCase = true)
                    val matchesQuery = vehicleSearchQuery.isEmpty() ||
                            car.make.contains(vehicleSearchQuery, ignoreCase = true) ||
                            car.model.contains(vehicleSearchQuery, ignoreCase = true) ||
                            car.engineCode.contains(vehicleSearchQuery, ignoreCase = true)
                    matchesBrand && matchesQuery
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (isTh) "ค้นหาข้อมูลเทคนิคตามรุ่นรถในไทย (${ThaiCarDatabase.popularThaiCars.size} รุ่น):" else "Search Technical Specs by Thai Models (${ThaiCarDatabase.popularThaiCars.size} Models):",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = vehicleSearchQuery,
                    onValueChange = { vehicleSearchQuery = it },
                    label = { Text(if (isTh) "พิมพ์ค้นหารุ่นรถ (เช่น Revo, Civic, D-Max, BYD, 1.0 Turbo)" else "Search Model or Engine (e.g., Revo, Civic, D-Max)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(brands) { brand ->
                        val isBrandSel = brand == selectedBrandFilter
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isBrandSel) AutoCyan else MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedBrandFilter = brand }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = brand,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isBrandSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredCars) { car ->
                        val isSel = car.id == selectedSpec?.id
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSel) AutoCyan.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.selectCarSpec(car) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = car.make,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (isSel) AutoCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = car.model.split("/").first().trim(),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Technical Specs Detail View
        selectedSpec?.let { car ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Title
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${car.make} ${car.model}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${car.generationTh} • ${car.yearRange}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AutoCyan
                                )
                            }
                        }

                        // OBD Port Location (Highlighted)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AutoAmber.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = AutoAmber,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isTh) "📍 ตำแหน่งพอร์ต OBD-II ในห้องโดยสาร:" else "📍 OBD-II Port Cabin Location:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = AutoAmber
                                    )
                                    Text(
                                        text = if (isTh) car.obdLocationTh else car.obdLocationEn,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }

                        // Specs Table
                        SpecRowItem(label = if (isTh) "รหัสเครื่องยนต์ & ความจุ" else "Engine & Displacement", value = "${car.engineCode} (${car.engineDisplacement})")
                        SpecRowItem(label = if (isTh) "กำลังสูงสุด / แรงบิด" else "Power & Torque", value = car.horsepower)
                        SpecRowItem(label = if (isTh) "ชนิดเชื้อเพลิง" else "Fuel Specification", value = car.fuelType)
                        SpecRowItem(label = if (isTh) "เกรดน้ำมันเครื่อง & ความจุ" else "Engine Oil Spec & Capacity", value = "${car.engineOilSpec} (~${car.oilCapacityLiters} ลิตร)")
                        SpecRowItem(label = if (isTh) "น้ำมันเกียร์ & หล่อเย็น" else "Transmission & Coolant", value = "${car.transmissionFluidSpec} (หล่อเย็น ~${car.coolantCapacityLiters}L)")
                        SpecRowItem(label = if (isTh) "แรงดันลมยางมาตรฐาน (PSI)" else "Recommended Tire Pressure", value = car.tirePressurePsi)
                        SpecRowItem(label = if (isTh) "หัวเทียน / หัวเผา" else "Spark / Glow Plugs", value = car.sparkPlugOrGlowSpec)

                        // Common Thailand Road Issues
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isTh) "⚠️ ปัญหาและอาการประจำรุ่นที่พบบ่อยในไทย:" else "⚠️ Common Regional Issues in Thailand:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AutoRed
                        )

                        val issues = if (isTh) car.commonIssuesTh else car.commonIssuesEn
                        issues.forEach { issue ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Text(text = "• ", color = AutoRed, style = MaterialTheme.typography.bodySmall)
                                Text(text = issue, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun ServiceLogItemCard(
    log: MaintenanceLog,
    isTh: Boolean,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isTh) log.serviceTypeTh else log.serviceTypeEn,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            val dateStr = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(log.serviceDate))
            Text(
                text = "${log.mileageAtServiceKm} km • $dateStr • ${log.garageNameTh}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (log.notes.isNotEmpty()) {
                Text(
                    text = "หมายเหตุ: ${log.notes}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = AutoCyan
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "฿${log.costThb.toInt()}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AutoGreen
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = AutoRed, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun SpecRowItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.3f)
        )
    }
}
