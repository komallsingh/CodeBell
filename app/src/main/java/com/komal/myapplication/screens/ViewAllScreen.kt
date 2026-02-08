package com.komal.myapplication.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.komal.myapplication.database.ContestEntity
import com.komal.myapplication.reminder.ReminderScheduler
import com.komal.myapplication.viewmodel.ContestViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(navController: NavController) {

    val viewModel: ContestViewModel = viewModel()
    val contests by viewModel.contests.collectAsState()

    var showAddSheet by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    val filtered = contests.filter {
        it.name.contains(search, true) ||
                it.platform.contains(search, true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = Color(0xFF2563EB)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0B1220), Color(0xFF020617))
                    )
                )
        ) {

            // Header
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Upcoming Contests",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Search
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                placeholder = { Text("Search contests") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color(0xFF1E293B),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { contest ->
                    ContestCard(contest,viewModel)
                }
            }
        }
    }

    if (showAddSheet) {
        AddContestBottomSheet(
            onDismiss = { showAddSheet = false },
            onSave = {
                viewModel.insertContest(it)
                showAddSheet = false
            }
        )
    }
}

// CONTEST CARD

@Composable
fun ContestCard(
    contest: ContestEntity,
    viewModel: ContestViewModel
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var showActions by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF020617)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(contest.name, color = Color.White, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(4.dp))

            Text(
                "${contest.platform} • ${
                    SimpleDateFormat(
                        "dd MMM, hh:mm a",
                        Locale.getDefault()
                    ).format(Date(contest.startTimeMillis))
                }",
                color = Color(0xFF94A3B8),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(8.dp))

            if (contest.reminderEnabled) {
                Text(
                    "🔔 Reminder set",
                    color = Color(0xFF22C55E),
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                    )
                    showActions = true   // 👈 OPEN SHEET
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Notifications, null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    if (contest.reminderEnabled)
                        "Reminder Options"
                    else
                        "Set Reminder"
                )
            }
        }
    }

    // ✅ THIS IS WHERE ContestActionsSheet IS CALLED
    if (showActions) {
        ContestActionsSheet(
            contest = contest,
            viewModel = viewModel,
            onDismiss = { showActions = false }
        )
    }
}

// ADD CONTEST SHEET

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContestBottomSheet(
    contest: ContestEntity? = null, // null for Add, non-null for Edit
    onDismiss: () -> Unit,
    onSave: (ContestEntity) -> Unit
) {
    var name by remember { mutableStateOf(contest?.name ?: "") }
    var platform by remember { mutableStateOf(contest?.platform ?: "") }
    var timeMillis by remember { mutableStateOf(contest?.startTimeMillis) }
    var reminderEnabled by remember { mutableStateOf(contest?.reminderEnabled ?: true) }
    var showDateSheet by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault())

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF0B1220), Color(0xFF020617))),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(20.dp)
        ) {

            SheetHeader(
                title = if(contest == null) "Add Manual Contest" else "Edit Contest",
                onClose = onDismiss
            )

            Spacer(Modifier.height(12.dp))

            // Contest Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Contest Name") },
                placeholder = { Text("e.g. Weekly LeetCode Challenge") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Platform/Organizer
            OutlinedTextField(
                value = platform,
                onValueChange = { platform = it },
                label = { Text("Platform / Organizer") },
                placeholder = { Text("e.g. Codeforces, HackerRank") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Date & Time picker
            BrushedButton(
                text = timeMillis?.let { dateFormat.format(Date(it)) } ?: "Select Start Date & Time",
                onClick = { showDateSheet = true }
            )

            Spacer(Modifier.height(12.dp))

            // Automatic Reminder
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF2563EB))
                Spacer(Modifier.width(8.dp))
                Text("Set Reminder Automatically", color = Color.White, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = reminderEnabled,
                    onCheckedChange = { reminderEnabled = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2563EB))
                )
            }

            Spacer(Modifier.height(24.dp))

            // Cancel & Add / Update buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancel") }

                Button(
                    onClick = {
                        if (timeMillis != null && name.isNotBlank() && platform.isNotBlank()) {
                            onSave(
                                ContestEntity(
                                    id = contest?.id ?: 0,
                                    name = name,
                                    platform = platform,
                                    startTimeMillis = timeMillis!!,
                                    reminderEnabled = reminderEnabled,
                                    reminderTimeMillis = timeMillis!! - 30*60*1000
                                )
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text(if(contest == null) "Add Contest" else "Update Contest", color = Color.White)
                }
            }
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



/* -------------------- DATE TIME PICKER -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerSheet(
    onDismiss: () -> Unit,
    onDateTimeSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    var dateMillis by remember { mutableStateOf<Long?>(null) }
    var hour by remember { mutableStateOf(12) }
    var minute by remember { mutableStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(20.dp)) {

            SheetHeader("Select Date & Time", onDismiss)

            BrushedButton("Select Date") { showDatePicker = true }

            Spacer(Modifier.height(12.dp))

            BrushedButton("Select Time") {
                TimePickerDialog(
                    context,
                    { _, h, m -> hour = h; minute = m },
                    hour, minute, false
                ).show()
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val cal = Calendar.getInstance().apply {
                        timeInMillis = dateMillis ?: return@Button
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }
                    onDateTimeSelected(cal.timeInMillis)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm")
            }
        }
    }

    if (showDatePicker) {
        val state = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateMillis = state.selectedDateMillis
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state)
        }
    }
}

/* -------------------- REUSABLE UI -------------------- */

@Composable
fun SheetHeader(title: String, onClose: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onClose) {
            Icon(Icons.Default.Close, null, tint = Color.Gray)
        }
    }
}

@Composable
fun BrushedButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.horizontalGradient(
                listOf(Color(0xFF1E293B), Color(0xFF334155))
            )
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFF020617),
            contentColor = Color.White
        )
    ) {
        Text(text)
    }
}


