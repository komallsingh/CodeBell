package com.komal.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komal.myapplication.database.ContestEntity
import com.komal.myapplication.reminder.ReminderScheduler
import com.komal.myapplication.viewmodel.ContestViewModel
import kotlin.math.floor

private val AmberYellow = Color(0xFFF59E0B)

@Composable
fun UpcomingItem(
    contest: ContestEntity,              //pass full entity instead of strings
    remainingTime: Long = 0L,
    viewModel: ContestViewModel
) {
    val context = LocalContext.current
    val haptic  = LocalHapticFeedback.current

    val hours   = floor(remainingTime / 1000 / 3600.0).toInt()
    val minutes = floor((remainingTime / 1000 % 3600) / 60.0).toInt()
    val seconds = (remainingTime / 1000 % 60).toInt()

    //initialise from DB so it survives recomposition
    var reminderActive by remember(contest.id) {
        mutableStateOf(contest.reminderEnabled)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(AppTheme.BgCard, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                // gradient icon box
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF22C55E), Color(0xFF3B82F6))
                            ),
                            RoundedCornerShape(12.dp)
                        )
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        contest.name,
                        color = AppTheme.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${contest.platform} • ${
                            java.text.SimpleDateFormat("dd MMM, hh:mm a", java.util.Locale.getDefault())
                                .format(java.util.Date(contest.startTimeMillis))
                        }",
                        color = AppTheme.TextSecondary,
                        fontSize = 12.sp
                    )
                }

                //Bell icon — grey when off, amber when on
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                        )
                        if (reminderActive) {
                            // cancel reminder
                            ReminderScheduler.cancel(
                                context   = context,
                                contestId = contest.id
                            )
                            viewModel.updateContest(
                                contest.copy(
                                    reminderEnabled    = false,
                                    reminderTimeMillis = 0L
                                )
                            )
                            reminderActive = false
                            Toast.makeText(context, "Reminder cancelled", Toast.LENGTH_SHORT).show()
                        } else {
                            // schedule 1 hour before
                            val reminderTime = contest.startTimeMillis - 60 * 60 * 1000L
                            if (reminderTime > System.currentTimeMillis()) {
                                ReminderScheduler.schedule(
                                    context      = context,
                                    contestId    = contest.id,
                                    contestName  = contest.name,
                                    platform     = contest.platform,
                                    reminderTime = reminderTime,
                                    offsetLabel  = "1 Hour Before"
                                )
                                viewModel.updateContest(
                                    contest.copy(
                                        reminderEnabled    = true,
                                        reminderTimeMillis = reminderTime
                                    )
                                )
                                reminderActive = true
                                Toast.makeText(context, "Reminder set ✅ 1hr before", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Contest starts in less than 1 hour", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (reminderActive)
                            Icons.Default.Notifications
                        else
                            Icons.Default.NotificationsNone,
                        contentDescription = "Toggle reminder",
                        tint = if (reminderActive) AmberYellow else AppTheme.TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            //countdown boxes
            if (remainingTime > 0) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    UpcomingTimeBox(hours,   "HRS", Modifier.weight(1f))
                    Spacer(Modifier.width(6.dp))
                    UpcomingTimeBox(minutes, "MIN", Modifier.weight(1f))
                    Spacer(Modifier.width(6.dp))
                    UpcomingTimeBox(seconds, "SEC", Modifier.weight(1f))
                }
            }
        }
    }
}

//Renamed to avoid conflict with TimeBox in ContestCard.kt
@Composable
fun UpcomingTimeBox(value: Int, label: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(AppTheme.BgSurface, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp)
    ) {
        Text(
            value.toString().padStart(2, '0'),
            color = AppTheme.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(label, color = AppTheme.TextMuted, fontSize = 10.sp)
    }
}