package com.komal.myapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContest(contest: ContestEntity): Long   // ← returns real inserted ID

    @Delete
    suspend fun deleteContest(contest: ContestEntity)

    @Update
    suspend fun updateContest(contest: ContestEntity)

    @Query("SELECT * FROM contests")
    fun getAllContests(): Flow<List<ContestEntity>>

    @Query("SELECT * FROM contests ORDER BY startTimeMillis ASC")
    fun getAllContestsSortedByStartTime(): Flow<List<ContestEntity>>

    @Query("SELECT * FROM contests ORDER BY reminderTimeMillis ASC")
    fun getAllContestsSortedByReminderTime(): Flow<List<ContestEntity>>

    @Query("DELETE FROM contests")
    suspend fun clearAll()

    @Query("DELETE FROM contests WHERE isManual = 0")
    suspend fun clearApiContests()

    @Query("SELECT * FROM contests WHERE isBookmarked = 1 ORDER BY startTimeMillis ASC")
    fun getBookmarkedContests(): Flow<List<ContestEntity>>

    @Query("SELECT * FROM contests WHERE name LIKE '%' || :query || '%' OR platform LIKE '%' || :query || '%' ORDER BY startTimeMillis ASC")
    fun searchContests(query: String): Flow<List<ContestEntity>>

    @Query("SELECT * FROM contests WHERE platform = :platform ORDER BY startTimeMillis ASC")
    fun getContestsByPlatform(platform: String): Flow<List<ContestEntity>>
}