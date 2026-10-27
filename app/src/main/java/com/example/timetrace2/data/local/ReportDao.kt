package com.example.timetrace2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class ReportCategoryRow(
    val categoryName: String,
    val categoryColor: String,
    val durationSeconds: Long,
)

@Dao
interface ReportDao {
    @Query(
        """
        SELECT category.name AS categoryName,
               category.color AS categoryColor,
               SUM(time_entry.durationSeconds) AS durationSeconds
        FROM time_entry
        INNER JOIN category ON category.id = time_entry.categoryId
        WHERE time_entry.userId = :userId
          AND time_entry.startTime >= :startTime
          AND time_entry.startTime < :endTime
        GROUP BY category.id, category.name, category.color
        ORDER BY durationSeconds DESC
        """,
    )
    fun observeCategoryTotals(
        userId: Int,
        startTime: Long,
        endTime: Long,
    ): Flow<List<ReportCategoryRow>>

    @Query("SELECT COUNT(*) FROM user")
    suspend fun countUsers(): Int

    @Query("SELECT id FROM user ORDER BY id LIMIT 1")
    suspend fun firstUserId(): Int

    @Query("SELECT id FROM category WHERE userId = :userId ORDER BY id")
    suspend fun categoryIds(userId: Int): List<Int>

    @Query("SELECT COUNT(*) FROM time_entry")
    suspend fun countTimeEntries(): Int

    @Query("SELECT id FROM category WHERE userId = :userId ORDER BY id LIMIT 1")
    suspend fun firstCategoryIdForUser(userId: Int): Int

    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Insert
    suspend fun insertCategories(categories: List<CategoryEntity>): List<Long>

    @Insert
    suspend fun insertTimeEntries(entries: List<TimeEntryEntity>)

    @Query(
        """
        SELECT * FROM time_entry
        WHERE userId = :userId
          AND source = 'manual_timer'
          AND startTime >= :startOfDay
        ORDER BY startTime DESC
        """,
    )
    suspend fun getTodayTimerSessions(userId: Int, startOfDay: Long): List<TimeEntryEntity>
}
