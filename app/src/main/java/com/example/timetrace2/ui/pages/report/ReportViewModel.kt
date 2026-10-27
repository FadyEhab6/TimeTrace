package com.example.timetrace2.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetrace2.data.local.AppDatabase
import com.example.timetrace2.data.report.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ReportRepository(
        AppDatabase.getInstance(application).reportDao(),
    )
    private val userId = MutableStateFlow<Int?>(null)
    val selectedPeriod = MutableStateFlow(ReportPeriod.Daily)
    val selectedDateMillis = MutableStateFlow(DEFAULT_REPORT_DATE_MILLIS)

    val report: StateFlow<ReportData> = combine(
        userId,
        selectedPeriod,
        selectedDateMillis,
    ) { currentUserId, period, dateMillis ->
        Triple(currentUserId, period, dateMillis)
    }.flatMapLatest { (currentUserId, period, dateMillis) ->
        if (currentUserId == null) {
            kotlinx.coroutines.flow.flowOf(ReportData())
        } else {
            repository.observeReport(currentUserId, period, dateMillis)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ReportData(),
    )

    init {
        viewModelScope.launch {
            repository.seedPlaceholderData()
            userId.value = repository.getUserId()
        }
    }

    fun selectPeriod(period: ReportPeriod) {
        selectedPeriod.value = period
    }

    fun selectDate(dateMillis: Long) {
        selectedDateMillis.value = dateMillis
    }

    companion object {
        val DEFAULT_REPORT_DATE_MILLIS = java.time.LocalDate.of(2025, 4, 26)
            .atStartOfDay(java.time.ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }
}
