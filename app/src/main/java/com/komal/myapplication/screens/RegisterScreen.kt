package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.GradientBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Create Account",
                color = AppTheme.TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Join the developer arena",
                color = AppTheme.TextMuted,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 36.dp)
            )

            // Dynamic Prompt: "What should we call you?"
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("What should we call you?") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.AccentBlue,
                    unfocusedBorderColor = AppTheme.BorderSubtle,
                    focusedContainerColor = AppTheme.BgCard,
                    unfocusedContainerColor = AppTheme.BgCard,
                    focusedTextColor = AppTheme.TextPrimary,
                    unfocusedTextColor = AppTheme.TextPrimary,
                    focusedLabelColor = AppTheme.AccentBlue,
                    unfocusedLabelColor = AppTheme.TextSecondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // M3 Email Input Block
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.AccentBlue,
                    unfocusedBorderColor = AppTheme.BorderSubtle,
                    focusedContainerColor = AppTheme.BgCard,
                    unfocusedContainerColor = AppTheme.BgCard,
                    focusedTextColor = AppTheme.TextPrimary,
                    unfocusedTextColor = AppTheme.TextPrimary,
                    focusedLabelColor = AppTheme.AccentBlue,
                    unfocusedLabelColor = AppTheme.TextSecondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // M3 Password Input Block
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.AccentBlue,
                    unfocusedBorderColor = AppTheme.BorderSubtle,
                    focusedContainerColor = AppTheme.BgCard,
                    unfocusedContainerColor = AppTheme.BgCard,
                    focusedTextColor = AppTheme.TextPrimary,
                    unfocusedTextColor = AppTheme.TextPrimary,
                    focusedLabelColor = AppTheme.AccentBlue,
                    unfocusedLabelColor = AppTheme.TextSecondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // M3 Confirm Password Input Block
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.AccentBlue,
                    unfocusedBorderColor = AppTheme.BorderSubtle,
                    focusedContainerColor = AppTheme.BgCard,
                    unfocusedContainerColor = AppTheme.BgCard,
                    focusedTextColor = AppTheme.TextPrimary,
                    unfocusedTextColor = AppTheme.TextPrimary,
                    focusedLabelColor = AppTheme.AccentBlue,
                    unfocusedLabelColor = AppTheme.TextSecondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // M3 Registration Action Button
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.AccentBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Toggle Button
            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text(
                    text = "Already have an account? Sign In",
                    color = AppTheme.TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}