package com.example.timetrace2.data.local

import java.time.LocalDate
import java.time.ZoneOffset

internal class PlaceholderDataSeeder(
    private val dao: CategoryDao,
) {
    suspend fun seedIfEmpty() {
        if (dao.countUsers() > 0) return

        val now = System.currentTimeMillis()
        val userId = dao.insertUser(
            UserEntity(
                name = "Sarah Ahmed",
                email = "sarah@example.com",
                createdAt = now,
            )
        ).toInt()

        val categories = listOf(
            CategoryEntity(
                userId = userId,
                name = "Study",
                color = "#4A80F0",
                description = "Graduation Project • Mobile Computing",
                isDefault = true,
                createdAt = now,
            ),
            CategoryEntity(
                userId = userId,
                name = "Work",
                color = "#E59C52",
                description = "Internship tasks",
                isDefault = true,
                createdAt = now,
            ),
            CategoryEntity(
                userId = userId,
                name = "Health",
                color = "#5ABF88",
                description = "Exercise sessions",
                isDefault = true,
                createdAt = now,
            ),
            CategoryEntity(
                userId = userId,
                name = "Leisure",
                color = "#A174ED",
                description = "Reading • Movies",
                isDefault = true,
                createdAt = now,
            ),
        )

        val categoryIds = dao.insertCategories(categories).map { it.toInt() }
        val baseTime = LocalDate.of(2025, 4, 26)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

        // Dummy Time Entries matching Wireframe #9 durations (12h 30m, 6h 15m, 2h 00m, 1h 30m)
        val entries = listOf(
            TimeEntryEntity(
                userId = userId,
                categoryId = categoryIds[0], // Study: 12h 30m = 45000s
                title = "Graduation Project",
                startTime = baseTime + 3_600_000L,
                endTime = baseTime + 48_600_000L,
                durationSeconds = 45_000L,
                source = "Manual",
                createdAt = now,
                updatedAt = now,
            ),
            TimeEntryEntity(
                userId = userId,
                categoryId = categoryIds[1], // Work: 6h 15m = 22500s
                title = "Internship tasks",
                startTime = baseTime + 50_000_000L,
                endTime = baseTime + 72_500_000L,
                durationSeconds = 22_500L,
                source = "Manual",
                createdAt = now,
                updatedAt = now,
            ),
            TimeEntryEntity(
                userId = userId,
                categoryId = categoryIds[2], // Health: 2h 00m = 7200s
                title = "Exercise sessions",
                startTime = baseTime + 73_000_000L,
                endTime = baseTime + 80_200_000L,
                durationSeconds = 7_200L,
                source = "Manual",
                createdAt = now,
                updatedAt = now,
            ),
            TimeEntryEntity(
                userId = userId,
                categoryId = categoryIds[3], // Leisure: 1h 30m = 5400s
                title = "Reading • Movies",
                startTime = baseTime + 81_000_000L,
                endTime = baseTime + 86_400_000L,
                durationSeconds = 5_400L,
                source = "Manual",
                createdAt = now,
                updatedAt = now,
            ),
        )
        dao.insertTimeEntries(entries)
    }
}