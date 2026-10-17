package com.example.timetrace2.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/** One row in the "Top Zones" list. */
data class Zone(
    val name: String,
    val hours: String,
    val color: Color,
    val icon: ImageVector,
)

/** A coloured slice of the 168-hour bar. `weight` is its relative width. */
data class BarPart(
    val color: Color,
    val weight: Float,
)

/** One coloured block inside a day's bar in the weekly chart. */
data class Segment(
    val color: Color,
    val hours: Float,
)

/** A single day in the weekly chart. Segments are listed bottom -> top. */
data class DaySummary(
    val day: String,
    val segments: List<Segment>,
)
