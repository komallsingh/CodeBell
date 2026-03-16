package com.komal.myapplication.screens

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.komal.myapplication.viewmodel.ContestViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                    showActions = true   //  OPEN SHEET
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

    // THIS IS WHERE ContestActionsSheet IS CALLED
    if (showActions) {
        ContestActionsSheet(
            contest = contest,
            viewModel = viewModel,
            onDismiss = { showActions = false }
        )
    }
}




//REUSABLE UI
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


