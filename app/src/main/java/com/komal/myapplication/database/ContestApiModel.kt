package com.komal.myapplication.database

// ── CODEFORCES ──
data class CodeforcesContest(
    val id: Int = 0,
    val name: String = "",
    val phase: String = "",
    val startTimeSeconds: Long = 0L,
    val durationSeconds: Long = 0L
)

// ── CODECHEF ──
data class CodechefResponse(
    val future_contests: List<CodechefContest> = emptyList(),
    val present_contests: List<CodechefContest> = emptyList()
)

data class CodechefContest(
    val contest_code: String = "",
    val contest_name: String = "",
    val contest_start_date: String = "",
    val contest_end_date: String = "",
    val contest_duration: String = ""
)

// ── HACKERRANK ──
data class HackerrankContest(
    val name: String = "",
    val slug: String = "",
    val created_at: String = ""
)

// ── ATCODER ──
data class AtcoderContest(
    val id: String = "",
    val title: String = "",
    val start_epoch_second: Long = 0L,
    val duration_second: Long = 0L
)