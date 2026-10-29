package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.FirestoreMaintenanceLog
import com.example.data.remote.OperationType
import com.example.data.remote.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class MaintenanceHistoryRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be authenticated before accessing maintenance history.")
    }

    /**
     * Observes real-time maintenance history logs for the authenticated user.
     * Optionally filters by vehicleId if provided.
     */
    fun observeMaintenanceLogs(vehicleId: String? = null): Flow<List<FirestoreMaintenanceLog>> {
        val uid = requireUserId()
        val logsCollection = db.collection("users").document(uid).collection("maintenance_logs")
        val query: Query = if (!vehicleId.isNullOrBlank()) {
            logsCollection.whereEqualTo("vehicleId", vehicleId)
        } else {
            logsCollection
        }

        return query.snapshots().map { snapshot ->
            snapshot.toObjects(
                FirestoreMaintenanceLog::class.java,
                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
            )
        }.catch { error ->
            if (error is Exception) {
                handleFirestoreError(error, OperationType.LIST, "users/$uid/maintenance_logs")
            }
            throw error
        }
    }

    /**
     * Retrieves a single maintenance log document by ID.
     */
    suspend fun getMaintenanceLog(logId: String): FirestoreMaintenanceLog? {
        val uid = requireUserId()
        require(logId.isNotBlank()) { "Log ID must not be blank" }
        val docRef = db.collection("users").document(uid).collection("maintenance_logs").document(logId)

        try {
            val snapshot = docRef.get().await()
            return snapshot.toObject(
                FirestoreMaintenanceLog::class.java,
                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            throw e
        }
    }

    /**
     * Creates a new maintenance log document under /users/{userId}/maintenance_logs/{logId}
     */
    suspend fun addMaintenanceLog(log: FirestoreMaintenanceLog): String {
        val uid = requireUserId()
        val logsCollection = db.collection("users").document(uid).collection("maintenance_logs")
        val docRef = if (log.id.isNotBlank()) logsCollection.document(log.id) else logsCollection.document()

        val payload = mapOf(
            "userId" to uid,
            "vehicleId" to log.vehicleId,
            "serviceName" to log.serviceName,
            "serviceType" to log.serviceType,
            "costThb" to log.costThb,
            "mileageKm" to log.mileageKm,
            "serviceDate" to log.serviceDate,
            "garageNameTh" to log.garageNameTh,
            "dtcCodes" to log.dtcCodes,
            "notes" to log.notes,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        try {
            docRef.set(payload).await()
            return docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    /**
     * Updates an existing maintenance log document.
     */
    suspend fun updateMaintenanceLog(log: FirestoreMaintenanceLog) {
        val uid = requireUserId()
        require(log.id.isNotBlank()) { "Maintenance log ID must not be blank for update." }
        val docRef = db.collection("users").document(uid).collection("maintenance_logs").document(log.id)

        val payload = mapOf(
            "userId" to uid,
            "vehicleId" to log.vehicleId,
            "serviceName" to log.serviceName,
            "serviceType" to log.serviceType,
            "costThb" to log.costThb,
            "mileageKm" to log.mileageKm,
            "serviceDate" to log.serviceDate,
            "garageNameTh" to log.garageNameTh,
            "dtcCodes" to log.dtcCodes,
            "notes" to log.notes,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        try {
            docRef.update(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    /**
     * Deletes a maintenance log document by ID.
     */
    suspend fun deleteMaintenanceLog(logId: String) {
        val uid = requireUserId()
        require(logId.isNotBlank()) { "Maintenance log ID must not be blank for deletion." }
        val docRef = db.collection("users").document(uid).collection("maintenance_logs").document(logId)

        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }
}
