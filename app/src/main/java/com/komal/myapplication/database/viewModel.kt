package com.komal.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.komal.myapplication.database.AppDataBase
import com.komal.myapplication.database.ContestEntity
import com.komal.myapplication.database.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repo

    val contests: StateFlow<List<ContestEntity>>

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching

    private val _fetchError = MutableStateFlow<String?>(null)
    val fetchError: StateFlow<String?> = _fetchError

    init {
        val dao = AppDataBase.getDatabase(application).contestDao()
        repository = Repo(dao)
        contests = repository.allContest
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
    }

    /**
     * Insert a contest and return the real Room-assigned ID via [onInserted].
     * Use this when you need to schedule a reminder right after insert.
     */
    fun insertContest(contest: ContestEntity, onInserted: (realId: Long) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val realId = repository.insert(contest)   // ← Long returned by Room
                onInserted(realId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteContest(contest: ContestEntity) {
        viewModelScope.launch {
            try { repository.delete(contest) }
            catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun updateContest(contest: ContestEntity) {
        viewModelScope.launch {
            try { repository.update(contest) }
            catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            try { repository.clearAll() }
            catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun fetchApiContests() {
        viewModelScope.launch {
            _isFetching.value = true
            _fetchError.value = null
            try {
                repository.fetchAndStoreApiContests()
            } catch (e: Exception) {
                _fetchError.value = "Failed to fetch contests. Check your internet."
                e.printStackTrace()
            } finally {
                _isFetching.value = false
            }
        }
    }

    fun toggleBookmark(contest: ContestEntity) {
        viewModelScope.launch {
            try { repository.toggleBookmark(contest) }
            catch (e: Exception) { e.printStackTrace() }
        }
    }
}