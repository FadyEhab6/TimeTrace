package com.example.timetrace2.data.report

import com.example.timetrace2.data.local.PlaceholderDataSeeder
import com.example.timetrace2.data.local.ReportDao
import com.example.timetrace2.ui.report.ReportCategory
import com.example.timetrace2.ui.report.ReportData
import com.example.timetrace2.ui.report.ReportPeriod
import androidx.compose.ui.graphics.Color
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ReportRepository(
    private val dao: ReportDao,
) {
    suspend fun seedPlaceholderData() {
        PlaceholderDataSeeder(dao).seedIfEmpty()
    }

    suspend fun getUserId(): Int = dao.firstUserId()

    fun observeReport(
        userId: Int,
        period: ReportPeriod,
        selectedDateMillis: Long,
    ): Flow<ReportData> {
        val date = Instant.ofEpochMilli(selectedDateMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
        val (start, end) = dateRange(date, period)

        return dao.observeCategoryTotals(userId, start, end).map { rows ->
            val totalSeconds = rows.sumOf { it.durationSeconds }
            ReportData(
                totalTime = formatDuration(totalSeconds),
                categories = rows.map { row ->
                    ReportCategory(
                        name = row.categoryName,
                        percent = if (totalSeconds == 0L) {
                            0
                        } else {
                            ((row.durationSeconds * 100) / totalSeconds).toInt()
                        },
                        duration = formatDuration(row.durationSeconds),
                        color = Color(android.graphics.Color.parseColor(row.categoryColor)),
                    )
                },
            )
        }
    }

    private fun dateRange(date: LocalDate, period: ReportPeriod): Pair<Long, Long> {
        val startDate = when (period) {
            ReportPeriod.Daily -> date
            ReportPeriod.Weekly -> date.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
            ReportPeriod.Monthly -> date.withDayOfMonth(1)
            ReportPeriod.Custom -> date.minusDays(3)
        }
        val endDate = when (period) {
            ReportPeriod.Daily -> startDate.plusDays(1)
            ReportPeriod.Weekly -> startDate.plusWeeks(1)
            ReportPeriod.Monthly -> startDate.plusMonths(1)
            ReportPeriod.Custom -> date.plusDays(4)
        }
        return startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli() to
            endDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
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
}
