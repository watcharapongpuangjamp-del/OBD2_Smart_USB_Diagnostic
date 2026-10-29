package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class HudColorTheme(
    val titleTh: String,
    val titleEn: String,
    val primaryColor: Color,
    val accentColor: Color
) {
    NEON_GREEN(
        titleTh = "เขียวนีออน (Neon Green)",
        titleEn = "Neon Green",
        primaryColor = Color(0xFF00FF66),
        accentColor = Color(0xFF00E5FF)
    ),
    CYBER_CYAN(
        titleTh = "ฟ้าไซเบอร์ (Cyber Cyan)",
        titleEn = "Cyber Cyan",
        primaryColor = Color(0xFF00E5FF),
        accentColor = Color(0xFF76FF03)
    ),
    SOLAR_AMBER(
        titleTh = "เหลืองส้ม (Solar Amber)",
        titleEn = "Solar Amber",
        primaryColor = Color(0xFFFFD600),
        accentColor = Color(0xFFFFAB00)
    ),
    CRISP_WHITE(
        titleTh = "ขาวสว่าง (Crisp White)",
        titleEn = "Crisp White",
        primaryColor = Color(0xFFFFFFFF),
        accentColor = Color(0xFF80D8FF)
    ),
    RACING_RED(
        titleTh = "แดงเรซซิ่ง (Racing Red)",
        titleEn = "Racing Red",
        primaryColor = Color(0xFFFF1744),
        accentColor = Color(0xFFFF9100)
    )
}

enum class HudLayoutMode(
    val titleTh: String,
    val titleEn: String
) {
    MINIMAL_SPEED("เน้นความเร็วใหญ่ (Minimalist)", "Speed Focus"),
    DASHBOARD_TRIO("3 เซนเซอร์หลัก (Digital Trio)", "Standard Trio"),
    FULL_TELEMETRY("แดชบอร์ดซูเปอร์สปอร์ต (Sport Gauge)", "Full Telemetry")
}
