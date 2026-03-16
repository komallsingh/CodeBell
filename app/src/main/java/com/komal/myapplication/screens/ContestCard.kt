package com.komal.myapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komal.myapplication.reminder.ReminderScheduler
import kotlin.math.floor

@Composable
fun ContestCard(contest: com.komal.myapplication.database.ContestEntity, remainingTime: Long) {
    val hours = floor(remainingTime / 1000 / 3600.0).toInt()
    val minutes = floor((remainingTime / 1000 % 3600) / 60.0).toInt()
    val seconds = (remainingTime / 1000 % 60).toInt()
    val context=LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF020617))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(contest.platform.uppercase(), color = Color(0xFF94A3B8), fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            Text(contest.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TimeBox(hours, "HOURS")
                TimeBox(minutes, "MINS")
                TimeBox(seconds, "SECS")
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { ReminderScheduler.schedule(
                    context = context,
                    contestId = contest.id,        // ← add this
                    contestName = contest.name,
                    platform = contest.platform,
                    startTime = contest.startTimeMillis
                ) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Set Reminder")
            }
        }
    }
}