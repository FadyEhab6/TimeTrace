package com.example.timetrace2.data.local

import java.time.LocalDate
import java.time.ZoneOffset

class PlaceholderDataSeeder(
    private val dao: ReportDao,
) {
    suspend fun seedIfEmpty() {
        if (dao.countTimeEntries() >= 12) return

        val now = System.currentTimeMillis()
        val userId = if (dao.countUsers() == 0) {
            val newUserId = dao.insertUser(
                UserEntity(
                    name = "Demo User",
                    email = "demo@timetrace.local",
                    createdAt = now,
                ),
            ).toInt()
            dao.insertCategories(
                listOf(
                    CategoryEntity(userId = newUserId, name = "Study", color = "#4F86F7", isDefault = true, createdAt = now),
                    CategoryEntity(userId = newUserId, name = "Work", color = "#FFB66D", isDefault = true, createdAt = now),
                    CategoryEntity(userId = newUserId, name = "Health", color = "#78D0B0", isDefault = true, createdAt = now),
                    CategoryEntity(userId = newUserId, name = "Leisure", color = "#AD8AE4", isDefault = true, createdAt = now),
                ),
            )
            newUserId
        } else {
            dao.firstUserId()
        }
        val categoryIds = dao.categoryIds(userId)

        val selectedDate = LocalDate.of(2025, 4, 26)
        val entries = listOf(
            PlaceholderEntry("Study session", categoryIds[0], 135, 9, 0),
            PlaceholderEntry("Work planning", categoryIds[1], 80, 12, 0),
            PlaceholderEntry("Morning walk", categoryIds[2], 40, 15, 0),
            PlaceholderEntry("Reading", categoryIds[3], 15, 19, 0),
            PlaceholderEntry("Weekly study", categoryIds[0], 120, 10, -1),
            PlaceholderEntry("Weekly work", categoryIds[1], 90, 13, -2),
            PlaceholderEntry("Weekly health", categoryIds[2], 45, 8, -3),
            PlaceholderEntry("Weekly leisure", categoryIds[3], 30, 18, -4),
            PlaceholderEntry("Monthly study", categoryIds[0], 180, 9, -8),
            PlaceholderEntry("Monthly work", categoryIds[1], 150, 11, -12),
            PlaceholderEntry("Monthly health", categoryIds[2], 60, 16, -18),
            PlaceholderEntry("Monthly leisure", categoryIds[3], 90, 20, -24),
        )
        val entriesToInsert = entries.map { placeholder ->
            val start = selectedDate
                .plusDays(placeholder.dayOffset.toLong())
                .atTime(placeholder.startHour, 0)
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli()
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
        dao.insertTimeEntries(entriesToInsert)
    }

    private data class PlaceholderEntry(
        val title: String,
        val categoryId: Int,
        val minutes: Long,
        val startHour: Int,
        val dayOffset: Int,
    )
}
