package com.example.domain.usecase

import com.example.domain.model.SensorReading
import com.example.domain.repository.ObdDiagnosticRepository
import kotlinx.coroutines.flow.Flow

/**
 * Single-responsibility UseCase to stream live vehicle telemetry data.
 */
class StreamLiveTelemetryUseCase(
    private val obdRepository: ObdDiagnosticRepository
) {
    operator fun invoke(): Flow<List<SensorReading>> {
        return obdRepository.observeLiveTelemetry()
    }
}
