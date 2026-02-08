package com.komal.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.komal.myapplication.database.AppDataBase
import com.komal.myapplication.database.ContestEntity
import com.komal.myapplication.database.Repo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repo

    val contests: StateFlow<List<ContestEntity>>

    init {
        val dao = AppDataBase
            .getDatabase(application)
            .contestDao()

        repository = Repo(dao)

        contests = repository.allContest
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
    }

    fun insertContest(contest: ContestEntity) {
        viewModelScope.launch {
            repository.insert(contest)
        }
    }

    fun deleteContest(contest: ContestEntity) {
        viewModelScope.launch {
            repository.delete(contest)
        }
    }

    fun updateContest(contest: ContestEntity) {
        viewModelScope.launch {
            repository.update(contest)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
