package com.example.timetrace2.ui.pages.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class TimerSession(val label: String)

class TimerViewModel : ViewModel() {

    private val _seconds = MutableStateFlow(0L)
    val seconds: StateFlow<Long> = _seconds.asStateFlow()

    private val _running = MutableStateFlow(false)
    val running: StateFlow<Boolean> = _running.asStateFlow()

    private val _sessions = MutableStateFlow<List<TimerSession>>(emptyList())
    val sessions: StateFlow<List<TimerSession>> = _sessions.asStateFlow()

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
            val hours = _seconds.value / 3600
            val minutes = (_seconds.value % 3600) / 60
            val seconds = _seconds.value % 60
            val label = "%02d:%02d:%02d".format(hours, minutes, seconds)
            _sessions.value = _sessions.value + TimerSession(label)
        }
        _seconds.value = 0
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}
