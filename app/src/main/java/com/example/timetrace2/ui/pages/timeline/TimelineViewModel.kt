package com.example.timetrace2.ui.pages.timeline

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetrace2.data.local.AppDatabase
import com.example.timetrace2.data.local.PlaceholderDataSeeder
import com.example.timetrace2.data.local.TimeEntryEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

sealed interface DialogMode {
    object Hidden : DialogMode
    object Add : DialogMode
    data class Edit(val entry: TimeEntry) : DialogMode
}

@OptIn(ExperimentalCoroutinesApi::class)
class TimelineViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db.reportDao()
    private val userId = MutableStateFlow<Int?>(null)

    val entries: StateFlow<List<TimeEntry>> = userId.flatMapLatest { currentUserId ->
        if (currentUserId == null) {
            flowOf(emptyList())
        } else {
            val today = LocalDate.now(ZoneOffset.UTC)
            val start = today.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            val end = today.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            dao.observeTimeEntries(currentUserId, start, end).map { rows ->
                rows.map { row ->
                    TimeEntry(
                        id = row.id.toLong(),
                        title = row.title,
                        category = EntryCategory.entries.firstOrNull {
                            it.label.equals(row.categoryName, ignoreCase = true)
                        } ?: EntryCategory.STUDY,
                        start = Instant.ofEpochMilli(row.startTime).atZone(ZoneOffset.UTC).toLocalTime(),
                        end = Instant.ofEpochMilli(row.endTime).atZone(ZoneOffset.UTC).toLocalTime(),
                        source = row.source,
                    )
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _selectedId = MutableStateFlow<Long?>(null)
    val selectedEntry: StateFlow<TimeEntry?> =
        combine(entries, _selectedId) { list, id -> list.firstOrNull { it.id == id } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _dialog = MutableStateFlow<DialogMode>(DialogMode.Hidden)
    val dialog: StateFlow<DialogMode> = _dialog.asStateFlow()

    fun openDetails(id: Long) { _selectedId.value = id }
    fun closeDetails() { _selectedId.value = null }

    fun openAddDialog() { _dialog.value = DialogMode.Add }
    fun openEditDialog(entry: TimeEntry) { _dialog.value = DialogMode.Edit(entry) }
    fun closeDialog() { _dialog.value = DialogMode.Hidden }

    fun saveEntry(category: EntryCategory, title: String, start: LocalTime, end: LocalTime) {
        viewModelScope.launch {
            val currentUserId = userId.value ?: return@launch
            val categoryId = dao.categoryIdForName(currentUserId, category.label)
                ?: dao.firstCategoryIdForUser(currentUserId)
            val today = LocalDate.now(ZoneOffset.UTC)
            val startMillis = today.atTime(start).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
            val endMillis = today.atTime(end).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
            val now = System.currentTimeMillis()

            val mode = _dialog.value
            if (mode is DialogMode.Edit) {
                dao.updateTimeEntry(
                    TimeEntryEntity(
                        id = mode.entry.id.toInt(),
                        userId = currentUserId,
                        categoryId = categoryId,
                        title = title.trim(),
                        startTime = startMillis,
                        endTime = endMillis,
                        durationSeconds = (endMillis - startMillis) / 1_000,
                        source = mode.entry.source,
                        createdAt = now,
                        updatedAt = now,
                    )
                )
            } else {
                dao.insertTimeEntry(
                    TimeEntryEntity(
                        userId = currentUserId,
                        categoryId = categoryId,
                        title = title.trim(),
                        startTime = startMillis,
                        endTime = endMillis,
                        durationSeconds = (endMillis - startMillis) / 1_000,
                        source = "Manual",
                        createdAt = now,
                        updatedAt = now,
                    )
                )
            }
            closeDialog()
        }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            dao.deleteTimeEntry(id.toInt())
            _selectedId.value = null
        }
    }

    init {
        viewModelScope.launch {
            PlaceholderDataSeeder(db.reportDao()).seedIfEmpty()
            userId.value = dao.firstUserId()
        }
    }
}