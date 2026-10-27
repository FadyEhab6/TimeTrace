package com.example.timetrace2.data.local

import java.time.LocalDate
import java.time.ZoneOffset

class PlaceholderDataSeeder(
    private val dao: ReportDao,
) {
    suspend fun seedIfEmpty() {
        if (dao.countUsers() > 0) return

        val now = System.currentTimeMillis()
        val userId = dao.insertUser(
            UserEntity(
                name = "Demo User",
                email = "demo@timetrace.local",
                createdAt = now,
            ),
        ).toInt()
        val categoryIds = dao.insertCategories(
            listOf(
                CategoryEntity(userId = userId, name = "Study", color = "#4F86F7", isDefault = true, createdAt = now),
                CategoryEntity(userId = userId, name = "Work", color = "#FFB66D", isDefault = true, createdAt = now),
                CategoryEntity(userId = userId, name = "Health", color = "#78D0B0", isDefault = true, createdAt = now),
                CategoryEntity(userId = userId, name = "Leisure", color = "#AD8AE4", isDefault = true, createdAt = now),
            ),
        ).map(Long::toInt)

        val dayStart = LocalDate.of(2025, 4, 26)
            .atStartOfDay(ZoneOffset.UTC)
        val entries = listOf(
            PlaceholderEntry("Study session", categoryIds[0], 135, 9),
            PlaceholderEntry("Work planning", categoryIds[1], 80, 12),
            PlaceholderEntry("Morning walk", categoryIds[2], 40, 15),
            PlaceholderEntry("Reading", categoryIds[3], 15, 19),
        ).map { placeholder ->
            val start = dayStart.plusHours(placeholder.startHour.toLong()).toInstant().toEpochMilli()
            TimeEntryEntity(
                userId = userId,
                categoryId = placeholder.categoryId,
                title = placeholder.title,
                startTime = start,
                endTime = start + placeholder.minutes * 60_000L,
                durationSeconds = placeholder.minutes * 60L,
                source = "MANUAL",
                createdAt = now,
                updatedAt = now,
            )
        }
        dao.insertTimeEntries(entries)
    }

    private data class PlaceholderEntry(
        val title: String,
        val categoryId: Int,
        val minutes: Long,
        val startHour: Int,
    )
}
