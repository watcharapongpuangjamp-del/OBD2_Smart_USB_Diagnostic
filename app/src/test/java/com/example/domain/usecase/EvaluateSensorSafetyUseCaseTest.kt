package com.example.domain.usecase

import com.example.domain.model.SensorReading
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EvaluateSensorSafetyUseCaseTest {

    private lateinit var evaluateSensorSafetyUseCase: EvaluateSensorSafetyUseCase

    @Before
    fun setUp() {
        evaluateSensorSafetyUseCase = EvaluateSensorSafetyUseCase()
    }

    @Test
    fun `evaluateSensorSafety returns safe when all sensors within safe bounds`() {
        val normalSensors = listOf(
            SensorReading(
                id = "coolant_temp",
                displayNameTh = "อุณหภูมิน้ำหล่อเย็น",
                displayNameEn = "Coolant Temp",
                value = 88f,
                unit = "°C",
                minSafeValue = 75f,
                maxSafeValue = 102f
            ),
            SensorReading(
                id = "battery_voltage",
                displayNameTh = "แรงดันไฟแบตเตอรี่",
                displayNameEn = "Battery Voltage",
                value = 14.1f,
                unit = "V",
                minSafeValue = 12.0f,
                maxSafeValue = 14.8f
            )
        )

        val result = evaluateSensorSafetyUseCase(normalSensors)

        assertTrue(result.isSafe)
        assertTrue(result.criticalAlerts.isEmpty())
        assertTrue(result.warningAlerts.isEmpty())
    }

    @Test
    fun `evaluateSensorSafety detects critical overheating coolant temperature`() {
        val overheatedSensors = listOf(
            SensorReading(
                id = "coolant_temp",
                displayNameTh = "อุณหภูมิน้ำหล่อเย็น",
                displayNameEn = "Coolant Temp",
                value = 112f,
                unit = "°C",
                minSafeValue = 75f,
                maxSafeValue = 102f
            )
        )

        val result = evaluateSensorSafetyUseCase(overheatedSensors)

        assertFalse(result.isSafe)
        assertEquals(1, result.criticalAlerts.size)
        assertTrue(result.criticalAlerts[0].contains("อุณหภูมิน้ำหล่อเย็นสูงวิกฤต"))
    }

    @Test
    fun `evaluateSensorSafety detects low battery voltage warning`() {
        val weakBatterySensors = listOf(
            SensorReading(
                id = "battery_voltage",
                displayNameTh = "แรงดันไฟแบตเตอรี่",
                displayNameEn = "Battery Voltage",
                value = 11.9f,
                unit = "V",
                minSafeValue = 12.0f,
                maxSafeValue = 14.8f
            )
        )

        val result = evaluateSensorSafetyUseCase(weakBatterySensors)

        assertTrue(result.isSafe)
        assertEquals(1, result.warningAlerts.size)
        assertTrue(result.warningAlerts[0].contains("แบตเตอรี่เริ่มอ่อน"))
    }
}
