package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreMaintenanceLog(
    @DocumentId val id: String = "",
    val userId: String = "",
    val vehicleId: String = "",
    val serviceName: String = "",
    val serviceType: String = "",
    val costThb: Int = 0,
    val mileageKm: Int = 0,
    val serviceDate: String = "",
    val garageNameTh: String = "",
    val dtcCodes: String = "",
    val notes: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
