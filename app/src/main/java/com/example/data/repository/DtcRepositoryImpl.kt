package com.example.data.repository

import com.example.data.local.DtcDao
import com.example.data.model.DtcCategory
import com.example.data.model.DtcCode
import com.example.data.model.DtcSeverity
import com.example.domain.model.DiagnosticCode
import com.example.domain.model.DomainDtcCategory
import com.example.domain.model.DomainDtcSeverity
import com.example.domain.repository.DtcRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Data layer implementation of [DtcRepository] querying the local Room SQLite database.
 * Completely offline: provides instant lookup without requiring any internet connection.
 */
class DtcRepositoryImpl(
    private val dtcDao: DtcDao,
    private val seedCodes: List<DtcCode> = DtcDatabase.allCodes
) : DtcRepository {

    override fun searchDtcCodes(
        query: String,
        category: DomainDtcCategory?,
        severity: DomainDtcSeverity?
    ): Flow<List<DiagnosticCode>> {
        val baseFlow = if (query.isBlank()) {
            dtcDao.getAllDtcs()
        } else {
            dtcDao.searchDtcs(query)
        }

        return baseFlow.map { entities ->
            entities.filter { entity ->
                val matchesCategory = category == null || entity.category.toDomain() == category
                val matchesSeverity = severity == null || entity.severity.toDomain() == severity
                matchesCategory && matchesSeverity
            }.map { it.toDomain() }
        }
    }

    override fun getDtcByCode(code: String): Flow<DiagnosticCode?> {
        return dtcDao.getDtcByCode(code).map { it?.toDomain() }
    }

    override fun getAllDtcCodes(): Flow<List<DiagnosticCode>> {
        return dtcDao.getAllDtcs().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getDtcCount(): Int {
        return dtcDao.getCount()
    }

    override suspend fun ensureDatabaseSeeded(): Int {
        val currentCount = dtcDao.getCount()
        if (currentCount == 0 && seedCodes.isNotEmpty()) {
            dtcDao.insertAll(seedCodes)
            return dtcDao.getCount()
        }
        return currentCount
    }
}

// -------------------------------------------------------------
// Mapping Functions between Room Entity and Clean Domain Model
// -------------------------------------------------------------

fun DtcCode.toDomain(): DiagnosticCode {
    return DiagnosticCode(
        code = code,
        category = category.toDomain(),
        enTitle = enTitle,
        thTitle = thTitle,
        enDescription = enDescription,
        thDescription = thDescription,
        severity = severity.toDomain(),
        commonCausesTh = commonCausesTh,
        commonCausesEn = commonCausesEn,
        symptomsTh = symptomsTh,
        symptomsEn = symptomsEn,
        estimatedCostThb = estimatedCostThb,
        diyDifficulty = diyDifficulty,
        affectedSystem = affectedSystem
    )
}

fun DtcCategory.toDomain(): DomainDtcCategory {
    return when (this) {
        DtcCategory.POWERTRAIN -> DomainDtcCategory.POWERTRAIN
        DtcCategory.CHASSIS -> DomainDtcCategory.CHASSIS
        DtcCategory.BODY -> DomainDtcCategory.BODY
        DtcCategory.NETWORK -> DomainDtcCategory.NETWORK
    }
}

fun DomainDtcCategory.toEntity(): DtcCategory {
    return when (this) {
        DomainDtcCategory.POWERTRAIN -> DtcCategory.POWERTRAIN
        DomainDtcCategory.CHASSIS -> DtcCategory.CHASSIS
        DomainDtcCategory.BODY -> DtcCategory.BODY
        DomainDtcCategory.NETWORK -> DtcCategory.NETWORK
    }
}

fun DtcSeverity.toDomain(): DomainDtcSeverity {
    return when (this) {
        DtcSeverity.CRITICAL -> DomainDtcSeverity.CRITICAL
        DtcSeverity.WARNING -> DomainDtcSeverity.WARNING
        DtcSeverity.INFO -> DomainDtcSeverity.INFO
    }
}

fun DomainDtcSeverity.toEntity(): DtcSeverity {
    return when (this) {
        DomainDtcSeverity.CRITICAL -> DtcSeverity.CRITICAL
        DomainDtcSeverity.WARNING -> DtcSeverity.WARNING
        DomainDtcSeverity.INFO -> DtcSeverity.INFO
    }
}
