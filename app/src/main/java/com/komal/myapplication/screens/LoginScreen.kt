package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.komal.myapplication.auth.AuthViewModel
import com.komal.myapplication.auth.TokenManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val tokenManager = remember {
        TokenManager(context)
    }

    val authViewModel: AuthViewModel = viewModel()

    val scope = rememberCoroutineScope()

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }
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
            Spacer(modifier = Modifier.height(64.dp))

            // App Branding Circle
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(AppTheme.GradientButton, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Computer,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CodeBell",
                color = AppTheme.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Stay ahead of the game",
                color = AppTheme.TextMuted,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 48.dp)
            )

            // M3 OutlinedTextField with explicitly defined colors
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

            // M3 OutlinedTextField for Password
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

            Spacer(modifier = Modifier.height(32.dp))

            // M3 Filled Button
            Button(
                onClick = {

                    authViewModel.login(
                        email = email,
                        password = password,

                        onSuccess = { token, username, userEmail ->

                            scope.launch {

                                tokenManager.saveAuthData(
                                    token = token,
                                    username = username,
                                    email = userEmail
                                )

                                navController.navigate("home") {

                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            }
                        },

                        onError = {

                            errorMessage = it
                        }
                    )
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
                    text = "Sign In",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // M3 Text Button
            TextButton(
                onClick = { navController.navigate("register") }
            ) {
                Text(
                    text = "New to CodeBell? Register here",
                    color = AppTheme.TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}