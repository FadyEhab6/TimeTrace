package com.example.timetrace2.ui.pages.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import java.time.LocalTime

sealed interface DialogMode {
    object Hidden : DialogMode
    object Add : DialogMode
    data class Edit(val entry: TimeEntry) : DialogMode
}

class TimelineViewModel : ViewModel() {

    private val _entries = MutableStateFlow(DummyTimelineData.today())
    val entries: StateFlow<List<TimeEntry>> = _entries.asStateFlow()

    private val _selectedId = MutableStateFlow<Long?>(null)
    val selectedEntry: StateFlow<TimeEntry?> =
        combine(_entries, _selectedId) { list, id -> list.firstOrNull { it.id == id } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _dialog = MutableStateFlow<DialogMode>(DialogMode.Hidden)
    val dialog: StateFlow<DialogMode> = _dialog.asStateFlow()

    fun openDetails(id: Long) { _selectedId.value = id }
    fun closeDetails() { _selectedId.value = null }

    fun openAddDialog() { _dialog.value = DialogMode.Add }
    fun openEditDialog(entry: TimeEntry) { _dialog.value = DialogMode.Edit(entry) }
    fun closeDialog() { _dialog.value = DialogMode.Hidden }

    fun saveEntry(category: EntryCategory, title: String, start: LocalTime, end: LocalTime) {
        when (val mode = _dialog.value) {
            is DialogMode.Edit -> _entries.update { list ->
                list.map {
                    if (it.id == mode.entry.id)
                        it.copy(title = title.trim(), category = category, start = start, end = end)
                    else it
                }.sortedBy { it.start }
            }
            else -> {
                val entry = TimeEntry(System.currentTimeMillis(), title.trim(), category, start, end)
                _entries.update { (it + entry).sortedBy { e -> e.start } }
            }
        }
        closeDialog()
    }

    fun deleteEntry(id: Long) {
        _entries.update { list -> list.filterNot { it.id == id } }
        _selectedId.value = null
    }
}