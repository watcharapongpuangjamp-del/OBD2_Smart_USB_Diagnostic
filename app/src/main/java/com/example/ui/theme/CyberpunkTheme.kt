package com.example.ui.theme

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.GaugeFontPreset
import com.example.data.model.NeonAccentPreset
import com.example.data.model.UiCustomizationState

@Composable
fun getGaugeFontFamily(preset: GaugeFontPreset): FontFamily {
    val context = LocalContext.current
    return remember(preset) {
        val resName = preset.fontResName
        if (resName != null) {
            val resId = context.resources.getIdentifier(resName, "font", context.packageName)
            if (resId != 0) FontFamily(Font(resId, FontWeight.Normal)) else FontFamily.Monospace
        } else {
            FontFamily.Default
        }
    }
}

fun Modifier.neonGlow(
    color: Color,
    radius: Dp = 12.dp,
    alpha: Float = 0.5f,
    shapeRadius: Dp = 16.dp
): Modifier = this.drawBehind {
    val transparentColor = color.copy(alpha = 0f).toArgb()
    val glowColor = color.copy(alpha = alpha).toArgb()

    val paint = Paint()
    val frameworkPaint = paint.asFrameworkPaint()
    frameworkPaint.color = transparentColor
    frameworkPaint.setShadowLayer(
        radius.toPx(),
        0f,
        0f,
        glowColor
    )

    drawIntoCanvas { canvas ->
        canvas.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            shapeRadius.toPx(),
            shapeRadius.toPx(),
            paint
        )
    }
}

fun parseHexColor(hex: String, fallback: Color): Color {
    return try {
        val cleaned = hex.removePrefix("#")
        val colorInt = if (cleaned.length == 6) {
            (0xFF000000 or cleaned.toLong(16)).toInt()
        } else if (cleaned.length == 8) {
            cleaned.toLong(16).toInt()
        } else {
            return fallback
        }
        Color(colorInt)
    } catch (e: Exception) {
        fallback
    }
}

@Composable
fun CyberThemeWrapper(
    customizationState: UiCustomizationState,
    content: @Composable () -> Unit
) {
    val preset = customizationState.preset
    val primaryColor = if (preset == NeonAccentPreset.CUSTOM_RGB) {
        parseHexColor(customizationState.customPrimaryColorHex, preset.primaryColor)
    } else {
        preset.primaryColor
    }

    val secondaryColor = if (preset == NeonAccentPreset.CUSTOM_RGB) {
        parseHexColor(customizationState.customSecondaryColorHex, preset.secondaryColor)
    } else {
        preset.secondaryColor
    }

    val cyberColorScheme = darkColorScheme(
        primary = primaryColor,
        onPrimary = Color(0xFF0B0E14),
        primaryContainer = primaryColor.copy(alpha = 0.2f),
        onPrimaryContainer = primaryColor,
        secondary = secondaryColor,
        onSecondary = Color(0xFF0B0E14),
        secondaryContainer = secondaryColor.copy(alpha = 0.2f),
        onSecondaryContainer = secondaryColor,
        tertiary = preset.accentColor,
        onTertiary = Color(0xFF0B0E14),
        background = Color(0xFF0B0E14),
        onBackground = Color(0xFFF0F6FC),
        surface = Color(0xFF121824),
        onSurface = Color(0xFFF0F6FC),
        surfaceVariant = Color(0xFF1A2332),
        onSurfaceVariant = Color(0xFF8B949E),
        outline = primaryColor.copy(alpha = 0.4f)
    )

    MaterialTheme(
        colorScheme = cyberColorScheme,
        typography = Typography,
        content = content
    )
}
