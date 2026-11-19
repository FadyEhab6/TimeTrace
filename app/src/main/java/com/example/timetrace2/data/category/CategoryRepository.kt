package com.example.timetrace2.data.category

import androidx.compose.ui.graphics.Color
import com.example.timetrace2.data.local.CategoryDao
import com.example.timetrace2.data.local.CategoryEntity
import com.example.timetrace2.data.local.PlaceholderDataSeeder
import com.example.timetrace2.data.local.ReportDao
import com.example.timetrace2.ui.pages.category.CategoryItemUi
import com.example.timetrace2.ui.pages.category.CategoryPeriod
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CategoryRepository(
    private val dao: CategoryDao,
    private val reportDao: ReportDao, // only used for seeding and the user id
) {
    suspend fun seedPlaceholderData() {
        PlaceholderDataSeeder(reportDao).seedIfEmpty()
    }

    suspend fun getUserId(): Int = reportDao.firstUserId()

    fun observeCategories(
        userId: Int,
        period: CategoryPeriod,
        selectedDateMillis: Long,
    ): Flow<List<CategoryItemUi>> {
        val date = Instant.ofEpochMilli(selectedDateMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
        val (start, end) = dateRange(date, period)

        return dao.observeCategories(userId, start, end).map { rows ->
            rows.map { row ->
                CategoryItemUi(
                    id = row.id,
                    name = row.name,
                    description = row.description.orEmpty(),
                    subtitle = row.description?.takeIf { it.isNotBlank() } ?: "No tasks yet",
                    duration = formatDuration(row.durationSeconds),
                    colorHex = row.color,
                    color = Color(android.graphics.Color.parseColor(row.color)),
                )
            }
        }
    }

    suspend fun addCategory(userId: Int, name: String, colorHex: String, description: String) {
        dao.insertCategory(
            CategoryEntity(
                userId = userId,
                name = name.trim(),
                color = colorHex,
                description = description.trim().ifEmpty { null },
                isDefault = false,
                createdAt = System.currentTimeMillis(),
            )
        )
    }

    suspend fun updateCategory(categoryId: Int, name: String, colorHex: String, description: String) {
        dao.updateCategory(
            categoryId = categoryId,
            name = name.trim(),
            color = colorHex,
            description = description.trim().ifEmpty { null },
        )
    }

    suspend fun deleteCategory(categoryId: Int) {
        dao.deleteCategory(categoryId)
    }

    private fun dateRange(date: LocalDate, period: CategoryPeriod): Pair<Long, Long> {
        val startDate = when (period) {
            CategoryPeriod.Today -> date
            CategoryPeriod.ThisWeek -> date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            CategoryPeriod.ThisMonth -> date.withDayOfMonth(1)
            CategoryPeriod.Custom -> date.minusDays(7)
        }
        val endDate = when (period) {
            CategoryPeriod.Today -> startDate.plusDays(1)
            CategoryPeriod.ThisWeek -> startDate.plusWeeks(1)
            CategoryPeriod.ThisMonth -> startDate.plusMonths(1)
            CategoryPeriod.Custom -> date.plusDays(8) // end is exclusive, so +8 includes day +7
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