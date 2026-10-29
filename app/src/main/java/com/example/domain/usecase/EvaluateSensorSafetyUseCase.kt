package com.example.domain.usecase

import com.example.domain.model.SensorReading

/**
 * Domain evaluation result regarding engine safety thresholds.
 */
data class SafetyAssessment(
    val isSafe: Boolean,
    val criticalAlerts: List<String> = emptyList(),
    val warningAlerts: List<String> = emptyList()
)

/**
 * Single-responsibility UseCase to evaluate live telemetry against automotive safety specifications.
 */
class EvaluateSensorSafetyUseCase {

    operator fun invoke(sensors: List<SensorReading>): SafetyAssessment {
        val criticals = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        for (sensor in sensors) {
            when (sensor.id) {
                "coolant_temp" -> {
                    if (sensor.value >= 108f) {
                        criticals.add("อุณหภูมิน้ำหล่อเย็นสูงวิกฤต (${sensor.value.toInt()}°C) เสี่ยงเครื่องยนต์ฮีต!")
                    } else if (sensor.value >= 98f) {
                        warnings.add("อุณหภูมิน้ำหล่อเย็นเริ่มสูงกว่าปกติ (${sensor.value.toInt()}°C)")
                    }
                }
                "battery_voltage" -> {
                    if (sensor.value < 11.5f && sensor.value > 0f) {
                        criticals.add("แรงดันไฟแบตเตอรี่ต่ำผิดปกติ (${sensor.value}V) เสี่ยงสตาร์ทไม่ติด!")
                    } else if (sensor.value < 12.2f && sensor.value > 0f) {
                        warnings.add("แบตเตอรี่เริ่มอ่อน (${sensor.value}V)")
                    }
                }
                "fuel_rail_pressure" -> {
                    if (sensor.value > 2000f) {
                        criticals.add("แรงดันรางคอมมอนเรลสูงผิดปกติ (${sensor.value.toInt()} Bar)")
                    }
                }
            }
        }

        return SafetyAssessment(
            isSafe = criticals.isEmpty(),
            criticalAlerts = criticals,
            warningAlerts = warnings
        )
    }
}
