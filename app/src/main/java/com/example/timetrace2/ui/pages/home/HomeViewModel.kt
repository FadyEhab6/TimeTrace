package com.example.timetrace2.ui.pages.home

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetrace2.data.local.AppDatabase
import com.example.timetrace2.data.local.PlaceholderDataSeeder
import com.example.timetrace2.ui.home.HomeData
import com.example.timetrace2.ui.home.HomeEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).reportDao()
    private val userId = MutableStateFlow<Int?>(null)

    val homeData: StateFlow<HomeData> = userId.flatMapLatest { currentUserId ->
        if (currentUserId == null) {
            flowOf(HomeData())
        } else {
            val today = LocalDate.now(ZoneOffset.UTC)
            val start = today.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            val end = today.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

            combine(
                dao.observeUserName(currentUserId),
                dao.observeTodayEntries(currentUserId, start, end),
            ) { name, entries ->
                val totalSeconds = entries.sumOf { (it.endTime - it.startTime) / 1_000 }
                HomeData(
                    userName = name.orEmpty().ifBlank { "User" },
                    trackedTime = formatDuration(totalSeconds),
                    goal = "8h",
                    progress = (totalSeconds / 28_800f).coerceIn(0f, 1f),
                    entries = entries.map { entry ->
                        val startTime = Instant.ofEpochMilli(entry.startTime)
                            .atZone(ZoneOffset.UTC).toLocalTime()
                        val endTime = Instant.ofEpochMilli(entry.endTime)
                            .atZone(ZoneOffset.UTC).toLocalTime()
                        val minutes = (entry.endTime - entry.startTime) / 60_000
                        HomeEntry(
                            title = entry.title,
                            subtitle = "${entry.categoryName} • ${startTime} – ${endTime}",
                            duration = formatDuration(minutes * 60),
                            color = parseColor(entry.categoryColor),
                        )
                    },
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeData())

    init {
        viewModelScope.launch {
            PlaceholderDataSeeder(dao).seedIfEmpty()
            userId.value = dao.firstUserId()
        }
    }

    private fun formatDuration(seconds: Long): String {
        val totalMinutes = seconds / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            else -> "${minutes}m"
        }
    }

    private fun parseColor(hex: String): Color =
        try {
            Color(android.graphics.Color.parseColor(hex))
        } catch (_: IllegalArgumentException) {
            Color.Gray
        }
}
