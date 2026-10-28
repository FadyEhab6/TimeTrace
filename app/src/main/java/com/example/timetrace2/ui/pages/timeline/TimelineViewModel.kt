package com.example.timetrace2.ui.pages.timeline

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalTime

class TimelineViewModel : ViewModel() {

    private val _entries = MutableStateFlow(DummyTimelineData.today())
    val entries: StateFlow<List<TimeEntry>> = _entries.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    fun openAddDialog() { _showAddDialog.value = true }
    fun closeAddDialog() { _showAddDialog.value = false }

    fun addEntry(category: EntryCategory, title: String, start: LocalTime, end: LocalTime) {
        val entry = TimeEntry(System.currentTimeMillis(), title.trim(), category, start, end)
        _entries.update { (it + entry).sortedBy { e -> e.start } }
        closeAddDialog()
    }
}