package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class NeonAccentPreset(
    val titleTh: String,
    val titleEn: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color
) {
    NEON_MATRIX(
        titleTh = "Cyber Matrix Green",
        titleEn = "Cyber Matrix Green",
        primaryColor = Color(0xFF00FF66),
        secondaryColor = Color(0xFF00E5FF),
        accentColor = Color(0xFFFF9E00)
    ),
    ELECTRIC_CYAN(
        titleTh = "Electric Cyan",
        titleEn = "Electric Cyan",
        primaryColor = Color(0xFF00E5FF),
        secondaryColor = Color(0xFF2979FF),
        accentColor = Color(0xFF00FF66)
    ),
    CYBER_RACING(
        titleTh = "Cyber Racing Amber/Red",
        titleEn = "Cyber Racing Amber/Red",
        primaryColor = Color(0xFFFF9E00),
        secondaryColor = Color(0xFFFF0055),
        accentColor = Color(0xFF00E5FF)
    ),
    SYNTH_SUNSET(
        titleTh = "Synthwave Purple/Pink",
        titleEn = "Synthwave Purple/Pink",
        primaryColor = Color(0xFFE040FB),
        secondaryColor = Color(0xFFFF007F),
        accentColor = Color(0xFF00E5FF)
    ),
    CUSTOM_RGB(
        titleTh = "กำหนดสีเอง (Custom RGB)",
        titleEn = "Custom RGB Palette",
        primaryColor = Color(0xFF00FF66),
        secondaryColor = Color(0xFF00E5FF),
        accentColor = Color(0xFFFF9E00)
    )
}

enum class GaugeFontPreset(
    val titleTh: String,
    val titleEn: String,
    val fontResName: String?
) {
    SHARE_TECH_MONO("Share Tech Mono (Sci-Fi HUD)", "Share Tech Mono (Sci-Fi HUD)", "share_tech_mono"),
    ROBOTO_MONO("Roboto Mono (Digital Tech)", "Roboto Mono (Digital Tech)", "roboto_mono"),
    SYSTEM_DEFAULT("มาตรฐานระบบ (System Default)", "System Default", null)
}

data class UiCustomizationState(
    val preset: NeonAccentPreset = NeonAccentPreset.NEON_MATRIX,
    val customPrimaryColorHex: String = "#00FF66",
    val customSecondaryColorHex: String = "#00E5FF",
    val gaugeFont: GaugeFontPreset = GaugeFontPreset.SHARE_TECH_MONO,
    val gaugeFontScale: Float = 1.0f,
    val glowIntensity: Float = 0.8f,
    val widgetGridOrder: List<String> = listOf("rpm", "speed", "boost", "coolant", "fuel_press", "voltage"),
    val hiddenWidgetIds: Set<String> = emptySet()
)
