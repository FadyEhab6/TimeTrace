package com.example.timetrace2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class ReportCategoryRow(
    val categoryName: String,
    val categoryColor: String,
    val durationSeconds: Long,
)

data class TimelineEntryRow(
    val id: Int,
    val title: String,
    val categoryName: String,
    val categoryColor: String,
    val startTime: Long,
    val endTime: Long,
    val source: String,
)

data class HomeEntryRow(
    val title: String,
    val categoryName: String,
    val categoryColor: String,
    val startTime: Long,
    val endTime: Long,
)

@Dao
interface ReportDao {
    @Query("SELECT name FROM user WHERE id = :userId LIMIT 1")
    fun observeUserName(userId: Int): Flow<String?>

    @Query(
        """
        SELECT time_entry.title AS title,
               category.name AS categoryName,
               category.color AS categoryColor,
               time_entry.startTime AS startTime,
               time_entry.endTime AS endTime
        FROM time_entry
        INNER JOIN category ON category.id = time_entry.categoryId
        WHERE time_entry.userId = :userId
          AND time_entry.startTime >= :startTime
          AND time_entry.startTime < :endTime
        ORDER BY time_entry.startTime ASC
        """
    )
    fun observeTodayEntries(
        userId: Int,
        startTime: Long,
        endTime: Long,
    ): Flow<List<HomeEntryRow>>

    @Query(
        """
        SELECT time_entry.id AS id,
               time_entry.title AS title,
               category.name AS categoryName,
               category.color AS categoryColor,
               time_entry.startTime AS startTime,
               time_entry.endTime AS endTime,
               time_entry.source AS source
        FROM time_entry
        INNER JOIN category ON category.id = time_entry.categoryId
        WHERE time_entry.userId = :userId
          AND time_entry.startTime >= :startTime
          AND time_entry.startTime < :endTime
        ORDER BY time_entry.startTime ASC
        """
    )
    fun observeTimeEntries(
        userId: Int,
        startTime: Long,
        endTime: Long,
    ): Flow<List<TimelineEntryRow>>

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

    @Query("SELECT id FROM category WHERE userId = :userId AND name = :name LIMIT 1")
    suspend fun categoryIdForName(userId: Int, name: String): Int?

    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Insert
    suspend fun insertCategories(categories: List<CategoryEntity>): List<Long>

    @Insert
    suspend fun insertTimeEntries(entries: List<TimeEntryEntity>)

    @Insert
    suspend fun insertTimeEntry(entry: TimeEntryEntity): Long

    @Update
    suspend fun updateTimeEntry(entry: TimeEntryEntity)

    @Query("DELETE FROM time_entry WHERE id = :entryId")
    suspend fun deleteTimeEntry(entryId: Int)

    @Query(
        """
        SELECT * FROM time_entry
        WHERE userId = :userId
          AND source = 'manual_timer'
          AND startTime >= :startOfDay
        ORDER BY startTime DESC
        """,
    )
    fun observeTodayTimerSessions(
        userId: Int,
        startOfDay: Long,
    ): Flow<List<TimeEntryEntity>>
}
