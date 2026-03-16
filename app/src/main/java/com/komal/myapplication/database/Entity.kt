package com.komal.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contests")
data class ContestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val platform: String,
    val startTimeMillis: Long,
    val durationSeconds: Long = 0L,        // ← NEW: contest duration
    val contestUrl: String = "",            // ← NEW: link to contest
    val reminderTimeMillis: Long = 0L,   // made nullable (was Long before)
    val reminderEnabled: Boolean = false,
    val isManual: Boolean = true,           // ← NEW: false = fetched from API
    val isBookmarked: Boolean = false       // ← NEW: user bookmarked it
)