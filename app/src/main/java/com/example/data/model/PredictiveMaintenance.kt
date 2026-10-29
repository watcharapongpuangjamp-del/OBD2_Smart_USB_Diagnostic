package com.example.data.model

enum class PredictiveStatus {
    HEALTHY,           // Green (85 - 100%): Component operating in optimal parameters
    MONITORING,        // Blue (70 - 84%): Normal wear, periodic check recommended
    ATTENTION,         // Amber (45 - 69%): Early wear signs detected, plan service
    IMMINENT_FAILURE   // Red (< 45%): High risk of breakdown within 500-1,500 km or 14-30 days
}

enum class UrgencyLevel {
    NORMAL,
    PLANNED_SERVICE,
    HIGH_PRIORITY,
    CRITICAL_IMMEDIATE
}

data class PredictiveComponent(
    val id: String,
    val nameTh: String,
    val nameEn: String,
    val system: DiagnosticSystem,
    val healthScore: Int, // 0 - 100%
    val status: PredictiveStatus,
    val failureRiskProbability: Float, // 0.0 to 1.0 (e.g. 0.88 = 88% probability)
    val estimatedRemainingKm: Int, // Estimated km before breakdown/failure
    val estimatedRemainingDays: Int, // Estimated days based on avg Thai daily commute (35 km/day)
    val monitoredSensors: List<String>, // Relevant live sensor readings
    val reasonTh: String,
    val reasonEn: String,
    val recommendationTh: String,
    val recommendationEn: String,
    val estimatedPartCostThb: String,
    val urgencyLevel: UrgencyLevel,
    val isImminentAlert: Boolean = false
)

data class PredictiveVehicleHealthSummary(
    val overallHealthScore: Int,
    val riskCategoryTh: String,
    val riskCategoryEn: String,
    val activeAlertCount: Int,
    val imminentFailureCount: Int,
    val components: List<PredictiveComponent>,
    val lastAnalysisTimestamp: Long,
    val mileageAnalyzedKm: Int,
    val drivingStressFactorTh: String,
    val drivingStressFactorEn: String
)
