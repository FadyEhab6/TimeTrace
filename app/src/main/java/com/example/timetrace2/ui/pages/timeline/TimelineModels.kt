package com.example.timetrace2.ui.pages.timeline

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.timetrace2.R
import com.example.timetrace2.ui.theme.*
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class TimelineCategory(
    val id: Int,
    val name: String,
    val color: Color,
) {
    val iconRes: Int
        get() = when (name.lowercase()) {
            "study" -> R.drawable.ic_school
            "health" -> R.drawable.ic_favorite
            "work" -> R.drawable.ic_work
            "leisure" -> R.drawable.ic_menu_book
            else -> R.drawable.ic_menu_book
        }

    val colorInt: Int get() = color.toArgb()
}

data class TimeEntry(
    val id: Long,
    val title: String,
    val category: TimelineCategory,
    val start: LocalTime,
    val end: LocalTime,
    val source: String = "Manual"
) {
    fun durationLabel(): String {
        val m = Duration.between(start, end).toMinutes()
        val h = m / 60
        val r = m % 60
        return when {
            h > 0 && r > 0 -> "${h}h ${r}m"
            h > 0 -> "${h}h"
            else -> "${r}m"
        }
    }

    fun shortRange(): String = "${start.format(SHORT)} - ${end.format(SHORT)}"
    fun longRange(): String = "Today, ${start.format(LONG)} – ${end.format(LONG)}"

    private companion object {
        val SHORT: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm")
        val LONG: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    }
}
