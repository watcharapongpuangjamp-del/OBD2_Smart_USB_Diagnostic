package com.example.data.model

enum class ThaiEngineCategory {
    DIESEL_COMMONRAIL_TURBO,
    GASOLINE_TURBO_DIRECT_INJECTION,
    GASOLINE_NATURALLY_ASPIRATED,
    HYBRID_HEV_PHEV,
    ELECTRIC_BEV
}

data class VehicleParameterBaseline(
    val idleRpmMin: Int = 650,
    val idleRpmMax: Int = 850,
    val normalCoolantTempMinC: Int = 82,
    val normalCoolantTempMaxC: Int = 98,
    val normalFuelRailPressureMinBar: Float = 250f,
    val normalFuelRailPressureMaxBar: Float = 1800f,
    val normalTurboBoostMaxBar: Float = 1.5f,
    val normalAtfTempMinC: Int = 70,
    val normalAtfTempMaxC: Int = 100,
    val normalBatteryVoltageMinV: Float = 13.5f,
    val normalBatteryVoltageMaxV: Float = 14.5f
)

data class VehicleDtcInterpretationInfo(
    val dtcCode: String,
    val carModelName: String,
    val vehicleSpecificCausesTh: List<String>,
    val vehicleSpecificCausesEn: List<String>,
    val specificComponentLocationTh: String,
    val specificComponentLocationEn: String,
    val severityWarningTh: String
)

data class VehicleFreezeFrameAnalysis(
    val vehicleName: String,
    val dtcCode: String,
    val isFuelPressureAnomaly: Boolean,
    val fuelPressureStatusTh: String,
    val isBoostPressureAnomaly: Boolean,
    val boostPressureStatusTh: String,
    val isCoolantTempAnomaly: Boolean,
    val coolantTempStatusTh: String,
    val isFuelTrimAnomaly: Boolean,
    val fuelTrimStatusTh: String,
    val overallDiagnosisTh: String,
    val overallDiagnosisEn: String
)

data class ThaiCarSpec(
    val id: String,
    val make: String,
    val model: String,
    val generationTh: String,
    val yearRange: String,
    val engineCode: String,
    val engineDisplacement: String,
    val horsepower: String,
    val fuelType: String,
    val engineCategory: ThaiEngineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
    val baseline: VehicleParameterBaseline = VehicleParameterBaseline(),
    val vinPrefixes: List<String>,
    val obdLocationTh: String,
    val obdLocationEn: String,
    val engineOilSpec: String,
    val oilCapacityLiters: Float,
    val transmissionFluidSpec: String,
    val coolantCapacityLiters: Float,
    val tirePressurePsi: String,
    val sparkPlugOrGlowSpec: String,
    val commonIssuesTh: List<String>,
    val commonIssuesEn: List<String>
)

