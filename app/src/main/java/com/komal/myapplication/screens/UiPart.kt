package com.komal.myapplication.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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

private val AmberYellow       = Color(0xFFF59E0B)
private val AmberYellowBg     = Color(0xFFF59E0B).copy(alpha = 0.11f)
private val AmberYellowBorder = Color(0xFFF59E0B).copy(alpha = 0.40f)

// CONTEST CARD

@Composable
fun ContestCard(
    contest: ContestEntity,
    viewModel: ContestViewModel
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var showActions by remember { mutableStateOf(false) }

    // card container
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.BgCard, RoundedCornerShape(16.dp))
            .drawBehind {
                // outer border
                drawRoundRect(
                    color = AppTheme.BorderSubtle,
                    cornerRadius = CornerRadius(16.dp.toPx()),
                    style = Stroke(1f)
                )
                // left accent stripe
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(AppTheme.AccentBlue, AppTheme.AccentTeal)
                    ),
                    topLeft = Offset(0f, size.height * 0.15f),
                    size = androidx.compose.ui.geometry.Size(3.dp.toPx(), size.height * 0.70f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 16.dp, top = 14.dp, bottom = 14.dp)
        ) {

            // platform badge + date row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .background(AppTheme.AccentBlue.copy(alpha = 0.12f), RoundedCornerShape(5.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = contest.platform.uppercase(),
                        color = AppTheme.AccentBlue,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
                Text(
                    text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                        .format(Date(contest.startTimeMillis)),
                    color = AppTheme.TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(8.dp))

            // contest name
            Text(
                text = contest.name,
                color = AppTheme.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp
            )

            // reminder set indicator — logic unchanged
            if (contest.reminderEnabled) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "🔔 Reminder set",
                    color = Color(0xFF22C55E),
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            // Set Reminder / Reminder Options button — full width, amber bell
            Button(
                onClick = {
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                    )
                    showActions = true   // OPEN SHEET — logic unchanged
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberYellowBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberYellowBorder),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = AmberYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (contest.reminderEnabled) "Reminder Options" else "Set Reminder",
                    color = AmberYellow,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }

    // ContestActionsSheet call — logic unchanged
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
        Text(
            text = title,
            color = AppTheme.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onClose) {
            Icon(Icons.Default.Close, null, tint = AppTheme.TextMuted)
        }
    }
}

@Composable
fun BrushedButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderSubtle),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = AppTheme.BgCard,
            contentColor = AppTheme.TextPrimary
        )
    ) {
        Text(text, fontWeight = FontWeight.Medium)
    }
}