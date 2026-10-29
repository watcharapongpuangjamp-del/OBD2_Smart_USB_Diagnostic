package com.example.domain.repository

import com.example.data.model.Vehicle
import kotlinx.coroutines.flow.Flow

/**
 * Domain boundary interface for Vehicle profile management and OEM specifications.
 */
interface VehicleRepository {

    /**
     * Flow observing currently selected vehicle.
     */
    fun observeCurrentVehicle(): Flow<Vehicle?>

    /**
     * Set active vehicle by id.
     */
    suspend fun setActiveVehicle(vehicleId: Long)

    /**
     * Save or update vehicle profile.
     */
    suspend fun saveVehicle(vehicle: Vehicle): Long

    /**
     * Get all registered vehicles.
     */
    fun getAllVehicles(): Flow<List<Vehicle>>
}
