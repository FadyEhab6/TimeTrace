package com.example.timetrace2.ui.pages.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetrace2.data.local.AppDatabase
import com.example.timetrace2.data.local.PlaceholderDataSeeder
import com.example.timetrace2.data.local.TimeEntryEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class TimerSession(val label: String)

@kotlinx.coroutines.ExperimentalCoroutinesApi
class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db.reportDao()
    private val userId = MutableStateFlow<Int?>(null)

    private val _seconds = MutableStateFlow(0L)
    val seconds: StateFlow<Long> = _seconds.asStateFlow()

    private val _running = MutableStateFlow(false)
    val running: StateFlow<Boolean> = _running.asStateFlow()

    val sessions: StateFlow<List<TimerSession>> = userId.flatMapLatest { currentUserId ->
        if (currentUserId == null) {
            flowOf(emptyList())
        } else {
            val start = LocalDate.now(ZoneOffset.UTC).atStartOfDay(ZoneOffset.UTC)
                .toInstant().toEpochMilli()
            dao.observeTodayTimerSessions(currentUserId, start).map { rows ->
                rows.map { row ->
                    val seconds = row.durationSeconds
                    val hours = seconds / 3600
                    val minutes = (seconds % 3600) / 60
                    val secs = seconds % 60
                    TimerSession("%02d:%02d:%02d".format(hours, minutes, secs))
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private var timerJob: Job? = null

    fun start() {
        if (_running.value) return

        _running.value = true
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                _seconds.value += 1
            }
        }
    }

    fun pause() {
        _running.value = false
        timerJob?.cancel()
        timerJob = null
    }

    fun stop() {
        pause()
        if (_seconds.value > 0) {
            viewModelScope.launch {
                val currentUserId = userId.value ?: return@launch
                val categoryId = dao.firstCategoryIdForUser(currentUserId)
                val now = System.currentTimeMillis()
                dao.insertTimeEntry(
                    TimeEntryEntity(
                        userId = currentUserId,
                        categoryId = categoryId,
                        title = "Timer Session",
                        startTime = now - _seconds.value * 1_000,
                        endTime = now,
                        durationSeconds = _seconds.value,
                        source = "manual_timer",
                        createdAt = now,
                        updatedAt = now,
                    )
                )
                _seconds.value = 0
            }
        } else {
            _seconds.value = 0
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }

    init {
        viewModelScope.launch {
            PlaceholderDataSeeder(dao).seedIfEmpty()
            userId.value = dao.firstUserId()
        }
    }
}
