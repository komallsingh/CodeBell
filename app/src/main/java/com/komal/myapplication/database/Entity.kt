package com.komal.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="contests")
data class ContestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val platform: String,
    val startTimeMillis: Long,
    val reminderTimeMillis: Long,
    val reminderEnabled: Boolean

)