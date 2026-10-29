package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.ui.graphics.vector.ImageVector

data class ShortcutFeature(
    val id: String,
    val titleTh: String,
    val titleEn: String,
    val descriptionTh: String,
    val descriptionEn: String,
    val iconName: String,
    val targetTab: Int, // Navigation tab (0..5) or dialog code
    val categoryId: String,
    val subcategoryId: String,
    val isDefaultFavorite: Boolean = false,
    val badgeTextTh: String? = null,
    val badgeTextEn: String? = null
)

data class FeatureCategory(
    val id: String,
    val titleTh: String,
    val titleEn: String,
    val descriptionTh: String,
    val descriptionEn: String,
    val iconName: String,
    val subcategories: List<FeatureSubcategory>
)

data class FeatureSubcategory(
    val id: String,
    val titleTh: String,
    val titleEn: String,
    val features: List<ShortcutFeature>
)

object ShortcutFeatureRegistry {

    val ALL_FEATURES = listOf(
        // Diagnostic Category
        ShortcutFeature(
            id = "dtc_scan",
            titleTh = "สแกนรหัสความผิดปกติ (DTC)",
            titleEn = "DTC Fault Code Scan",
            descriptionTh = "อ่านค่า P/C/B/U codes พร้อมคำแนะนำวิธีแก้ตามรุ่นรถ",
            descriptionEn = "Read P/C/B/U codes with model-specific repair guides",
            iconName = "qr_code_scanner",
            targetTab = 1,
            categoryId = "diag",
            subcategoryId = "diag_engine",
            isDefaultFavorite = true,
            badgeTextTh = "ยอดนิยม",
            badgeTextEn = "Popular"
        ),
        ShortcutFeature(
            id = "freeze_frame",
            titleTh = "บันทึกข้อมูลขณะเสีย (Freeze Frame)",
            titleEn = "Freeze Frame Telemetry",
            descriptionTh = "วิเคราะห์ค่าเซนเซอร์ขณะกล่อง ECU จับ Fault Code",
            descriptionEn = "Snapshot telemetry when DTC was triggered",
            iconName = "hourglass_top",
            targetTab = 1,
            categoryId = "diag",
            subcategoryId = "diag_engine"
        ),
        ShortcutFeature(
            id = "active_test",
            titleTh = "สั่งการทดสอบอุปกรณ์ (Active Test)",
            titleEn = "Active Actuator Test",
            descriptionTh = "ทดสอบการทำงานของพัดลมหม้อน้ำ, ปั๊มเชื้อเพลิง, หัวฉีด",
            descriptionEn = "Trigger radiator fan, fuel pump, injectors directly",
            iconName = "build",
            targetTab = 1,
            categoryId = "diag",
            subcategoryId = "diag_engine",
            badgeTextTh = "PRO",
            badgeTextEn = "PRO"
        ),
        ShortcutFeature(
            id = "special_functions",
            titleTh = "ฟังก์ชันพิเศษ / รีเซ็ตระบบ (Special Functions)",
            titleEn = "Special Functions & Resets",
            descriptionTh = "เรียนรู้ค่าปั๊ม SCV, รีเซ็ตไฟเตือนกรองโซล่า, DPF regen",
            descriptionEn = "SCV relearn, fuel filter reset, DPF regeneration",
            iconName = "electrical_services",
            targetTab = 1,
            categoryId = "diag",
            subcategoryId = "diag_body"
        ),

        // Live Telemetry Category
        ShortcutFeature(
            id = "live_telemetry",
            titleTh = "มาตรวัดสด (Live Gauges)",
            titleEn = "Live Gauges & Telemetry",
            descriptionTh = "มาตรวัดความเร็ว รอบเครื่อง บูสต์เทอร์โบ แรงดันรางดีเซล",
            descriptionEn = "Digital Speedometer, Tachometer, Boost & Commonrail pressure",
            iconName = "speed",
            targetTab = 3,
            categoryId = "telemetry",
            subcategoryId = "tel_gauges",
            isDefaultFavorite = true,
            badgeTextTh = "Real-time",
            badgeTextEn = "Real-time"
        ),
        ShortcutFeature(
            id = "hud_display",
            titleTh = "โหมดสะท้อนกระจกหน้า (HUD Mode)",
            titleEn = "Head-Up Display Mode",
            descriptionTh = "กลับหน้าจอสำหรับวางบนคอนโซลสะท้อนกระจกหน้าตอนกลางคืน",
            descriptionEn = "Mirrored display mode for windshield reflection at night",
            iconName = "speed",
            targetTab = 3,
            categoryId = "telemetry",
            subcategoryId = "tel_gauges"
        ),
        ShortcutFeature(
            id = "oem_baseline",
            titleTh = "เปรียบเทียบค่ามาตรฐาน OEM",
            titleEn = "OEM Baseline Comparison",
            descriptionTh = "เทียบเซนเซอร์ปัจจุบันกับค่ามาตรฐานโรงงานของรถแต่ละรุ่น",
            descriptionEn = "Benchmark current telemetry against factory specifications",
            iconName = "security",
            targetTab = 3,
            categoryId = "telemetry",
            subcategoryId = "tel_pressure"
        ),

        // Maintenance & Specs Category
        ShortcutFeature(
            id = "predictive_health",
            titleTh = "คาดการณ์สุขภาพรถ (Predictive AI)",
            titleEn = "Predictive Vehicle Health",
            descriptionTh = "คำนวณเปอร์เซ็นต์ความเสี่ยงการเสื่อมของแบตเตอรี่ หัวฉีด เทอร์โบ",
            descriptionEn = "Predictive wear risk scores for battery, injectors, turbo",
            iconName = "psychology",
            targetTab = 2,
            categoryId = "maintenance",
            subcategoryId = "maint_schedule",
            isDefaultFavorite = true,
            badgeTextTh = "AI Score",
            badgeTextEn = "AI Score"
        ),
        ShortcutFeature(
            id = "service_schedule",
            titleTh = "ตารางเช็คระยะ & บันทึกซ่อม",
            titleEn = "Maintenance & Service Log",
            descriptionTh = "แจ้งเตือนเช็คระยะน้ำมันเครื่อง กรองโซล่า ผ้าเบรก สายพาน",
            descriptionEn = "Service interval alerts for engine oil, fuel filter, brakes",
            iconName = "history",
            targetTab = 5,
            categoryId = "maintenance",
            subcategoryId = "maint_schedule"
        ),
        ShortcutFeature(
            id = "thai_car_specs",
            titleTh = "ข้อมูลสเปกรถยนต์ในไทย",
            titleEn = "Thai Vehicle Specs Database",
            descriptionTh = "เบอร์น้ำมันเครื่อง ความจุ ตำแหน่งปลั๊ก OBD รหัสเครื่องยนต์",
            descriptionEn = "Engine codes, oil viscosity specs, OBD port location in Thai cars",
            iconName = "directions_car",
            targetTab = 5,
            categoryId = "maintenance",
            subcategoryId = "maint_specs",
            isDefaultFavorite = true,
            badgeTextTh = "ไทย 2026",
            badgeTextEn = "TH 2026"
        ),

        // AI & System
        ShortcutFeature(
            id = "ai_mechanic",
            titleTh = "ช่างวิเคราะห์อัจฉริยะ (AI Mechanic)",
            titleEn = "AI Mechanic Assistant",
            descriptionTh = "ปรึกษาอาการเสีย วินิจฉัยเสียงแปลกๆ และขั้นตอนการซ่อม",
            descriptionEn = "Consult abnormal sounds, DTC troubleshooting with AI",
            iconName = "auto_awesome",
            targetTab = 4,
            categoryId = "ai",
            subcategoryId = "ai_chat",
            isDefaultFavorite = true,
            badgeTextTh = "Gemini",
            badgeTextEn = "Gemini"
        ),
        ShortcutFeature(
            id = "usb_terminal",
            titleTh = "OBD2 USB Terminal & Logs",
            titleEn = "OBD2 Terminal & Commands",
            descriptionTh = "ส่งคำสั่ง AT/ST Direct CAN Bus และดู Raw Hex Data Log",
            descriptionEn = "Send direct AT/ST CAN commands and view hex logs",
            iconName = "terminal",
            targetTab = 0,
            categoryId = "ai",
            subcategoryId = "ai_terminal"
        )
    )

    val CATEGORIES = listOf(
        FeatureCategory(
            id = "diag",
            titleTh = "🚗 ระบบวิเคราะห์และสแกนความผิดปกติ",
            titleEn = "🚗 Diagnostic & Fault Scanning",
            descriptionTh = "อ่านและลบโค้ด DTC, ดู Freeze Frame, และสั่งทดสอบอุปกรณ์",
            descriptionEn = "Read/clear DTC codes, analyze Freeze Frames, active testing",
            iconName = "qr_code_scanner",
            subcategories = listOf(
                FeatureSubcategory(
                    id = "diag_engine",
                    titleTh = "ระบบเครื่องยนต์ & ส่งกำลัง (Engine & TCM)",
                    titleEn = "Engine & Powertrain (ECM/TCM)",
                    features = ALL_FEATURES.filter { it.subcategoryId == "diag_engine" }
                ),
                FeatureSubcategory(
                    id = "diag_body",
                    titleTh = "เบรก แชสซีส์ & ไฟฟ้าตัวถัง (ABS/BCM)",
                    titleEn = "Chassis & Body Electrical (ABS/BCM)",
                    features = ALL_FEATURES.filter { it.subcategoryId == "diag_body" }
                )
            )
        ),
        FeatureCategory(
            id = "telemetry",
            titleTh = "⚡ เซนเซอร์และมาตรวัดสด (Telemetry)",
            titleEn = "⚡ Sensors & Live Telemetry",
            descriptionTh = "มาตรวัดดิจิทัล โหมด HUD สะท้อนกระจก และเทียบค่า OEM",
            descriptionEn = "Digital gauges, HUD windshield mode, OEM baseline benchmarks",
            iconName = "speed",
            subcategories = listOf(
                FeatureSubcategory(
                    id = "tel_gauges",
                    titleTh = "มาตรวัดสด & โหมด HUD (Gauges & HUD)",
                    titleEn = "Gauges & HUD Displays",
                    features = ALL_FEATURES.filter { it.subcategoryId == "tel_gauges" }
                ),
                FeatureSubcategory(
                    id = "tel_pressure",
                    titleTh = "วิเคราะห์แรงดัน & OEM Baseline",
                    titleEn = "Pressure Sensors & OEM Benchmarks",
                    features = ALL_FEATURES.filter { it.subcategoryId == "tel_pressure" }
                )
            )
        ),
        FeatureCategory(
            id = "maintenance",
            titleTh = "🛠️ การบำรุงรักษาและสเปกรถยนต์",
            titleEn = "🛠️ Maintenance & Vehicle Specifications",
            descriptionTh = "เช็คระยะ บันทึกประวัติซ่อม คาดการณ์อะไหล่ และสเปกรถไทย",
            descriptionEn = "Service intervals, maintenance log, wear prediction, Thai specs",
            iconName = "build",
            subcategories = listOf(
                FeatureSubcategory(
                    id = "maint_schedule",
                    titleTh = "คาดการณ์อะไหล่ & ตารางเช็คระยะ",
                    titleEn = "Predictive Wear & Service Schedule",
                    features = ALL_FEATURES.filter { it.subcategoryId == "maint_schedule" }
                ),
                FeatureSubcategory(
                    id = "maint_specs",
                    titleTh = "ฐานข้อมูลสเปกรถยนต์ในประเทศไทย",
                    titleEn = "Thai Vehicles Specs & Fluid Grades",
                    features = ALL_FEATURES.filter { it.subcategoryId == "maint_specs" }
                )
            )
        ),
        FeatureCategory(
            id = "ai",
            titleTh = "🤖 ช่างวิเคราะห์อัจฉริยะ & เทอร์มินัล",
            titleEn = "🤖 AI Mechanic & System Terminal",
            descriptionTh = "ปรึกษา AI Assistant และส่งคำสั่ง OBD2 Direct CAN Command",
            descriptionEn = "Consult AI Assistant & execute direct OBD2 CAN commands",
            iconName = "auto_awesome",
            subcategories = listOf(
                FeatureSubcategory(
                    id = "ai_chat",
                    titleTh = "ผู้ช่วยช่าง AI (AI Mechanic Chat)",
                    titleEn = "AI Mechanic Assistant Chat",
                    features = ALL_FEATURES.filter { it.subcategoryId == "ai_chat" }
                ),
                FeatureSubcategory(
                    id = "ai_terminal",
                    titleTh = "เทอร์มินัลคำสั่ง & เครื่องมือขั้นสูง",
                    titleEn = "Command Terminal & Advanced Tools",
                    features = ALL_FEATURES.filter { it.subcategoryId == "ai_terminal" }
                )
            )
        )
    )
}
