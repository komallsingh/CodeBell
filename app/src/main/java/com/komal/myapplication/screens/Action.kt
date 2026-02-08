package com.komal.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.komal.myapplication.database.ContestEntity
import com.komal.myapplication.reminder.ReminderScheduler
import com.komal.myapplication.viewmodel.ContestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContestActionsSheet(
    contest: ContestEntity,
    viewModel: ContestViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var reminderOffset by remember {
        mutableStateOf(
            when (contest.reminderTimeMillis?.let { contest.startTimeMillis - it }) {
                24 * 60 * 60 * 1000L -> 1440
                60 * 60 * 1000L -> 60
                30 * 60 * 1000L -> 30
                else -> 60
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0B1220), Color(0xFF020617))
                    ),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(20.dp)
        ) {

            Text(
                "Reminder Settings",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            Text("Notify me", color = Color(0xFF94A3B8))

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ReminderChip("Start of Day", reminderOffset == 1440) {
                    reminderOffset = 1440
                }
                ReminderChip("1 Hour Before", reminderOffset == 60) {
                    reminderOffset = 60
                }
                ReminderChip("30 Mins Before", reminderOffset == 30) {
                    reminderOffset = 30
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val reminderTime =
                        contest.startTimeMillis - reminderOffset * 60 * 1000

                    val updated = contest.copy(
                        reminderEnabled = true,
                        reminderTimeMillis = reminderTime
                    )

                    viewModel.updateContest(updated)
                    ReminderScheduler.schedule(
                        context = context,
                        contestName = updated.name,
                        platform = updated.platform,
                        startTime = updated.startTimeMillis
                    )

                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                    )

                    Toast.makeText(
                        context,
                        "Reminder set successfully 🔔",
                        Toast.LENGTH_SHORT
                    ).show()

                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Icon(Icons.Default.Notifications, null)
                Spacer(Modifier.width(8.dp))
                Text("Save Reminder")
            }
        }
    }
}

@Composable
private fun ReminderChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) }
    )
}

