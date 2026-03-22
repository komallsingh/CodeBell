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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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

    val endOfMonth = Calendar.getInstance().let { cal ->
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.timeInMillis
    }

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
    val hasActiveFilters = selectedPlatform != "All" ||
            selectedDateFilter != DateFilter.ALL ||
            search.isNotBlank()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = AppTheme.AccentBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.Add, null)
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.GradientBg)
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // ── Header ──────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AppTheme.BgCard, CircleShape)
                            .drawBehind {
                                drawCircle(
                                    color = AppTheme.BorderSubtle,
                                    radius = size.minDimension / 2f,
                                    style = Stroke(1f)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                null,
                                tint = AppTheme.TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "ALL CONTESTS",
                            color = AppTheme.AccentBlue.copy(alpha = 0.75f),
                            fontSize = 10.sp,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Upcoming",
                            color = AppTheme.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(AppTheme.BgCard, RoundedCornerShape(8.dp))
                            .drawBehind {
                                drawRoundRect(
                                    color = AppTheme.BorderSubtle,
                                    cornerRadius = CornerRadius(8.dp.toPx()),
                                    style = Stroke(1f)
                                )
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            "${filtered.size}",
                            color = AppTheme.AccentBlue,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // ── Search ───────────────────────────────────────────────────
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    leadingIcon = {
                        Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (search.isNotBlank()) {
                            IconButton(onClick = { search = "" }) {
                                Icon(
                                    Icons.Default.Clear, null,
                                    modifier = Modifier.size(16.dp),
                                    tint = AppTheme.TextMuted
                                )
                            }
                        }
                    },
                    placeholder = { Text("Search contests or platforms…", fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppTheme.AccentBlue,
                        unfocusedBorderColor = AppTheme.BorderSubtle,
                        focusedTextColor = AppTheme.TextPrimary,
                        unfocusedTextColor = AppTheme.TextPrimary,
                        cursorColor = AppTheme.AccentBlue,
                        focusedLeadingIconColor = AppTheme.AccentBlue,
                        unfocusedLeadingIconColor = AppTheme.TextMuted,
                        focusedPlaceholderColor = AppTheme.TextMuted,
                        unfocusedPlaceholderColor = AppTheme.TextMuted,
                        focusedContainerColor = AppTheme.BgCard,
                        unfocusedContainerColor = AppTheme.BgCard
                    )
                )

                Spacer(Modifier.height(16.dp))

                // ── Date Filters ─────────────────────────────────────────────
                Text(
                    "DATE",
                    color = AppTheme.TextMuted,
                    fontSize = 10.sp,
                    letterSpacing = 1.8.sp,
                    fontWeight = FontWeight.Bold,
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
                        val isSelected = selectedDateFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedDateFilter = filter },
                            label = {
                                Text(
                                    filter.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AppTheme.AccentBlue,
                                selectedLabelColor = Color.White,
                                labelColor = AppTheme.TextSecondary,
                                containerColor = AppTheme.BgCard
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = AppTheme.BorderSubtle,
                                selectedBorderColor = Color.Transparent
                            )
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ── Platform Filters ─────────────────────────────────────────
                Text(
                    "PLATFORM",
                    color = AppTheme.TextMuted,
                    fontSize = 10.sp,
                    letterSpacing = 1.8.sp,
                    fontWeight = FontWeight.Bold,
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
                        val isSelected = selectedPlatform == platform
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPlatform = platform },
                            label = {
                                Text(
                                    platform,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AppTheme.AccentBlue.copy(alpha = 0.18f),
                                selectedLabelColor = AppTheme.AccentBlue,
                                labelColor = AppTheme.TextSecondary,
                                containerColor = AppTheme.BgCard
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = AppTheme.BorderSubtle,
                                selectedBorderColor = AppTheme.AccentBlue.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ── Results bar ──────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${filtered.size} contest${if (filtered.size != 1) "s" else ""} found",
                        color = AppTheme.TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (hasActiveFilters) {
                        TextButton(
                            onClick = {
                                selectedPlatform = "All"
                                selectedDateFilter = DateFilter.ALL
                                search = ""
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.FilterAltOff, null,
                                modifier = Modifier.size(14.dp),
                                tint = AppTheme.AccentBlue
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Clear", color = AppTheme.AccentBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .background(AppTheme.BorderSubtle)
                )
                Spacer(Modifier.height(4.dp))

                // ── Contest list ─────────────────────────────────────────────
                if (filtered.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 36.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No contests match your filters",
                                color = AppTheme.TextMuted,
                                fontSize = 14.sp
                            )
                            if (hasActiveFilters) {
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    "Try clearing some filters",
                                    color = AppTheme.TextMuted.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filtered, key = { it.id }) { contest ->
                            ContestListCard(contest, viewModel)
                        }
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        val context = androidx.compose.ui.platform.LocalContext.current
        AddContestBottomSheet(
            onDismiss = { showAddSheet = false },
            onSave = { entity ->
                viewModel.insertContest(entity) { realId ->
                    if (entity.reminderEnabled && entity.reminderTimeMillis > System.currentTimeMillis()) {
                        val offsetMs = entity.startTimeMillis - entity.reminderTimeMillis
                        val label = when {
                            offsetMs >= 23 * 60 * 60 * 1000L -> "Start of Day"
                            offsetMs >= 55 * 60 * 1000L      -> "1 Hour Before"
                            else                              -> "30 Mins Before"
                        }
                        com.komal.myapplication.reminder.ReminderScheduler.schedule(
                            context      = context,
                            contestId    = realId.toInt(),
                            contestName  = entity.name,
                            platform     = entity.platform,
                            reminderTime = entity.reminderTimeMillis,
                            offsetLabel  = label
                        )
                    }
                }
                showAddSheet = false
            }
        )
    }
}