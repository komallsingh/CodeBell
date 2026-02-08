package com.komal.myapplication.database

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ContestEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase(){
    abstract fun contestDao(): dao

    companion object {
        @Volatile private var INSTANCE: AppDataBase? = null
        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).build().also{INSTANCE= it}
            }
        }
        }
    }