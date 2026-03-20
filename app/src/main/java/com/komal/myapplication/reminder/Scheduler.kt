package com.komal.myapplication.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object ReminderScheduler {

    /**
     * Schedule a single reminder at [reminderTime] (exact epoch ms).
     * Call this after computing: reminderTime = startTime - offsetMillis
     */
    fun schedule(
        context: Context,
        contestId: Int,
        contestName: String,
        platform: String,
        reminderTime: Long,         // ← exact time to fire the alarm
        offsetLabel: String = ""    // ← "1 Hour Before" etc., shown in notification
    ) {
        if (reminderTime <= System.currentTimeMillis()) return  // already past

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("name", contestName)
            putExtra("platform", platform)
            putExtra("offsetLabel", offsetLabel)
        }

        val requestCode = contestId * 10 + 1   // one alarm per contest

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
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminderTime,
                pendingIntent
            )
        }
    }

    /**
     * Cancel any scheduled reminder for this contest.
     */
    fun cancel(
        context: Context,
        contestId: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = contestId * 10 + 1

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or
                    if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_IMMUTABLE else 0
        ) ?: return   // already cancelled / never set

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}