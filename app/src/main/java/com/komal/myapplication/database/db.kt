package com.komal.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ContestEntity::class],
    version = 2,                           // ← bumped from 1 to 2
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun contestDao(): dao

    companion object {
        @Volatile private var INSTANCE: AppDataBase? = null

        // Migration from version 1 to 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE contests ADD COLUMN durationSeconds INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE contests ADD COLUMN contestUrl TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE contests ADD COLUMN isManual INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE contests ADD COLUMN isBookmarked INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()  // ← replace addMigrations with this
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}