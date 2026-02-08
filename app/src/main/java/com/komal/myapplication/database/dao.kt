package com.komal.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContest(contest: ContestEntity)
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
}