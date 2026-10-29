package com.example.data.model

enum class OemPartCategory(
    val titleTh: String,
    val titleEn: String,
    val iconName: String
) {
    FILTERS_FLUIDS("ไส้กรอง & ของเหลว", "Filters & Fluids", "opacity"),
    ENGINE_SENSORS("เซนเซอร์เครื่องยนต์ & วาล์ว", "Engine Sensors & Valves", "electrical_services"),
    TURBO_FUEL_SYSTEM("ระบบจ่ายน้ำมัน & เทอร์โบ", "Fuel System & Turbo", "speed"),
    BRAKES_CHASSIS("ระบบเบรก & แชสซีส์", "Brakes & Chassis", "security"),
    IGNITION_ELECTRICAL("ระบบจุดระเบิด & ไฟฟ้า", "Ignition & Electrical", "build"),
    TRANSMISSION_DRIVETRAIN("ระบบเกียร์ & ส่งกำลัง", "Transmission & Drivetrain", "directions_car")
}

data class AftermarketPart(
    val brandName: String,
    val partNumber: String,
    val qualityLevelTh: String = "คุณภาพเทียบเท่า OEM"
)

data class OemPart(
    val id: String,
    val partNameTh: String,
    val partNameEn: String,
    val category: OemPartCategory,
    val oemPartNumber: String,
    val carBrand: String,
    val applicableModelsTh: List<String>,
    val applicableEngineCodes: List<String>,
    val aftermarketCrossReferences: List<AftermarketPart>,
    val estimatedPriceThb: String,
    val dtcCodesRelated: List<String> = emptyList(),
    val usageDescriptionTh: String
)
