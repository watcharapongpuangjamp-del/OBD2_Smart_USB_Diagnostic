package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.DtcDao
import com.example.data.model.DtcCategory
import com.example.data.model.DtcCode
import com.example.data.model.DtcSeverity
import com.example.data.repository.DtcDatabase
import com.example.data.repository.DtcRepositoryImpl
import com.example.domain.model.DomainDtcCategory
import com.example.domain.model.DomainDtcSeverity
import com.example.domain.usecase.GetDtcDetailsUseCase
import com.example.domain.usecase.SearchDtcUseCase
import com.example.domain.usecase.SeedDtcDatabaseUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class DtcOfflineDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var dtcDao: DtcDao
    private lateinit var repository: DtcRepositoryImpl
    private lateinit var searchDtcUseCase: SearchDtcUseCase
    private lateinit var getDtcDetailsUseCase: GetDtcDetailsUseCase
    private lateinit var seedDtcDatabaseUseCase: SeedDtcDatabaseUseCase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dtcDao = database.dtcDao()

        repository = DtcRepositoryImpl(
            dtcDao = dtcDao,
            seedCodes = DtcDatabase.allCodes
        )
        searchDtcUseCase = SearchDtcUseCase(repository)
        getDtcDetailsUseCase = GetDtcDetailsUseCase(repository)
        seedDtcDatabaseUseCase = SeedDtcDatabaseUseCase(repository)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSeedDatabaseAndVerifyCount() = runBlocking {
        assertEquals(0, dtcDao.getCount())

        val seededCount = seedDtcDatabaseUseCase()
        assertTrue("Seeded count should be at least 20", seededCount >= 20)
        assertEquals(seededCount, dtcDao.getCount())
    }

    @Test
    fun testSearchByCodeQuery() = runBlocking {
        seedDtcDatabaseUseCase()

        val results = searchDtcUseCase(query = "P0171").first()
        assertTrue("Results for P0171 should not be empty", results.isNotEmpty())
        assertEquals("P0171", results.first().code)
        assertEquals(DomainDtcCategory.POWERTRAIN, results.first().category)
        assertTrue(results.first().thTitle.contains("บางเกินไป"))
    }

    @Test
    fun testSearchByThaiKeyword() = runBlocking {
        seedDtcDatabaseUseCase()

        val results = searchDtcUseCase(query = "คอมมอนเรล").first()
        assertTrue("Results searching for 'คอมมอนเรล' should return codes", results.isNotEmpty())
        assertTrue(results.any { it.code == "P0087" })
    }

    @Test
    fun testFilterByCategoryPowertrain() = runBlocking {
        seedDtcDatabaseUseCase()

        val pCodes = searchDtcUseCase(
            query = "",
            category = DomainDtcCategory.POWERTRAIN
        ).first()

        assertTrue("Powertrain codes should not be empty", pCodes.isNotEmpty())
        assertTrue("All returned codes should start with P", pCodes.all { it.code.startsWith("P") })
    }

    @Test
    fun testFilterByCategoryChassis() = runBlocking {
        seedDtcDatabaseUseCase()

        val cCodes = searchDtcUseCase(
            query = "",
            category = DomainDtcCategory.CHASSIS
        ).first()

        assertTrue("Chassis codes should be returned if present", cCodes.all { it.category == DomainDtcCategory.CHASSIS })
    }

    @Test
    fun testFilterBySeverityCritical() = runBlocking {
        seedDtcDatabaseUseCase()

        val criticalCodes = searchDtcUseCase(
            query = "",
            severity = DomainDtcSeverity.CRITICAL
        ).first()

        assertTrue("Critical codes should be returned", criticalCodes.isNotEmpty())
        assertTrue("All returned items must have CRITICAL severity", criticalCodes.all { it.severity == DomainDtcSeverity.CRITICAL })
    }

    @Test
    fun testGetDtcDetailsByCode() = runBlocking {
        seedDtcDatabaseUseCase()

        val dtc = getDtcDetailsUseCase("P0087").first()
        assertNotNull("DTC P0087 should exist", dtc)
        assertEquals("P0087", dtc?.code)
        assertEquals(DomainDtcSeverity.CRITICAL, dtc?.severity)
        assertTrue("Should contain symptoms", dtc?.symptomsTh?.isNotEmpty() == true)
        assertTrue("Should contain causes", dtc?.commonCausesTh?.isNotEmpty() == true)
    }
}
