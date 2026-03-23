package com.komal.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

private val AmberYellow = Color(0xFFF59E0B)
private val RedCancel   = Color(0xFFEF4444)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContestActionsSheet(
    contest: ContestEntity,
    viewModel: ContestViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic  = LocalHapticFeedback.current

    // restore previously saved offset, default 60 mins
    var reminderOffset by remember {
        mutableStateOf(
            when (contest.startTimeMillis - contest.reminderTimeMillis) {
                24 * 60 * 60 * 1000L -> 1440
                60 * 60 * 1000L      -> 60
                30 * 60 * 1000L      -> 30
                else                 -> 60
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0B1220), Color(0xFF020617))
                    ),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(20.dp)
        ) {

            // drag handle
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .background(AppTheme.BorderSubtle, RoundedCornerShape(2.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(18.dp))

            Text(
                "Reminder Settings",
                color = AppTheme.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            // show current reminder status
            if (contest.reminderEnabled) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "🔔 Reminder is active",
                    color = Color(0xFF22C55E),
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Notify me",
                color = AppTheme.TextSecondary,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(12.dp))

            // ── Time option chips ────────────────────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    1440 to "Start of Day",
                    60   to "1 Hour Before",
                    30   to "30 Mins Before"
                ).forEach { (offset, label) ->
                    ReminderChip(
                        text = label,
                        selected = reminderOffset == offset,
                        onClick = { reminderOffset = offset }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Save Reminder button ─────────────────────────────────────────
            Button(
                onClick = {
                    // compute exact fire time from the chosen offset
                    val offsetMillis = reminderOffset * 60 * 1000L
                    val reminderTime = contest.startTimeMillis - offsetMillis

                    val offsetLabel = when (reminderOffset) {
                        1440 -> "Start of Day"
                        60   -> "1 Hour Before"
                        30   -> "30 Mins Before"
                        else -> ""
                    }

                    // persist to DB
                    val updated = contest.copy(
                        reminderEnabled    = true,
                        reminderTimeMillis = reminderTime
                    )
                    viewModel.updateContest(updated)

                    // schedule alarm at the exact reminderTime
                    ReminderScheduler.schedule(
                        context      = context,
                        contestId    = updated.id,
                        contestName  = updated.name,
                        platform     = updated.platform,
                        reminderTime = reminderTime,     // ← correct: fire at this time
                        offsetLabel  = offsetLabel
                    )

                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                    )
                    Toast.makeText(context, "Reminder set ✅", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.AccentBlue),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Save Reminder", fontWeight = FontWeight.SemiBold)
            }

            //  Cancel Reminder button (only shown when reminder is active)
            if (contest.reminderEnabled) {
                Spacer(Modifier.height(10.dp))
                OutlinedButton(
                    onClick = {
                        // cancel the alarm
                        ReminderScheduler.cancel(
                            context    = context,
                            contestId  = contest.id
                        )
                        // update DB — clear reminder
                        val updated = contest.copy(
                            reminderEnabled    = false,
                            reminderTimeMillis = 0L
                        )
                        viewModel.updateContest(updated)

                        haptic.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                        )
                        Toast.makeText(context, "Reminder cancelled", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, RedCancel.copy(alpha = 0.5f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = RedCancel.copy(alpha = 0.08f),
                        contentColor   = RedCancel
                    )
                ) {
                    Icon(
                        Icons.Default.NotificationsOff,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Cancel Reminder", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(20.dp))
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
        onClick  = onClick,
        label    = {
            Text(
                text,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AppTheme.AccentBlue,
            selectedLabelColor     = Color.White,
            labelColor             = AppTheme.TextSecondary,
            containerColor         = AppTheme.BgCard
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled             = true,
            selected            = selected,
            borderColor         = AppTheme.BorderSubtle,
            selectedBorderColor = Color.Transparent
        )
    )
}