package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AutoCyan

@Composable
fun AddMaintenanceDialog(
    currentVehicleMileage: Int,
    isThai: Boolean = true,
    onAdd: (typeTh: String, typeEn: String, mileage: Int, nextMileage: Int, cost: Double, garage: String, notes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var typeTh by remember { mutableStateOf("เปลี่ยนน้ำมันเกียร์ออโต้ + ไส้กรองเกียร์") }
    var typeEn by remember { mutableStateOf("Transmission Fluid & Filter Replacement") }
    var mileageStr by remember { mutableStateOf(currentVehicleMileage.toString()) }
    var nextMileageStr by remember { mutableStateOf((currentVehicleMileage + 40000).toString()) }
    var costStr by remember { mutableStateOf("2800") }
    var garage by remember { mutableStateOf("ศูนย์บริการมาตรฐาน / อู่ช่างเอก") }
    var notes by remember { mutableStateOf("ใช้น้ำมันเกียร์มาตรฐานตรงสเปกศูนย์") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isThai) "บันทึกประวัติการซ่อมบำรุง" else "Log Service Record",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = typeTh,
                    onValueChange = { typeTh = it },
                    label = { Text(if (isThai) "รายการซ่อมบำรุง (ไทย)" else "Service Item (TH)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = typeEn,
                    onValueChange = { typeEn = it },
                    label = { Text(if (isThai) "Service Item (English)" else "Service Item (EN)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = mileageStr,
                        onValueChange = { mileageStr = it },
                        label = { Text(if (isThai) "เลขไมล์ที่เปลี่ยน (กม.)" else "Service Mileage") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = nextMileageStr,
                        onValueChange = { nextMileageStr = it },
                        label = { Text(if (isThai) "เช็คครั้งถัดไป (กม.)" else "Next Due (km)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = costStr,
                        onValueChange = { costStr = it },
                        label = { Text(if (isThai) "ค่าใช้จ่าย (บาท THB)" else "Cost (THB)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = garage,
                        onValueChange = { garage = it },
                        label = { Text(if (isThai) "ศูนย์/อู่ที่ซ่อม" else "Garage / Workshop") },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(if (isThai) "บันทึกเพิ่มเติม / อะไหล่" else "Notes / Spare Parts") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val mil = mileageStr.toIntOrNull() ?: currentVehicleMileage
                        val nextMil = nextMileageStr.toIntOrNull() ?: (mil + 10000)
                        val cost = costStr.toDoubleOrNull() ?: 0.0
                        onAdd(typeTh, typeEn, mil, nextMil, cost, garage, notes)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AutoCyan)
                ) {
                    Text(
                        text = if (isThai) "บันทึกประวัติการเช็คระยะ" else "Save Service Log",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
