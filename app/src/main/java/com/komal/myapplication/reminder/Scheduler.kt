package com.komal.myapplication.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object ReminderScheduler {

    fun schedule(
        context: Context,
        contestId: Int,        // ← NEW: pass ID to avoid hash collision
        contestName: String,
        platform: String,
        startTime: Long
    ) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // START OF DAY (9 AM)
        val startOfDay = Calendar.getInstance().apply {
            timeInMillis = startTime
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        // ONE HOUR BEFORE
        val oneHourBefore = startTime - 60 * 60 * 1000

        scheduleExact(context, alarmManager, contestId, contestName, platform, startOfDay, type = 1)
        scheduleExact(context, alarmManager, contestId, contestName, platform, oneHourBefore, type = 2)
    }

    private fun scheduleExact(
        context: Context,
        alarmManager: AlarmManager,
        contestId: Int,        // ← use ID instead of name
        contestName: String,
        platform: String,
        triggerAt: Long,
        type: Int
    ) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("name", contestName)
            putExtra("platform", platform)
            putExtra("type", type)
        }

        // ← FIXED: use contestId * 10 + type — guaranteed unique, no collision
        val requestCode = contestId * 10 + type

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_IMMUTABLE else 0
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        }
    }
}