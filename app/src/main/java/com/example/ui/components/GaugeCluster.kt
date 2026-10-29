package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.AutoRed
import com.example.ui.theme.GaugeTrack
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedometerGauge(
    speedKmh: Int,
    maxSpeed: Int = 220,
    fontFamily: FontFamily = FontFamily.Monospace,
    fontScale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    val animatedSpeed by animateFloatAsState(
        targetValue = speedKmh.toFloat(),
        animationSpec = tween(durationMillis = 300),
        label = "SpeedAnim"
    )

    val progress = (animatedSpeed / maxSpeed).coerceIn(0f, 1f)

    Box(
        modifier = modifier.size(175.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(170.dp)) {
            val startAngle = 135f
            val sweepAngle = 270f
            val strokeWidth = 14.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // Background Track
            drawArc(
                color = GaugeTrack,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Gradient Active Arc
            val activeColor = when {
                speedKmh > 120 -> AutoRed
                speedKmh > 90 -> AutoAmber
                else -> AutoCyan
            }

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(AutoCyan, AutoAmber, AutoRed),
                    center = center
                ),
                startAngle = startAngle,
                sweepAngle = sweepAngle * progress,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Needle indicator line
            val currentAngleRad = Math.toRadians((startAngle + sweepAngle * progress).toDouble())
            val needleEnd = Offset(
                (center.x + (radius - 8.dp.toPx()) * cos(currentAngleRad)).toFloat(),
                (center.y + (radius - 8.dp.toPx()) * sin(currentAngleRad)).toFloat()
            )
            drawLine(
                color = Color.White,
                start = center,
                end = needleEnd,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(color = Color.White, radius = 5.dp.toPx(), center = center)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$speedKmh",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = (36 * fontScale).sp,
                    fontFamily = fontFamily
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "KM/H",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    fontFamily = fontFamily
                ),
                color = AutoCyan
            )
        }
    }
}

@Composable
fun TachometerGauge(
    rpm: Int,
    maxRpm: Int = 6000,
    fontFamily: FontFamily = FontFamily.Monospace,
    fontScale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    val animatedRpm by animateFloatAsState(
        targetValue = rpm.toFloat(),
        animationSpec = tween(durationMillis = 300),
        label = "RpmAnim"
    )

    val progress = (animatedRpm / maxRpm).coerceIn(0f, 1f)

    Box(
        modifier = modifier.size(175.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(170.dp)) {
            val startAngle = 135f
            val sweepAngle = 270f
            val strokeWidth = 14.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // Background Track
            drawArc(
                color = GaugeTrack,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Redline indicator zone on track (4500+ RPM)
            val redlineStart = startAngle + sweepAngle * (4500f / maxRpm)
            val redlineSweep = sweepAngle * ((maxRpm - 4500f) / maxRpm)
            drawArc(
                color = AutoRed.copy(alpha = 0.35f),
                startAngle = redlineStart,
                sweepAngle = redlineSweep,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Gradient Active Arc
            val activeColor = when {
                rpm > 4500 -> AutoRed
                rpm > 3000 -> AutoAmber
                else -> AutoGreen
            }

            drawArc(
                color = activeColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle * progress,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Needle indicator line
            val currentAngleRad = Math.toRadians((startAngle + sweepAngle * progress).toDouble())
            val needleEnd = Offset(
                (center.x + (radius - 8.dp.toPx()) * cos(currentAngleRad)).toFloat(),
                (center.y + (radius - 8.dp.toPx()) * sin(currentAngleRad)).toFloat()
            )
            drawLine(
                color = Color.White,
                start = center,
                end = needleEnd,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(color = Color.White, radius = 5.dp.toPx(), center = center)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$rpm",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = (32 * fontScale).sp,
                    fontFamily = fontFamily
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "RPM x1000",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    fontFamily = fontFamily
                ),
                color = AutoGreen
            )
        }
    }
}

@Composable
fun MiniArcGauge(
    title: String,
    valueStr: String,
    unit: String,
    progress: Float,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier.size(70.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(64.dp)) {
                    val startAngle = 140f
                    val sweepAngle = 260f
                    val strokeWidth = 6.dp.toPx()
                    val radius = (size.minDimension - strokeWidth) / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)

                    drawArc(
                        color = GaugeTrack,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    drawArc(
                        color = barColor,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle * progress.coerceIn(0f, 1f),
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = valueStr,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = barColor
                    )
                }
            }
        }
    }
}
