package com.example.timetrace2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class CategoryRow(
    val id: Int,
    val name: String,
    val color: String,
    val description: String?,
    val durationSeconds: Long,
)

@Dao
interface CategoryDao {
    @Query(
        """
        SELECT category.id AS id,
               category.name AS name,
               category.color AS color,
               category.description AS description,
               COALESCE(SUM(time_entry.durationSeconds), 0) AS durationSeconds
        FROM category
        LEFT JOIN time_entry ON category.id = time_entry.categoryId
            AND time_entry.startTime >= :startTime
            AND time_entry.startTime < :endTime
        WHERE category.userId = :userId
        GROUP BY category.id, category.name, category.color, category.description
        ORDER BY durationSeconds DESC, category.id ASC
        """
    )
    fun observeCategories(
        userId: Int,
        startTime: Long,
        endTime: Long,
    ): Flow<List<CategoryRow>>

    @Query("SELECT COUNT(*) FROM user")
    suspend fun countUsers(): Int

    @Query("SELECT id FROM user ORDER BY id LIMIT 1")
    suspend fun firstUserId(): Int

    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Insert
    suspend fun insertCategories(categories: List<CategoryEntity>): List<Long>

    @Insert
    suspend fun insertCategory(category: CategoryEntity): Long

    @Insert
    suspend fun insertTimeEntries(entries: List<TimeEntryEntity>)

    @Query("UPDATE category SET name = :name, color = :color, description = :description WHERE id = :categoryId")
    suspend fun updateCategory(categoryId: Int, name: String, color: String, description: String?)

    @Query("DELETE FROM category WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: Int)
}