package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.komal.myapplication.viewmodel.ContestViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ContestViewModel = viewModel()
) {
    val contests by viewModel.contests.collectAsState()
    val isFetching by viewModel.isFetching.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            val now = System.currentTimeMillis()
            contests.filter { it.startTimeMillis <= now }
                .forEach { viewModel.deleteContest(it) }
            delay(60_000L)
        }
    }

    LaunchedEffect(Unit) { viewModel.fetchApiContests() }

    val now = System.currentTimeMillis()
    val endOfToday = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }.timeInMillis

    val upcomingContests = contests
        .filter { it.startTimeMillis > now }
        .sortedBy { it.startTimeMillis }

    val todayContests = upcomingContests.filter { it.startTimeMillis <= endOfToday }
    val nearestContests = if (todayContests.size >= 3) todayContests.take(3)
    else todayContests + upcomingContests.filter { it.startTimeMillis > endOfToday }.take(3 - todayContests.size)

    val nextContest = nearestContests.firstOrNull()

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A1628), Color(0xFF020817))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(56.dp))

            // ── Top Bar ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Computer,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ContestTracker",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        )
                        Text(
                            text = "Stay ahead of the game",
                            color = Color(0xFF64748B),
                            fontSize = 11.sp
                        )
                    }
                }
                IconButton(
                    onClick = { navController.navigate("view all") },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF0F172A), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Fetch progress ──
            if (isFetching) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp)),
                    color = Color(0xFF3B82F6),
                    trackColor = Color(0xFF1E293B)
                )
                Spacer(Modifier.height(16.dp))
            }

            // ── Date label ──
            val todayLabel = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(Date())
            Row(
                modifier = Modifier
                    .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "📅  $todayLabel",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Section header ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Next Contest",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Coming Up",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (upcomingContests.size > 3) {
                    TextButton(
                        onClick = { navController.navigate("view all") },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF3B82F6)
                        )
                    ) {
                        Text("View All →", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Next contest card ──
            if (nextContest != null) {
                ContestCard(nextContest, remainingTime)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFF0F172A), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎯", fontSize = 28.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No upcoming contests",
                            color = Color(0xFF475569),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Upcoming section ──
            val upcomingTwo = nearestContests.drop(1)
            if (upcomingTwo.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Also Coming Up",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${upcomingTwo.size} contests",
                        color = Color(0xFF475569),
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                upcomingTwo.forEach { contest ->
                    var timeLeft by remember(contest.id) {
                        mutableStateOf(contest.startTimeMillis - System.currentTimeMillis())
                    }
                    LaunchedEffect(contest.id) {
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
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Explore button ──
            Button(
                onClick = { navController.navigate("view all") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0F172A)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(
                        listOf(Color(0xFF1E3A5F), Color(0xFF1E293B))
                    )
                )
            ) {
                Text(
                    "Explore All Contests →",
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}