package com.example.presentation.common.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.max
import kotlin.math.min

/**
 * D3-inspired Linear Scale mapping domain values [min, max] to canvas pixel ranges [rangeMin, rangeMax].
 */
data class LinearScale(
    val domainMin: Float,
    val domainMax: Float,
    val rangeMin: Float,
    val rangeMax: Float
) {
    private val domainSpan = if (domainMax == domainMin) 1f else domainMax - domainMin
    private val rangeSpan = rangeMax - rangeMin

    fun scale(value: Float): Float {
        val clamped = value.coerceIn(min(domainMin, domainMax), max(domainMin, domainMax))
        val normalized = (clamped - domainMin) / domainSpan
        return rangeMin + normalized * rangeSpan
    }

    fun invert(pixel: Float): Float {
        val normalized = (pixel - rangeMin) / rangeSpan
        return domainMin + normalized * domainSpan
    }

    fun ticks(count: Int = 4): List<Float> {
        if (count <= 1) return listOf(domainMin, domainMax)
        val step = domainSpan / (count - 1)
        return (0 until count).map { domainMin + it * step }
    }
}

/**
 * D3-inspired Smooth Curve Generator (equivalent to d3.curveMonotoneX).
 * Computes smooth cubic Bezier control points between adjacent telemetry coordinates.
 */
object D3CurveInterpolator {

    fun generateMonotoneSpline(
        points: List<Offset>,
        baselineY: Float
    ): Pair<Path, Path> {
        val strokePath = Path()
        val areaPath = Path()

        if (points.isEmpty()) return Pair(strokePath, areaPath)

        strokePath.moveTo(points.first().x, points.first().y)
        areaPath.moveTo(points.first().x, baselineY)
        areaPath.lineTo(points.first().x, points.first().y)

        if (points.size == 1) {
            areaPath.lineTo(points.first().x, baselineY)
            areaPath.close()
            return Pair(strokePath, areaPath)
        }

        for (i in 0 until points.size - 1) {
            val p0 = points.getOrElse(i - 1) { points[i] }
            val p1 = points[i]
            val p2 = points[i + 1]
            val p3 = points.getOrElse(i + 2) { p2 }

            // Catmull-Rom to Cubic Bezier control points conversion
            val c1x = p1.x + (p2.x - p0.x) / 6f
            val c1y = p1.y + (p2.y - p0.y) / 6f
            val c2x = p2.x - (p3.x - p1.x) / 6f
            val c2y = p2.y - (p3.y - p1.y) / 6f

            strokePath.cubicTo(c1x, c1y, c2x, c2y, p2.x, p2.y)
            areaPath.cubicTo(c1x, c1y, c2x, c2y, p2.x, p2.y)
        }

        areaPath.lineTo(points.last().x, baselineY)
        areaPath.close()

        return Pair(strokePath, areaPath)
    }
}
