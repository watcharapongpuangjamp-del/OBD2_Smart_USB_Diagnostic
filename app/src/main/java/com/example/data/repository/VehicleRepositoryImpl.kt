package com.example.data.repository

import com.example.data.local.VehicleDao
import com.example.data.model.Vehicle
import com.example.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow

/**
 * Concrete implementation of [VehicleRepository] delegating to Room [VehicleDao].
 */
class VehicleRepositoryImpl(
    private val vehicleDao: VehicleDao
) : VehicleRepository {

    override fun observeCurrentVehicle(): Flow<Vehicle?> {
        return vehicleDao.getSelectedVehicle()
    }

    override suspend fun setActiveVehicle(vehicleId: Long) {
        vehicleDao.clearSelection()
        vehicleDao.setSelected(vehicleId)
    }

    override suspend fun saveVehicle(vehicle: Vehicle): Long {
        return vehicleDao.insertVehicle(vehicle)
    }

    override fun getAllVehicles(): Flow<List<Vehicle>> {
        return vehicleDao.getAllVehicles()
    }
}
