package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komal.myapplication.database.ContestEntity
import com.komal.myapplication.viewmodel.ContestViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

private val AmberYellow       = Color(0xFFF59E0B)
private val AmberYellowBg     = Color(0xFFF59E0B).copy(alpha = 0.11f)
private val AmberYellowBorder = Color(0xFFF59E0B).copy(alpha = 0.40f)

// ═════════════════════════════════════════════════════════════════════════════
//  TimeBox + TimeSeparator
// ═════════════════════════════════════════════════════════════════════════════

@Composable
fun TimeBox(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(width = 70.dp, height = 56.dp)
                .background(AppTheme.BgSurface, RoundedCornerShape(12.dp))
                .drawBehind {
                    drawRoundRect(
                        color = AppTheme.BorderSubtle,
                        cornerRadius = CornerRadius(12.dp.toPx()),
                        style = Stroke(1f)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value.toString().padStart(2, '0'),
                color = AppTheme.TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(5.dp))
        Text(
            text = label,
            color = AppTheme.TextMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun TimeSeparator() {
    Text(
        text = ":",
        color = AppTheme.AccentBlue.copy(alpha = 0.55f),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 18.dp)
    )
}


//  ContestCard — overload 1: HomeScreen featured card (with countdown)
//  "Set Reminder" opens ContestActionsSheet so the user picks an offset


@Composable
fun ContestCard(contest: ContestEntity, remainingTime: Long) {
    val hours   = floor(remainingTime / 1000 / 3600.0).toInt()
    val minutes = floor((remainingTime / 1000 % 3600) / 60.0).toInt()
    val seconds = (remainingTime / 1000 % 60).toInt()

    // We need viewModel to pass to ContestActionsSheet — get it from the call site
    // via a separate composable that wraps this. But to keep the signature identical
    // to what HomeScreen already calls (contest, remainingTime), we lift the sheet
    // state locally and require viewModel via viewModel() — safe because HomeScreen
    // is always inside a ViewModelStoreOwner.
    val viewModel: ContestViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()

    var showActions by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.BgCard, RoundedCornerShape(20.dp))
            .drawBehind {
                drawRoundRect(
                    brush = Brush.linearGradient(
                        listOf(
                            AppTheme.AccentBlue.copy(alpha = 0.40f),
                            AppTheme.AccentTeal.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style = Stroke(1.2f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(AppTheme.AccentBlue.copy(alpha = 0.07f), Color.Transparent),
                        center = Offset(0f, 0f),
                        radius = size.width * 0.55f
                    )
                )
            }
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // platform badge
            Box(
                modifier = Modifier
                    .background(AppTheme.AccentBlue.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = contest.platform.uppercase(),
                    color = AppTheme.AccentBlue,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = contest.name,
                color = AppTheme.TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 23.sp
            )

            // reminder active indicator
            if (contest.reminderEnabled) {
                Spacer(Modifier.height(4.dp))
                Text("🔔 Reminder set", color = Color(0xFF22C55E), fontSize = 12.sp)
            }

            Spacer(Modifier.height(20.dp))

            // countdown row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeBox(hours, "HRS")
                Spacer(Modifier.width(6.dp))
                TimeSeparator()
                Spacer(Modifier.width(6.dp))
                TimeBox(minutes, "MIN")
                Spacer(Modifier.width(6.dp))
                TimeSeparator()
                Spacer(Modifier.width(6.dp))
                TimeBox(seconds, "SEC")
            }

            Spacer(Modifier.height(20.dp))

            // Set Reminder / Reminder Options — opens ContestActionsSheet
            Button(
                onClick = {
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                    )
                    showActions = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberYellowBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberYellowBorder),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
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
                    fontSize = 14.sp
                )
            }
        }
    }

    if (showActions) {
        ContestActionsSheet(
            contest   = contest,
            viewModel = viewModel,
            onDismiss = { showActions = false }
        )
    }
}

//  ContestListCard — ViewAllScreen list card


@Composable
fun ContestListCard(contest: ContestEntity, viewModel: ContestViewModel) {
    val haptic    = LocalHapticFeedback.current
    val dateLabel = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        .format(Date(contest.startTimeMillis))

    var showActions by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.BgCard, RoundedCornerShape(16.dp))
            .drawBehind {
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
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
                    text = dateLabel,
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

            // reminder active indicator
            if (contest.reminderEnabled) {
                Spacer(Modifier.height(4.dp))
                Text("🔔 Reminder set", color = Color(0xFF22C55E), fontSize = 12.sp)
            }

            Spacer(Modifier.height(14.dp))

            // Set Reminder / Reminder Options — full width, amber bell
            Button(
                onClick = {
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                    )
                    showActions = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberYellowBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberYellowBorder),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
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

    // ContestActionsSheet — logic unchanged
    if (showActions) {
        ContestActionsSheet(
            contest   = contest,
            viewModel = viewModel,
            onDismiss = { showActions = false }
        )
    }
}