package com.example.timetrace2.ui.home

import androidx.compose.ui.graphics.Color
import com.example.timetrace2.ui.theme.HealthGreen
import com.example.timetrace2.ui.theme.StudyBlue
import com.example.timetrace2.ui.theme.WorkOrange

/** One row in "Today's entries". */
data class HomeEntry(
    val title: String,
    val subtitle: String,
    val duration: String,
    val color: Color,
)

/** The running timer card. */
data class ActiveTimer(
    val category: String,
    val title: String,
    val elapsed: String,
)

/** Everything the Home screen needs to draw itself. */
data class HomeData(
    val userName: String = "",
    val trackedTime: String = "0m",
    val goal: String = "8h",
    val progress: Float = 0f,
    val activeTimer: ActiveTimer? = null,
    val entries: List<HomeEntry> = emptyList(),
)

object SampleHomeData {
    val data = HomeData(
        userName = "Sarah",
        trackedTime = "4h 30m",
        goal = "8h",
        progress = 0.56f,
        activeTimer = ActiveTimer("Study", "Graduation Project", "1h 15m"),
        entries = listOf(
            HomeEntry("Graduation Project", "Study • 9:00 – 11:00", "2h", StudyBlue),
            HomeEntry("Workout", "Health • 12:30 – 1:30", "1h", HealthGreen),
            HomeEntry("Team Meeting", "Work • 3:00 – 4:30", "1h 30m", WorkOrange),
        ),
    )
}
