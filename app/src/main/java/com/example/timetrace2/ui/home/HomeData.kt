package com.example.timetrace2.ui.home

import androidx.compose.ui.graphics.Color

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
