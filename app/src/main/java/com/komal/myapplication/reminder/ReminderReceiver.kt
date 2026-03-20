package com.komal.myapplication.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val name        = intent.getStringExtra("name")        ?: return
        val platform    = intent.getStringExtra("platform")    ?: ""
        val offsetLabel = intent.getStringExtra("offsetLabel") ?: ""

        val title = when {
            offsetLabel.contains("Day", ignoreCase = true) -> "Contest Today 🚀"
            offsetLabel.contains("Hour", ignoreCase = true) -> "Contest in 1 Hour ⏰"
            offsetLabel.contains("30", ignoreCase = true)  -> "Contest in 30 Minutes ⚡"
            else -> "Upcoming Contest 🔔"
        }

        val body = buildString {
            append(name)
            append(" on ")
            append(platform)
            if (offsetLabel.isNotBlank()) {
                append("  •  ")
                append(offsetLabel)
            }
        }

        NotificationHelper.show(context, title, body)
    }
}