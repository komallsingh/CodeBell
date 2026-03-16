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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komal.myapplication.database.ContestEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContestBottomSheet(
    contest: ContestEntity? = null,
    onDismiss: () -> Unit,
    onSave: (ContestEntity) -> Unit
) {
    var name by remember { mutableStateOf(contest?.name ?: "") }
    var platform by remember { mutableStateOf(contest?.platform ?: "") }
    var timeMillis by remember { mutableStateOf(contest?.startTimeMillis) }
    var reminderEnabled by remember { mutableStateOf(contest?.reminderEnabled ?: true) }
    var showDateSheet by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy  hh:mm a", Locale.getDefault())

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF3B82F6),
        unfocusedBorderColor = Color(0xFF1E293B),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color(0xFF3B82F6),
        focusedLabelColor = Color(0xFF3B82F6),
        unfocusedLabelColor = Color(0xFF475569),
        focusedPlaceholderColor = Color(0xFF334155),
        unfocusedPlaceholderColor = Color(0xFF1E293B),
        focusedContainerColor = Color(0xFF0A1628),
        unfocusedContainerColor = Color(0xFF0A1628)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .background(Color(0xFF1E293B), RoundedCornerShape(2.dp))
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
                title = if (contest == null) "Add Contest" else "Edit Contest",
                onClose = onDismiss
            )

            Spacer(Modifier.height(4.dp))

            Text(
                if (contest == null) "Add a manual contest to track"
                else "Update contest details",
                color = Color(0xFF475569),
                fontSize = 13.sp
            )

            Spacer(Modifier.height(20.dp))

            // Contest Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Contest Name") },
                placeholder = { Text("e.g. Weekly LeetCode Challenge") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            // Platform
            OutlinedTextField(
                value = platform,
                onValueChange = { platform = it },
                label = { Text("Platform / Organizer") },
                placeholder = { Text("e.g. Codeforces, HackerRank") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            // Date picker button
            OutlinedButton(
                onClick = { showDateSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (timeMillis != null) Color(0xFF3B82F6).copy(alpha = 0.5f)
                    else Color(0xFF1E293B)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFF0A1628),
                    contentColor = if (timeMillis != null) Color.White else Color(0xFF475569)
                )
            ) {
                Text(
                    timeMillis?.let { dateFormat.format(Date(it)) }
                        ?: "📅  Select Start Date & Time",
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // Reminder toggle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0A1628), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                Color(0xFF2563EB).copy(alpha = 0.15f),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Set Reminder",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            "Notify 30 minutes before",
                            color = Color(0xFF475569),
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF2563EB),
                            uncheckedThumbColor = Color(0xFF475569),
                            uncheckedTrackColor = Color(0xFF1E293B)
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, Color(0xFF1E293B)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF94A3B8)
                    )
                ) {
                    Text("Cancel", fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        if (timeMillis != null &&
                            name.isNotBlank() &&
                            platform.isNotBlank()
                        ) {
                            onSave(
                                ContestEntity(
                                    id = contest?.id ?: 0,
                                    name = name.trim(),
                                    platform = platform.trim(),
                                    startTimeMillis = timeMillis!!,
                                    reminderEnabled = reminderEnabled,
                                    reminderTimeMillis = if (reminderEnabled)
                                        timeMillis!! - 30L * 60L * 1000L else 0L,
                                    isManual = true,
                                    isBookmarked = false
                                )
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB),
                        disabledContainerColor = Color(0xFF1E293B)
                    ),
                    enabled = name.isNotBlank() &&
                            platform.isNotBlank() &&
                            timeMillis != null
                ) {
                    Text(
                        if (contest == null) "Add Contest" else "Update",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (showDateSheet) {
        DateTimePickerSheet(
            onDismiss = { showDateSheet = false },
            onDateTimeSelected = {
                timeMillis = it
                showDateSheet = false
            }
        )
    }
}