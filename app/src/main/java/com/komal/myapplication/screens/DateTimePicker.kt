package com.komal.myapplication.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

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

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.BgSurface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {

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
                "Select Date & Time",
                color = AppTheme.TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Choose when your contest starts",
                color = AppTheme.TextMuted,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(22.dp))

            // Date picker button
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.BgCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderSubtle),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = if (dateMillis != null)
                        "📅  " + SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(dateMillis!!))
                    else "📅  Choose Date",
                    color = if (dateMillis != null) AppTheme.TextPrimary else AppTheme.TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(10.dp))

            // Time picker button
            Button(
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, h, m -> hour = h; minute = m },
                        hour, minute, false
                    ).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.BgCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderSubtle),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = "🕐  %02d:%02d".format(hour, minute),
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(24.dp))

            // Confirm button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(AppTheme.GradientButton, RoundedCornerShape(14.dp))
            ) {
                Button(
                    onClick = {
                        val cal = Calendar.getInstance().apply {
                            timeInMillis = dateMillis ?: return@Button
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                        }
                        onDateTimeSelected(cal.timeInMillis)
                    },
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Confirm", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }

    if (showDatePicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateMillis = state.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK", color = AppTheme.AccentBlue) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = AppTheme.TextSecondary)
                }
            }
        ) {
            DatePicker(state)
        }
    }
}