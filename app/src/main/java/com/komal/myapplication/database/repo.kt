package com.komal.myapplication.database

import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class Repo(private val dao: dao) {

    val allContest: Flow<List<ContestEntity>>                = dao.getAllContests()
    val contestSortedByStartTime: Flow<List<ContestEntity>> = dao.getAllContestsSortedByStartTime()
    val bookmarkedContests: Flow<List<ContestEntity>>        = dao.getBookmarkedContests()

    // ← returns the real Room-assigned ID
    suspend fun insert(contest: ContestEntity): Long = dao.insertContest(contest)

    suspend fun delete(contest: ContestEntity)  { dao.deleteContest(contest) }
    suspend fun update(contest: ContestEntity)  { dao.updateContest(contest) }
    suspend fun clearAll()                       { dao.clearAll() }

    suspend fun fetchAndStoreApiContests() {
        dao.clearApiContests()
        fetchCodeforces()
        fetchCodechef()
        fetchLeetcode()
    }

    // ── CODEFORCES ──────────────────────────────────────────────────────────
    private suspend fun fetchCodeforces() {
        try {
            android.util.Log.d("FETCH_DEBUG", "Fetching Codeforces...")
            val response = RetrofitInstance.codeforces.getContests()
            if (response.status != "OK") return
            val now = System.currentTimeMillis()
            response.result
                .filter { it.phase == "BEFORE" }
                .forEach { contest ->
                    val startMillis = contest.startTimeSeconds * 1000L
                    if (startMillis > now) {
                        dao.insertContest(
                            ContestEntity(
                                name            = contest.name,
                                platform        = "Codeforces",
                                startTimeMillis = startMillis,
                                durationSeconds = contest.durationSeconds,
                                contestUrl      = "https://codeforces.com/contest/${contest.id}",
                                reminderTimeMillis = 0L,
                                reminderEnabled = false,
                                isManual        = false
                            )
                        )
                    }
                }
            android.util.Log.d("FETCH_DEBUG", "Codeforces done!")
        } catch (e: Exception) {
            android.util.Log.e("FETCH_DEBUG", "Codeforces failed: ${e.message}")
        }
    }

    // ── CODECHEF ─────────────────────────────────────────────────────────────
    private suspend fun fetchCodechef() {
        try {
            android.util.Log.d("FETCH_DEBUG", "Fetching CodeChef...")
            val response = RetrofitInstance.codechef.getContests()
            val now = System.currentTimeMillis()
            response.future_contests.forEach { contest ->
                val startMillis = parseCodechefTime(contest.contest_start_date)
                if (startMillis > now) {
                    dao.insertContest(
                        ContestEntity(
                            name            = contest.contest_name,
                            platform        = "CodeChef",
                            startTimeMillis = startMillis,
                            durationSeconds = contest.contest_duration.toLongOrNull() ?: 0L,
                            contestUrl      = "https://www.codechef.com/${contest.contest_code}",
                            reminderTimeMillis = 0L,
                            reminderEnabled = false,
                            isManual        = false
                        )
                    )
                }
            }
            android.util.Log.d("FETCH_DEBUG", "CodeChef done!")
        } catch (e: Exception) {
            android.util.Log.e("FETCH_DEBUG", "CodeChef failed: ${e.message}")
        }
    }

    // ── LEETCODE ──────────────────────────────────────────────────────────────
    private suspend fun fetchLeetcode() {
        try {
            android.util.Log.d("FETCH_DEBUG", "Fetching LeetCode...")
            val query = """
                {"query":"{ topTwoContests { title titleSlug startTime duration } }"}
            """.trimIndent()
            val body = query.toRequestBody("application/json".toMediaType())
            val response = RetrofitInstance.leetcode.getContests(body)
            val now = System.currentTimeMillis()
            response.data?.topTwoContests?.forEach { contest ->
                val startMillis = contest.startTime * 1000L
                if (startMillis > now) {
                    dao.insertContest(
                        ContestEntity(
                            name            = contest.title,
                            platform        = "LeetCode",
                            startTimeMillis = startMillis,
                            durationSeconds = contest.duration.toLong(),
                            contestUrl      = "https://leetcode.com/contest/${contest.titleSlug}",
                            reminderTimeMillis = 0L,
                            reminderEnabled = false,
                            isManual        = false
                        )
                    )
                }
            }
            android.util.Log.d("FETCH_DEBUG", "LeetCode done!")
        } catch (e: Exception) {
            android.util.Log.e("FETCH_DEBUG", "LeetCode failed: ${e.message}")
        }
    }

    // ── TIME PARSERS ──────────────────────────────────────────────────────────
    private fun parseCodechefTime(timeStr: String): Long {
        return try {
            val sdf = java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault()
            ).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }
            sdf.parse(timeStr)?.time ?: 0L
        } catch (e: Exception) { 0L }
    }

    suspend fun toggleBookmark(contest: ContestEntity) {
        dao.updateContest(contest.copy(isBookmarked = !contest.isBookmarked))
    }
}