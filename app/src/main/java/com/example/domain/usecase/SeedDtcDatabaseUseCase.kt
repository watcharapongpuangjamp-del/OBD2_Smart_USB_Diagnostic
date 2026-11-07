package com.example.domain.usecase

import com.example.domain.repository.DtcRepository

/**
 * UseCase to initialize and verify that the local Room SQLite database contains offline DTC records.
 */
class SeedDtcDatabaseUseCase(
    private val repository: DtcRepository
) {
    suspend operator fun invoke(): Int {
        return repository.ensureDatabaseSeeded()
    }
}
