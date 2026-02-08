package com.komal.myapplication.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.komal.myapplication.R

//class ReminderReceiver : BroadcastReceiver(){
//    override fun onReceive(context: Context, intent: Intent){
//        val contestName=intent.getStringExtra("name")?:"Contest"
//        val platform=intent.getStringExtra("platform")?:""
//
//        val notification= NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setContentTitle(contestName)
//            .setContentText(platform)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .build()
//        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(),notification)
//
//
//    }
//}

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val name = intent.getStringExtra("name") ?: return
        val platform = intent.getStringExtra("platform") ?: ""
        val type = intent.getIntExtra("type", 1)

        val title = if (type == 1)
            "Contest Today 🚀"
        else
            "Contest Starting Soon ⏰"

        val text = if (type == 1)
            "$name on $platform today"
        else
            "$name starts in 1 hour"

        NotificationHelper.show(context, title, text)
    }
}
