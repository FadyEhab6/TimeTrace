package com.example.timetrace2.ui.pages.timeline

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.timetrace2.R
import com.example.timetrace2.ui.theme.*
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class EntryCategory(val label: String, @DrawableRes val iconRes: Int, val color: Color) {
    STUDY("Study", R.drawable.ic_school, HomeBlue),
    HEALTH("Health", R.drawable.ic_favorite, SuccessGreen),
    WORK("Work", R.drawable.ic_work, ShoppingAmber),
    LEISURE("Leisure", R.drawable.ic_menu_book, SleepPurple);

    val colorInt: Int get() = color.toArgb()
}

data class TimeEntry(
    val id: Long,
    val title: String,
    val category: EntryCategory,
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

// TODO: replace with real data (Room / repository) once the data layer is ready
object DummyTimelineData {
    fun today() = listOf(
        TimeEntry(1, "Graduation Project", EntryCategory.STUDY, LocalTime.of(9, 0), LocalTime.of(11, 0)),
        TimeEntry(2, "Workout", EntryCategory.HEALTH, LocalTime.of(12, 30), LocalTime.of(13, 30)),
        TimeEntry(3, "Team Meeting", EntryCategory.WORK, LocalTime.of(15, 0), LocalTime.of(16, 30)),
        TimeEntry(4, "Reading", EntryCategory.LEISURE, LocalTime.of(18, 0), LocalTime.of(19, 0))
    )
}