package com.komal.myapplication.database

import kotlinx.coroutines.flow.Flow

class Repo(private val dao: dao){
    val allContest: Flow<List<ContestEntity>> = dao.getAllContests()

    val contestSortedByStartTime: Flow<List<ContestEntity>> = dao.getAllContestsSortedByStartTime()

    val contestSortedByReminderTime: Flow<List<ContestEntity>> = dao.getAllContestsSortedByReminderTime()

    suspend fun insert(contest: ContestEntity){
        dao.insertContest(contest)
    }
    suspend fun delete(contest: ContestEntity){
        dao.deleteContest(contest)
    }
    suspend fun update(contest: ContestEntity){
        dao.updateContest(contest)
    }
    suspend fun clearAll(){
        dao.clearAll()
    }

}