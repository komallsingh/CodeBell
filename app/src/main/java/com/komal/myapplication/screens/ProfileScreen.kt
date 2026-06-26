package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    username: String = "Developer", // Pass these from your state/database dynamically
    email: String = "developer@codebell.com"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.GradientBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // ── Top Header Bar ──────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(42.dp)
                        .background(AppTheme.BgCard, CircleShape)
                        .drawBehind {
                            drawCircle(
                                color = AppTheme.BorderSubtle,
                                radius = size.minDimension / 2f,
                                style = Stroke(1f)
                            )
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = AppTheme.TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "User Profile",
                    color = AppTheme.TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Avatar Placeholder ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(AppTheme.GradientButton, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = username,
                color = AppTheme.TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Beta Competitor",
                color = AppTheme.AccentTeal,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ── Info Section Heading ────────────────────────────────────────
            Text(
                text = "ACCOUNT DETAILS",
                color = AppTheme.TextMuted,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // ── Credential Info Block ───────────────────────────────────────
            Column(
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
                    .padding(vertical = 8.dp)
            ) {
                // Name Row
                ListItem(
                    headlineContent = { Text("Display Name", color = AppTheme.TextSecondary, fontSize = 12.sp) },
                    supportingContent = { Text(username, color = AppTheme.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium) },
                    leadingContent = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = AppTheme.AccentBlue, modifier = Modifier.size(20.dp))
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )

                HorizontalDivider(color = AppTheme.BorderSubtle, modifier = Modifier.padding(horizontal = 16.dp))

                // Email Row
                ListItem(
                    headlineContent = { Text("Email Address", color = AppTheme.TextSecondary, fontSize = 12.sp) },
                    supportingContent = { Text(email, color = AppTheme.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium) },
                    leadingContent = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = AppTheme.AccentBlue, modifier = Modifier.size(20.dp))
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Logout Action Button ────────────────────────────────────────
            Button(
                onClick = {
                    // Wipes the back-stack clean and drops user completely out to the Welcome Screen
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.35f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Log Out",
                        tint = Color(0xFFF87171),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Log Out",
                        color = Color(0xFFF87171),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(44.dp))
        }
    }
}