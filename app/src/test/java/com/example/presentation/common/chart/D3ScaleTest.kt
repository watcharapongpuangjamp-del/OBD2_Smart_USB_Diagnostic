package com.example.presentation.common.chart

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class D3ScaleTest {

    @Test
    fun `linearScale correctly maps domain values to canvas pixel coordinates`() {
        val scale = LinearScale(
            domainMin = 0f,
            domainMax = 100f,
            rangeMin = 0f,
            rangeMax = 500f
        )

        assertEquals(0f, scale.scale(0f), 0.001f)
        assertEquals(250f, scale.scale(50f), 0.001f)
        assertEquals(500f, scale.scale(100f), 0.001f)
    }

    @Test
    fun `linearScale clamps out-of-bound domain values`() {
        val scale = LinearScale(
            domainMin = 0f,
            domainMax = 100f,
            rangeMin = 0f,
            rangeMax = 500f
        )

        assertEquals(0f, scale.scale(-20f), 0.001f)
        assertEquals(500f, scale.scale(150f), 0.001f)
    }

    @Test
    fun `linearScale invert correctly reverses pixel coordinate to domain value`() {
        val scale = LinearScale(
            domainMin = 0f,
            domainMax = 200f,
            rangeMin = 100f,
            rangeMax = 500f
        )

        assertEquals(0f, scale.invert(100f), 0.001f)
        assertEquals(100f, scale.invert(300f), 0.001f)
        assertEquals(200f, scale.invert(500f), 0.001f)
    }

    @Test
    fun `linearScale ticks generates evenly spaced divisions`() {
        val scale = LinearScale(
            domainMin = 0f,
            domainMax = 100f,
            rangeMin = 0f,
            rangeMax = 1000f
        )

        val ticks = scale.ticks(count = 5)
        assertEquals(5, ticks.size)
        assertEquals(0f, ticks[0], 0.001f)
        assertEquals(25f, ticks[1], 0.001f)
        assertEquals(50f, ticks[2], 0.001f)
        assertEquals(75f, ticks[3], 0.001f)
        assertEquals(100f, ticks[4], 0.001f)
    }

    @Test
    fun `d3CurveInterpolator produces valid paths for empty and multi-point coordinates`() {
        // Empty
        val (emptyStroke, emptyArea) = D3CurveInterpolator.generateMonotoneSpline(emptyList(), 200f)
        assertNotNull(emptyStroke)
        assertNotNull(emptyArea)

        // Single point
        val (singleStroke, singleArea) = D3CurveInterpolator.generateMonotoneSpline(
            listOf(Offset(10f, 50f)),
            200f
        )
        assertNotNull(singleStroke)
        assertNotNull(singleArea)

        // Multiple points
        val points = listOf(
            Offset(0f, 100f),
            Offset(50f, 40f),
            Offset(100f, 80f),
            Offset(150f, 20f),
            Offset(200f, 60f)
        )
        val (multiStroke, multiArea) = D3CurveInterpolator.generateMonotoneSpline(points, 200f)
        assertNotNull(multiStroke)
        assertNotNull(multiArea)
    }
}
