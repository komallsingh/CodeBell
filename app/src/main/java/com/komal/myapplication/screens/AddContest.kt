package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komal.myapplication.database.ContestEntity
import com.komal.myapplication.reminder.ReminderScheduler
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContestBottomSheet(
    contest: ContestEntity? = null,
    onDismiss: () -> Unit,
    onSave: (ContestEntity) -> Unit
) {
    val context = LocalContext.current

    var name            by remember { mutableStateOf(contest?.name ?: "") }
    var platform        by remember { mutableStateOf(contest?.platform ?: "") }
    var timeMillis      by remember { mutableStateOf(contest?.startTimeMillis) }
    var reminderEnabled by remember { mutableStateOf(contest?.reminderEnabled ?: true) }
    var showDateSheet   by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy  hh:mm a", Locale.getDefault())

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor      = AppTheme.AccentBlue,
        unfocusedBorderColor    = AppTheme.BorderSubtle,
        focusedTextColor        = AppTheme.TextPrimary,
        unfocusedTextColor      = AppTheme.TextPrimary,
        cursorColor             = AppTheme.AccentBlue,
        focusedLabelColor       = AppTheme.AccentBlue,
        unfocusedLabelColor     = AppTheme.TextSecondary,
        focusedPlaceholderColor = AppTheme.TextMuted,
        unfocusedPlaceholderColor = AppTheme.TextMuted,
        focusedContainerColor   = AppTheme.BgCard,
        unfocusedContainerColor = AppTheme.BgCard
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .background(AppTheme.BorderSubtle, RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0D1B2E), Color(0xFF020817))
                    ),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            SheetHeader(
                title   = if (contest == null) "Add Contest" else "Edit Contest",
                onClose = onDismiss
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text  = if (contest == null) "Add a manual contest to track" else "Update contest details",
                color = AppTheme.TextMuted,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(20.dp))

            // ── Contest Name ─────────────────────────────────────────────────
            OutlinedTextField(
                value         = name,
                onValueChange = { name = it },
                label         = { Text("Contest Name") },
                placeholder   = { Text("e.g. Weekly LeetCode Challenge") },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = textFieldColors,
                singleLine    = true
            )

            Spacer(Modifier.height(12.dp))

            // ── Platform ─────────────────────────────────────────────────────
            OutlinedTextField(
                value         = platform,
                onValueChange = { platform = it },
                label         = { Text("Platform / Organizer") },
                placeholder   = { Text("e.g. Codeforces, HackerRank") },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = textFieldColors,
                singleLine    = true
            )

            Spacer(Modifier.height(12.dp))

            // ── Date & Time picker ───────────────────────────────────────────
            OutlinedButton(
                onClick = { showDateSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (timeMillis != null) AppTheme.AccentBlue.copy(alpha = 0.5f)
                    else AppTheme.BorderSubtle
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = AppTheme.BgCard,
                    contentColor   = if (timeMillis != null) AppTheme.TextPrimary else AppTheme.TextMuted
                )
            ) {
                Text(
                    text     = timeMillis?.let { "📅  " + dateFormat.format(Date(it)) } ?: "📅  Select Start Date & Time",
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Reminder toggle ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.BgCard, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                AppTheme.AccentBlue.copy(alpha = 0.15f),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint     = AppTheme.AccentBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Set Reminder",
                            color      = AppTheme.TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp
                        )
                        Text(
                            "Notify 30 minutes before",
                            color    = AppTheme.TextMuted,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked         = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor   = Color.White,
                            checkedTrackColor   = AppTheme.AccentBlue,
                            uncheckedThumbColor = AppTheme.TextMuted,
                            uncheckedTrackColor = AppTheme.BorderSubtle
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Action buttons ───────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = onDismiss,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderSubtle),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = AppTheme.TextSecondary)
                ) {
                    Text("Cancel", fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        val t = timeMillis ?: return@Button
                        if (name.isBlank() || platform.isBlank()) return@Button

                        // 30-min offset for the quick-add reminder
                        val reminderTime = if (reminderEnabled) t - 30L * 60L * 1000L else 0L

                        val entity = ContestEntity(
                            id                 = contest?.id ?: 0,
                            name               = name.trim(),
                            platform           = platform.trim(),
                            startTimeMillis    = t,
                            reminderEnabled    = reminderEnabled,
                            reminderTimeMillis = reminderTime,
                            isManual           = true,
                            isBookmarked       = false
                        )

                        // actually schedule the alarm if toggle is on
                        if (reminderEnabled && reminderTime > System.currentTimeMillis()) {
                            ReminderScheduler.schedule(
                                context      = context,
                                contestId    = entity.id.takeIf { it != 0 } ?: name.hashCode(),
                                contestName  = entity.name,
                                platform     = entity.platform,
                                reminderTime = reminderTime,
                                offsetLabel  = "30 Mins Before"
                            )
                        }

                        onSave(entity)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = AppTheme.AccentBlue,
                        disabledContainerColor = AppTheme.BorderSubtle
                    ),
                    enabled = name.isNotBlank() && platform.isNotBlank() && timeMillis != null
                ) {
                    Text(
                        text       = if (contest == null) "Add Contest" else "Update",
                        color      = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (showDateSheet) {
        DateTimePickerSheet(
            onDismiss           = { showDateSheet = false },
            onDateTimeSelected  = {
                timeMillis    = it
                showDateSheet = false
            }
        )
    }
}