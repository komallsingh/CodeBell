package com.komal.myapplication.database

// codeforces
data class CodeforcesContest(
    val id: Int = 0,
    val name: String = "",
    val phase: String = "",
    val startTimeSeconds: Long = 0L,
    val durationSeconds: Long = 0L
)

// codechef
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
//leetcode
data class LeetcodeContest(
    val title: String = "",
    val titleSlug: String = "",
    val startTime: Long = 0L,
    val duration: Long = 0L
)
// hackerrank
data class HackerrankContest(
    val name: String = "",
    val slug: String = "",
    val created_at: String = ""
)

// atcoder
data class AtcoderContest(
    val id: String = "",
    val title: String = "",
    val start_epoch_second: Long = 0L,
    val duration_second: Long = 0L
)