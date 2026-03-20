package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komal.myapplication.database.ContestEntity
import com.komal.myapplication.reminder.ReminderScheduler
import kotlin.math.floor

// ── TimeBox ──────────────────────────────────────────────────────────────────
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

// ── TimeSeparator ─────────────────────────────────────────────────────────────
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

// ── ContestCard (used on HomeScreen with countdown) ───────────────────────────
@Composable
fun ContestCard(contest: ContestEntity, remainingTime: Long) {
    val hours   = floor(remainingTime / 1000 / 3600.0).toInt()
    val minutes = floor((remainingTime / 1000 % 3600) / 60.0).toInt()
    val seconds = (remainingTime / 1000 % 60).toInt()
    val context = LocalContext.current

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
                // subtle corner glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AppTheme.AccentBlue.copy(alpha = 0.07f),
                            Color.Transparent
                        ),
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
                    contest.platform.uppercase(),
                    color = AppTheme.AccentBlue,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                contest.name,
                color = AppTheme.TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 23.sp
            )

            Spacer(Modifier.height(20.dp))

            // countdown
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

            // reminder button — outlined gradient style
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                AppTheme.AccentBlue.copy(alpha = 0.13f),
                                AppTheme.AccentTeal.copy(alpha = 0.08f)
                            )
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                listOf(AppTheme.AccentBlue, AppTheme.AccentTeal)
                            ),
                            cornerRadius = CornerRadius(12.dp.toPx()),
                            style = Stroke(1f)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = {
                        ReminderScheduler.schedule(
                            context     = context,
                            contestId   = contest.id,
                            contestName = contest.name,
                            platform    = contest.platform,
                            startTime   = contest.startTimeMillis
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        tint = AppTheme.AccentBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Set Reminder",
                        color = AppTheme.AccentBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}