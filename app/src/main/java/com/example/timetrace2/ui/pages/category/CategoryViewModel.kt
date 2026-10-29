package com.example.timetrace2.ui.pages.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetrace2.data.category.CategoryRepository
import com.example.timetrace2.data.local.AppDatabase
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CategoryRepository(
        AppDatabase.getInstance(application).categoryDao(),
    )
    private val userId = MutableStateFlow<Int?>(null)
    val selectedPeriod = MutableStateFlow(CategoryPeriod.Today)
    val selectedDateMillis = MutableStateFlow(DEFAULT_DATE_MILLIS)

    val categories: StateFlow<List<CategoryItemUi>> = combine(
        userId,
        selectedPeriod,
        selectedDateMillis,
    ) { currentUserId, period, dateMillis ->
        Triple(currentUserId, period, dateMillis)
    }.flatMapLatest { (currentUserId, period, dateMillis) ->
        if (currentUserId == null) {
            flowOf(emptyList())
        } else {
            repository.observeCategories(currentUserId, period, dateMillis)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    init {
        viewModelScope.launch {
            repository.seedPlaceholderData()
            userId.value = repository.getUserId()
        }
    }

    fun selectPeriod(period: CategoryPeriod) {
        selectedPeriod.value = period
    }

    fun selectDate(dateMillis: Long) {
        selectedDateMillis.value = dateMillis
    }

    fun addCategory(name: String, colorHex: String, subtitle: String) {
        val currentUserId = userId.value ?: return
        viewModelScope.launch {
            repository.addCategory(currentUserId, name, colorHex, subtitle)
        }
    }

    fun updateCategory(categoryId: Int, name: String, colorHex: String, subtitle: String) {
        viewModelScope.launch {
            repository.updateCategory(categoryId, name, colorHex, subtitle)
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {
            repository.deleteCategory(categoryId)
        }
    }

    companion object {
        val DEFAULT_DATE_MILLIS = LocalDate.of(2025, 4, 26)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }
}