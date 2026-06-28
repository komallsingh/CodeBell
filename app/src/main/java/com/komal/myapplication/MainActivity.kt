package com.komal.myapplication

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.komal.myapplication.auth.TokenManager
import com.komal.myapplication.navigation.AppNavigation
import com.komal.myapplication.reminder.NotificationHelper
import com.komal.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    // Runtime permission launcher for POST_NOTIFICATIONS (Android 13+)
    private val notifPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* granted or denied — user decision, nothing more to do here */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create notification channel early so it exists before any alarm fires
        NotificationHelper.createChannel(this)

        // Request POST_NOTIFICATIONS on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val alreadyGranted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!alreadyGranted) {
                notifPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val tokenManager = remember { TokenManager(context) }

                val token by tokenManager
                    .tokenFlow
                    .collectAsState(initial = null)
                val navController = rememberNavController()
                AppNavigation(navController, isLoggedIn = !token.isNullOrEmpty())
            }
        }
    }
}