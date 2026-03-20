package com.komal.myapplication.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


object AppTheme {
    val BgDeep        = Color(0xFF060D18)
    val BgSurface     = Color(0xFF0C1628)
    val BgCard        = Color(0xFF0F1E35)
    val AccentBlue    = Color(0xFF3B82F6)
    val AccentTeal    = Color(0xFF06B6D4)
    val TextPrimary   = Color(0xFFF0F6FF)
    val TextSecondary = Color(0xFF8CA3C0)
    val TextMuted     = Color(0xFF3E5170)
    val BorderSubtle  = Color(0xFF1A2E4A)

    val GradientBg = Brush.verticalGradient(
        colors = listOf(BgDeep, Color(0xFF040A14))
    )
    val GradientAccent = Brush.linearGradient(
        colors = listOf(AccentBlue, AccentTeal)
    )
    val GradientButton = Brush.horizontalGradient(
        colors = listOf(Color(0xFF2563EB), Color(0xFF0EA5E9))
    )
}


@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.GradientBg)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF1D4ED8).copy(alpha = 0.16f), Color.Transparent),
                        center = Offset(size.width / 2f, size.height * 0.28f),
                        radius = size.width * 0.7f
                    )
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(210.dp))

            // ── Logo badge ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF1E3A5F), Color(0xFF0F2240))),
                        RoundedCornerShape(20.dp)
                    )
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                listOf(
                                    AppTheme.AccentBlue.copy(alpha = 0.55f),
                                    AppTheme.AccentTeal.copy(alpha = 0.25f)
                                )
                            ),
                            cornerRadius = CornerRadius(20.dp.toPx()),
                            style = Stroke(1.2f)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "App Icon",
                    tint = AppTheme.AccentBlue,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "CONTEST TRACKER",
                color = AppTheme.AccentBlue.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Track. Prepare.\nCompete.",
                textAlign = TextAlign.Center,
                color = AppTheme.TextPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Never miss a coding contest again.\nAll platforms, one place.",
                textAlign = TextAlign.Center,
                color = AppTheme.TextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // ── Quote card ──────────────────────────────────────────────────
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
                    }
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .size(width = 28.dp, height = 3.dp)
                            .background(AppTheme.GradientAccent, RoundedCornerShape(2.dp))
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "\"The only way to learn a new programming language is by writing programs in it.\"",
                        color = AppTheme.TextSecondary,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "— Dennis Ritchie",
                        color = AppTheme.AccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Get Started button ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(AppTheme.GradientButton, RoundedCornerShape(14.dp))
            ) {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Codeforces · LeetCode · CodeChef",
                color = AppTheme.TextMuted,
                fontSize = 11.sp,
                letterSpacing = 0.3.sp
            )
            Spacer(modifier = Modifier.height(44.dp))
        }
    }
}