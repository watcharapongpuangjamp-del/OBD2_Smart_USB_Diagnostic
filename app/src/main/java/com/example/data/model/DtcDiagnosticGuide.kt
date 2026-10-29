package com.example.data.model

/**
 * Type of diagnostic inspection or test
 */
enum class DiagnosticStepType {
    VISUAL_MECHANICAL,
    MULTIMETER_ELECTRICAL,
    LIVE_PID_TELEMETRY,
    ACTUATOR_FUNCTIONAL_TEST,
    DRIVE_CYCLE_VERIFICATION
}

/**
 * Specific multimeter test details for sensor/actuator troubleshooting
 */
data class MultimeterTestDetail(
    val componentName: String,
    val harnessConnectorTh: String,
    val harnessConnectorEn: String,
    val testMode: String, // e.g. "DC Voltage (V)", "Resistance (Ω)", "Duty Cycle (%)"
    val redProbeLocationTh: String,
    val blackProbeLocationTh: String,
    val testConditionTh: String, // e.g. "Key ON Engine OFF (KOEO)", "เครื่องเดินเบา (Idle)", "ปลดปลั๊กวัดที่ตัวเซนเซอร์"
    val normalRange: String, // e.g. "0.5V - 4.5V", "1.5Ω - 2.8Ω at 20°C"
    val abnormalDiagnosisTh: String,
    val abnormalDiagnosisEn: String
)

/**
 * A single step in the troubleshooting workflow
 */
data class DiagnosticStep(
    val stepNumber: Int,
    val titleTh: String,
    val titleEn: String,
    val stepType: DiagnosticStepType,
    val descriptionTh: String,
    val descriptionEn: String,
    val multimeterTest: MultimeterTestDetail? = null,
    val pidName: String? = null,
    val pidExpectedValue: String? = null,
    val actionTipsTh: List<String> = emptyList()
)

/**
 * Comprehensive troubleshooting workflow guide for a specific DTC code
 */
data class DtcTroubleshootingGuide(
    val dtcCode: String,
    val titleTh: String,
    val titleEn: String,
    val severity: DtcSeverity,
    val affectedSystemTh: String,
    val requiredToolsTh: List<String>,
    val safetyPrecautionsTh: String,
    val steps: List<DiagnosticStep>,
    val thaiVehicleNotesTh: String,
    val postRepairDriveCycleTh: String
)
