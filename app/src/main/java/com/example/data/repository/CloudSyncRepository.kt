package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.MaintenanceLog
import com.example.data.model.ScanRecord
import com.example.data.model.TelemetrySnapshot
import com.example.data.model.Vehicle
import com.example.data.remote.OperationType
import com.example.data.remote.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class CloudSyncRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be authenticated before performing Cloud Sync.")
    }

    suspend fun saveUserProfile(displayName: String?, email: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val data = mapOf(
            "userId" to uid,
            "email" to email,
            "displayName" to (displayName ?: ""),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(data).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
        }
    }

    suspend fun saveVehicle(vehicle: Vehicle) {
        val uid = requireUserId()
        val vehicleId = if (vehicle.id > 0) vehicle.id.toString() else "veh_${System.currentTimeMillis()}"
        val docRef = db.collection("users").document(uid).collection("vehicles").document(vehicleId)
        val payload = mapOf(
            "userId" to uid,
            "make" to vehicle.make,
            "model" to vehicle.model,
            "year" to vehicle.year,
            "licensePlate" to vehicle.licensePlate,
            "vin" to vehicle.vin,
            "engine" to vehicle.engine,
            "currentMileageKm" to vehicle.currentMileageKm,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
        }
    }

    suspend fun saveScanRecord(record: ScanRecord) {
        val uid = requireUserId()
        val recordId = if (record.id > 0) record.id.toString() else "scan_${System.currentTimeMillis()}"
        val docRef = db.collection("users").document(uid).collection("scan_records").document(recordId)
        val payload = mapOf(
            "userId" to uid,
            "vehicleId" to record.vehicleId.toString(),
            "dtcCodes" to record.dtcCodes,
            "dtcCount" to record.dtcCount,
            "milStatus" to record.milStatus,
            "healthSummaryTh" to record.healthSummaryTh,
            "healthSummaryEn" to record.healthSummaryEn,
            "rpm" to record.rpm,
            "coolantTempC" to record.coolantTempC,
            "speedKmh" to record.speedKmh,
            "batteryVoltage" to record.batteryVoltage,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
        }
    }

    suspend fun saveMaintenanceLog(log: MaintenanceLog) {
        val uid = requireUserId()
        val logId = if (log.id > 0) log.id.toString() else "maint_${System.currentTimeMillis()}"
        val docRef = db.collection("users").document(uid).collection("maintenance_logs").document(logId)
        val payload = mapOf(
            "userId" to uid,
            "vehicleId" to log.vehicleId.toString(),
            "serviceTypeTh" to log.serviceTypeTh,
            "serviceTypeEn" to log.serviceTypeEn,
            "mileageAtServiceKm" to log.mileageAtServiceKm,
            "nextServiceMileageKm" to log.nextServiceMileageKm,
            "costThb" to log.costThb,
            "garageNameTh" to log.garageNameTh,
            "notes" to log.notes,
            "isCompleted" to log.isCompleted,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
        }
    }

    suspend fun saveTelemetrySnapshot(snapshot: TelemetrySnapshot) {
        val uid = requireUserId()
        val snapshotId = if (snapshot.id > 0) snapshot.id.toString() else "snap_${System.currentTimeMillis()}"
        val docRef = db.collection("users").document(uid).collection("telemetry_snapshots").document(snapshotId)
        val payload = mapOf(
            "userId" to uid,
            "vehicleName" to snapshot.vehicleName,
            "title" to snapshot.title,
            "reason" to snapshot.reason,
            "speedKmh" to snapshot.speedKmh,
            "rpm" to snapshot.rpm,
            "coolantTempC" to snapshot.coolantTempC,
            "intakeAirTempC" to snapshot.intakeAirTempC,
            "boostPressureBar" to snapshot.boostPressureBar,
            "mafGps" to snapshot.mafGps,
            "throttlePercent" to snapshot.throttlePercent,
            "batteryVoltage" to snapshot.batteryVoltage,
            "activeDtcs" to snapshot.activeDtcs,
            "notes" to snapshot.notes,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
        }
    }
}
