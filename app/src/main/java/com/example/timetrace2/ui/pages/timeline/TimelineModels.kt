package com.example.timetrace2.ui.pages.timeline

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.timetrace2.ui.theme.*   // your Color.kt (adjust names if they differ)
import java.time.Duration
import java.time.LocalTime

enum class EntryCategory(val label: String, val icon: ImageVector, val color: Color) {
    STUDY("Study", Icons.Outlined.School, HomeBlue),
    HEALTH("Health", Icons.Outlined.FavoriteBorder, SuccessGreen),
    WORK("Work", Icons.Outlined.Work, ShoppingAmber),
    LEISURE("Leisure", Icons.Outlined.MenuBook, SleepPurple)
}

data class TimeEntry(
    val id: Long,
    val title: String,
    val category: EntryCategory,
    val start: LocalTime,
    val end: LocalTime
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