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
        contestName: String,
        platform: String,
        startTime: Long
    ) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 1️⃣ START OF DAY (9 AM)
        val startOfDay = Calendar.getInstance().apply {
            timeInMillis = startTime
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        // 2️⃣ ONE HOUR BEFORE
        val oneHourBefore = startTime - 60 * 60 * 1000

        scheduleExact(context, alarmManager, contestName, platform, startOfDay, 1)
        scheduleExact(context, alarmManager, contestName, platform, oneHourBefore, 2)
    }

    private fun scheduleExact(
        context: Context,
        alarmManager: AlarmManager,
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

        val requestCode = (contestName + type).hashCode()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= 31)
                        PendingIntent.FLAG_MUTABLE else 0
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
