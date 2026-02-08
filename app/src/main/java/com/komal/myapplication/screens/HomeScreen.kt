package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.komal.myapplication.reminder.ReminderScheduler
import com.komal.myapplication.viewmodel.ContestViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ContestViewModel = viewModel()
) {
    val contests by viewModel.contests.collectAsState()

    // Auto-remove past contests
    LaunchedEffect(contests) {
        val now = System.currentTimeMillis()
        contests.filter { it.startTimeMillis <= now }.forEach { pastContest ->
            viewModel.deleteContest(pastContest)
        }
    }

    val now = System.currentTimeMillis()
    val upcomingContests = contests
        .filter { it.startTimeMillis > now }
        .sortedBy { it.startTimeMillis }

    val nextContest = upcomingContests.firstOrNull()
    val upcomingList = upcomingContests.drop(1)

    // Live countdown for next contest
    var remainingTime by remember { mutableStateOf(0L) }
    LaunchedEffect(nextContest) {
        while (true) {
            nextContest?.let {
                val diff = it.startTimeMillis - System.currentTimeMillis()
                remainingTime = if (diff > 0) diff else 0L
            }
            delay(1000L)
        }
    }

    val background = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), Color(0xFF020617))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().offset(y=50.dp),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF2563EB), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "ContestTracker",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(Modifier.height(80.dp))

        Text(
            text = "Next Major Contest",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        if (nextContest != null) {
            ContestCard(nextContest, remainingTime)
        } else {
            Text(
                text = "No upcoming contests",
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(24.dp))

        // Upcoming Challenges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Challenges",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            //Text(text = "View All", color = Color(0xFF60A5FA))
        }

        Spacer(Modifier.height(12.dp))

        if (upcomingList.isEmpty()) {
            Text(text = "No upcoming challenges", color = Color.Gray)
        } else {
            upcomingList.forEach { contest ->
                var timeLeft by remember { mutableStateOf(contest.startTimeMillis - System.currentTimeMillis()) }
                LaunchedEffect(contest.startTimeMillis) {
                    while (timeLeft > 0) {
                        timeLeft = contest.startTimeMillis - System.currentTimeMillis()
                        delay(1000L)
                    }
                }

                UpcomingItem(
                    title = contest.name,
                    subtitle = "${contest.platform} • ${
                        SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                            .format(Date(contest.startTimeMillis))
                    }",
                    remainingTime = timeLeft
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        OutlinedButton(
            onClick = { navController.navigate("view all") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Text("Explore Calendar", color = Color.White)
        }
    }
}

@Composable
fun ContestCard(contest: com.komal.myapplication.database.ContestEntity, remainingTime: Long) {
    val hours = floor(remainingTime / 1000 / 3600.0).toInt()
    val minutes = floor((remainingTime / 1000 % 3600) / 60.0).toInt()
    val seconds = (remainingTime / 1000 % 60).toInt()
val context=LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF020617))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(contest.platform.uppercase(), color = Color(0xFF94A3B8), fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            Text(contest.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TimeBox(hours, "HOURS")
                TimeBox(minutes, "MINS")
                TimeBox(seconds, "SECS")
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { ReminderScheduler.schedule(
                    context = context,
                    contestName = contest.name,
                    platform = contest.platform,
                    startTime = contest.startTimeMillis
                ) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Set Reminder")
            }
        }
    }
}

@Composable
fun UpcomingItem(title: String, subtitle: String, remainingTime: Long = 0L) {
    val hours = floor(remainingTime / 1000 / 3600.0).toInt()
    val minutes = floor((remainingTime / 1000 % 3600) / 60.0).toInt()
    val seconds = (remainingTime / 1000 % 60).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF020617))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF22C55E), Color(0xFF3B82F6))
                            ),
                            RoundedCornerShape(12.dp)
                        )

                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, color = Color(0xFF94A3B8), fontSize = 12.sp)
                }
                Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = Color.White)
            }

            if (remainingTime > 0) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TimeBox(hours, "HRS", Modifier.weight(1f))
                    Spacer(Modifier.width(6.dp))
                    TimeBox(minutes, "MIN", Modifier.weight(1f))
                    Spacer(Modifier.width(6.dp))
                    TimeBox(seconds, "SEC", Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TimeBox(value: Int, label: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(0xFF0F172A), RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp)
    ) {
        Text(value.toString().padStart(2, '0'), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color(0xFF94A3B8), fontSize = 10.sp)
    }
}
