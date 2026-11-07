package com.example.presentation.common.chart

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.common.components.CarbonCard
import com.example.presentation.common.components.CyberAmber
import com.example.presentation.common.components.CyberCyan
import com.example.presentation.common.components.CyberGreen
import com.example.presentation.common.components.CyberRed
import com.example.presentation.common.components.GlassmorphicCard
import com.example.presentation.common.components.StatusBadge
import com.example.presentation.common.components.TextMuted
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Data point in a time-series telemetry sequence.
 */
data class TelemetryDataPoint(
    val timestamp: Long,
    val value: Float
)

@Composable
fun D3InteractiveChart(
    title: String,
    unit: String,
    dataPoints: List<TelemetryDataPoint>,
    lineColor: Color = CyberCyan,
    modifier: Modifier = Modifier,
    heightDp: Int = 180,
    warningThreshold: Float? = null,
    criticalThreshold: Float? = null,
    fixedMinDomain: Float? = null,
    fixedMaxDomain: Float? = null
) {
    var scrubbedPoint by remember { mutableStateOf<TelemetryDataPoint?>(null) }
    var scrubbedPixelX by remember { mutableStateOf<Float?>(null) }

    val currentValue = dataPoints.lastOrNull()?.value ?: 0f
    val peakValue = dataPoints.maxOfOrNull { it.value } ?: 0f
    val minValue = dataPoints.minOfOrNull { it.value } ?: 0f
    val avgValue = if (dataPoints.isNotEmpty()) dataPoints.map { it.value }.average().toFloat() else 0f

    val displayValue = scrubbedPoint?.value ?: currentValue

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("d3_chart_${title.lowercase().replace(" ", "_")}"),
        borderColor = lineColor.copy(alpha = 0.35f)
    ) {
        // Chart Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    color = TextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = if (displayValue % 1 == 0f) displayValue.toInt().toString() else String.format("%.1f", displayValue),
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        color = lineColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                    if (scrubbedPoint != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(เลื่อนดูประวัติ)",
                            color = CyberAmber,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
            }

            // Stat Badges
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF1B2433)
                ) {
                    Text(
                        text = "Max: ${if (peakValue % 1 == 0f) peakValue.toInt() else String.format("%.1f", peakValue)}",
                        color = CyberGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF1B2433)
                ) {
                    Text(
                        text = "Avg: ${String.format("%.1f", avgValue)}",
                        color = TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // D3 Interactive Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightDp.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F151F))
                    .pointerInput(dataPoints) {
                        detectTapGestures(
                            onPress = { offset ->
                                scrubbedPixelX = offset.x
                                scrubbedPoint = findNearestDataPoint(offset.x, size.width.toFloat(), dataPoints)
                                tryAwaitRelease()
                                scrubbedPixelX = null
                                scrubbedPoint = null
                            }
                        )
                    }
                    .pointerInput(dataPoints) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                scrubbedPixelX = offset.x
                                scrubbedPoint = findNearestDataPoint(offset.x, size.width.toFloat(), dataPoints)
                            },
                            onDragEnd = {
                                scrubbedPixelX = null
                                scrubbedPoint = null
                            },
                            onDragCancel = {
                                scrubbedPixelX = null
                                scrubbedPoint = null
                            },
                            onDrag = { change, _ ->
                                scrubbedPixelX = change.position.x
                                scrubbedPoint = findNearestDataPoint(change.position.x, size.width.toFloat(), dataPoints)
                            }
                        )
                    }
            ) {
                if (dataPoints.isEmpty()) return@Canvas

                val width = size.width
                val height = size.height

                val paddingLeft = 36f
                val paddingRight = 16f
                val paddingTop = 16f
                val paddingBottom = 24f

                val chartWidth = width - paddingLeft - paddingRight
                val chartHeight = height - paddingTop - paddingBottom

                val calculatedMin = fixedMinDomain ?: (dataPoints.minOf { it.value } * 0.95f)
                val calculatedMax = fixedMaxDomain ?: max(dataPoints.maxOf { it.value } * 1.05f, calculatedMin + 1f)

                // D3 Scales
                val yScale = LinearScale(
                    domainMin = calculatedMin,
                    domainMax = calculatedMax,
                    rangeMin = paddingTop + chartHeight,
                    rangeMax = paddingTop
                )

                val minTime = dataPoints.first().timestamp
                val maxTime = max(dataPoints.last().timestamp, minTime + 1L)

                val xScale = LinearScale(
                    domainMin = minTime.toFloat(),
                    domainMax = maxTime.toFloat(),
                    rangeMin = paddingLeft,
                    rangeMax = paddingLeft + chartWidth
                )

                // 1. Draw D3 Grid & Y-Axis Ticks
                val yTicks = yScale.ticks(4)
                val textPaint = Paint().apply {
                    color = android.graphics.Color.rgb(120, 140, 165)
                    textSize = 22f
                    isAntiAlias = true
                }

                yTicks.forEach { tickVal ->
                    val yPixel = yScale.scale(tickVal)
                    drawLine(
                        color = Color(0x1AFFFFFF),
                        start = Offset(paddingLeft, yPixel),
                        end = Offset(paddingLeft + chartWidth, yPixel),
                        strokeWidth = 1f
                    )
                    drawContext.canvas.nativeCanvas.drawText(
                        if (tickVal >= 1000f) "${(tickVal / 1000).toInt()}k" else "${tickVal.toInt()}",
                        6f,
                        yPixel + 8f,
                        textPaint
                    )
                }

                // 2. Draw Warning & Critical Threshold Lines
                warningThreshold?.let { warn ->
                    if (warn in calculatedMin..calculatedMax) {
                        val yPixel = yScale.scale(warn)
                        drawLine(
                            color = CyberAmber.copy(alpha = 0.7f),
                            start = Offset(paddingLeft, yPixel),
                            end = Offset(paddingLeft + chartWidth, yPixel),
                            strokeWidth = 1.5f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }
                }

                criticalThreshold?.let { crit ->
                    if (crit in calculatedMin..calculatedMax) {
                        val yPixel = yScale.scale(crit)
                        drawLine(
                            color = CyberRed.copy(alpha = 0.85f),
                            start = Offset(paddingLeft, yPixel),
                            end = Offset(paddingLeft + chartWidth, yPixel),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 8f), 0f)
                        )
                    }
                }

                // 3. Compute D3 Cubic Spline Points
                val canvasPoints = dataPoints.map { pt ->
                    Offset(
                        x = xScale.scale(pt.timestamp.toFloat()),
                        y = yScale.scale(pt.value)
                    )
                }

                val baselineY = paddingTop + chartHeight
                val (strokePath, areaPath) = D3CurveInterpolator.generateMonotoneSpline(canvasPoints, baselineY)

                // 4. Draw Area Gradient
                drawPath(
                    path = areaPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lineColor.copy(alpha = 0.35f),
                            lineColor.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        startY = paddingTop,
                        endY = baselineY
                    )
                )

                // 5. Draw Stroke Curve
                drawPath(
                    path = strokePath,
                    color = lineColor,
                    style = Stroke(
                        width = 3.5f,
                        cap = StrokeCap.Round
                    )
                )

                // 6. Draw Latest Value Dot
                canvasPoints.lastOrNull()?.let { lastPt ->
                    drawCircle(
                        color = lineColor.copy(alpha = 0.3f),
                        radius = 8f,
                        center = lastPt
                    )
                    drawCircle(
                        color = lineColor,
                        radius = 4f,
                        center = lastPt
                    )
                }

                // 7. Draw Interactive Scrubber Cursor
                scrubbedPixelX?.let { touchX ->
                    val clampedX = touchX.coerceIn(paddingLeft, paddingLeft + chartWidth)
                    drawLine(
                        color = Color.White.copy(alpha = 0.7f),
                        start = Offset(clampedX, paddingTop),
                        end = Offset(clampedX, baselineY),
                        strokeWidth = 1.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )

                    scrubbedPoint?.let { pt ->
                        val ptX = xScale.scale(pt.timestamp.toFloat())
                        val ptY = yScale.scale(pt.value)
                        drawCircle(
                            color = CyberAmber,
                            radius = 6f,
                            center = Offset(ptX, ptY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3f,
                            center = Offset(ptX, ptY)
                        )
                    }
                }
            }
        }
    }
}

private fun findNearestDataPoint(
    pixelX: Float,
    canvasWidth: Float,
    points: List<TelemetryDataPoint>
): TelemetryDataPoint? {
    if (points.isEmpty()) return null
    if (points.size == 1) return points.first()

    val paddingLeft = 36f
    val paddingRight = 16f
    val chartWidth = canvasWidth - paddingLeft - paddingRight
    val clampedX = (pixelX - paddingLeft).coerceIn(0f, chartWidth)
    val ratio = clampedX / chartWidth

    val index = (ratio * (points.size - 1)).toInt().coerceIn(0, points.size - 1)
    return points[index]
}
