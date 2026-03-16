package com.komal.myapplication.database

// ── CODEFORCES ──
data class CodeforcesContest(
    val id: Int = 0,
    val name: String = "",
    val phase: String = "",
    val startTimeSeconds: Long = 0L,
    val durationSeconds: Long = 0L
)
data class CodeforcesResponse(
    val status: String = "",
    val result: List<CodeforcesContest> = emptyList()
)

// ── CODECHEF ──
data class CodechefResponse(
    val future_contests: List<CodechefContest> = emptyList(),
    val present_contests: List<CodechefContest> = emptyList()
)

data class CodechefContest(
    val contest_code: String = "",
    val contest_name: String = "",
    val contest_start_date: String = "",  // "2026-03-20 14:00:00"
    val contest_end_date: String = "",
    val contest_duration: String = ""
)

// ── LEETCODE ──
data class LeetcodeContest(
    val title: String = "",
    val titleSlug: String = "",
    val startTime: Long = 0L,
    val duration: Int = 0
)
data class LeetcodeResponse(
    val data: LeetcodeData? = null
)
data class LeetcodeData(
    val topTwoContests: List<LeetcodeContest> = emptyList()
)