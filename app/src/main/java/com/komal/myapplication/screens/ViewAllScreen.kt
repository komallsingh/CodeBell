package com.komal.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import java.util.*

enum class DateFilter(val label: String) {
    ALL("All"),
    TODAY("Today"),
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(navController: NavController) {

    val viewModel: ContestViewModel = viewModel()
    val contests by viewModel.contests.collectAsState()

    var search by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf("All") }
    var selectedDateFilter by remember { mutableStateOf(DateFilter.ALL) }

    val platforms = remember(contests) {
        listOf("All") + contests.map { it.platform }.distinct().sorted()
    }

    val now = System.currentTimeMillis()

    val endOfToday = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59)
    }.timeInMillis

    val endOfWeek = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        add(Calendar.WEEK_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59)
    }.timeInMillis

    val endOfMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59)
    }.timeInMillis

    val filtered = contests
        .filter { it.startTimeMillis > now }
        .filter {
            search.isBlank() ||
                    it.name.contains(search, true) ||
                    it.platform.contains(search, true)
        }
        .filter { selectedPlatform == "All" || it.platform == selectedPlatform }
        .filter {
            when (selectedDateFilter) {
                DateFilter.ALL        -> true
                DateFilter.TODAY      -> it.startTimeMillis <= endOfToday
                DateFilter.THIS_WEEK  -> it.startTimeMillis <= endOfWeek
                DateFilter.THIS_MONTH -> it.startTimeMillis <= endOfMonth
            }
        }
        .sortedBy { it.startTimeMillis }

    var showAddSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = Color(0xFF2563EB),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0A1628), Color(0xFF020817))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // ── Header ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF0F172A), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "CONTESTS",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Upcoming",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }

                // ── Search ──
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    placeholder = { Text("Search contests...", fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color(0xFF1E293B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF3B82F6),
                        focusedLeadingIconColor = Color(0xFF3B82F6),
                        unfocusedLeadingIconColor = Color(0xFF475569),
                        focusedPlaceholderColor = Color(0xFF475569),
                        unfocusedPlaceholderColor = Color(0xFF334155),
                        focusedContainerColor = Color(0xFF0F172A),
                        unfocusedContainerColor = Color(0xFF0F172A)
                    )
                )

                Spacer(Modifier.height(16.dp))

                // ── Date Filters ──
                Text(
                    "FILTER BY DATE",
                    color = Color(0xFF475569),
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DateFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = selectedDateFilter == filter,
                            onClick = { selectedDateFilter = filter },
                            label = {
                                Text(
                                    filter.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedDateFilter == filter)
                                        FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2563EB),
                                selectedLabelColor = Color.White,
                                labelColor = Color(0xFF64748B),
                                containerColor = Color(0xFF0F172A)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedDateFilter == filter,
                                borderColor = Color(0xFF1E293B),
                                selectedBorderColor = Color.Transparent
                            )
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ── Platform Filters ──
                Text(
                    "FILTER BY PLATFORM",
                    color = Color(0xFF475569),
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    platforms.forEach { platform ->
                        val pColor = if (platform == "All") Color(0xFF3B82F6)
                        else Color(0xFF2563EB)
                        FilterChip(
                            selected = selectedPlatform == platform,
                            onClick = { selectedPlatform = platform },
                            label = {
                                Text(
                                    platform,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedPlatform == platform)
                                        FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = pColor.copy(alpha = 0.2f),
                                selectedLabelColor = pColor,
                                labelColor = Color(0xFF64748B),
                                containerColor = Color(0xFF0F172A)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedPlatform == platform,
                                borderColor = Color(0xFF1E293B),
                                selectedBorderColor = pColor.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ── Results count + clear ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${filtered.size} contests found",
                        color = Color(0xFF475569),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (selectedPlatform != "All" ||
                        selectedDateFilter != DateFilter.ALL ||
                        search.isNotBlank()
                    ) {
                        TextButton(
                            onClick = {
                                selectedPlatform = "All"
                                selectedDateFilter = DateFilter.ALL
                                search = ""
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text(
                                "Clear filters",
                                color = Color(0xFF3B82F6),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // ── Contest list ──
                if (filtered.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 32.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No contests match your filters",
                                color = Color(0xFF334155),
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filtered, key = { it.id }) { contest ->
                            ContestCard(contest, viewModel)
                        }
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        AddContestBottomSheet(
            onDismiss = { showAddSheet = false },
            onSave = {
                viewModel.insertContest(it)
                showAddSheet = false
            }
        )
    }
}