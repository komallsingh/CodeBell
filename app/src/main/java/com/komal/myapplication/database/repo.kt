package com.komal.myapplication.database

import kotlinx.coroutines.flow.Flow

class Repo(private val dao: dao) {

    val allContest: Flow<List<ContestEntity>> =
        dao.getAllContests()

    val contestSortedByStartTime: Flow<List<ContestEntity>> =
        dao.getAllContestsSortedByStartTime()

    val bookmarkedContests: Flow<List<ContestEntity>> =
        dao.getBookmarkedContests()

    suspend fun insert(contest: ContestEntity): Long =
        dao.insertContest(contest)

    suspend fun delete(contest: ContestEntity) =
        dao.deleteContest(contest)

    suspend fun update(contest: ContestEntity) =
        dao.updateContest(contest)

    suspend fun clearAll() =
        dao.clearAll()

    // MAIN FETCH FUNCTION
    suspend fun fetchAndStoreApiContests() {
        dao.clearApiContests()
        fetchCodeforces()
        fetchCodechef()
        fetchHackerrank()
        fetchLeetCode()
        //fetchAtcoder()
    }
    // CODEFORCES
    private suspend fun fetchCodeforces() {
        try {
            val contests = RetrofitInstance.codeforces.getContests()
            val now = System.currentTimeMillis()

            contests
                .filter { it.phase == "BEFORE" }
                .forEach { contest ->

                    val startMillis = contest.startTimeSeconds * 1000L

                    if (startMillis > now) {

                        dao.insertContest(
                            ContestEntity(
                                name = contest.name,
                                platform = "Codeforces",
                                startTimeMillis = startMillis,
                                durationSeconds = contest.durationSeconds,
                                contestUrl = "https://codeforces.com/contest/${contest.id}",
                                isManual = false
                            )
                        )
                    }
                }

        } catch (e: Exception) {
            android.util.Log.e("FETCH", "Codeforces failed", e)
        }
    }
    //LEETCODE
    private suspend fun fetchLeetCode() {
        try {

            val contest = RetrofitInstance.leetcode.getContests()
            val now = System.currentTimeMillis()

            contest.forEach { contest ->

                val startMillis = contest.startTime * 1000L

                if (startMillis > now) {

                    dao.insertContest(
                        ContestEntity(
                            name = contest.title,
                            platform = "LeetCode",
                            startTimeMillis = startMillis,
                            durationSeconds = contest.duration,
                            contestUrl = "https://leetcode.com/contest/${contest.titleSlug}",
                            isManual = false
                        )
                    )
                }
            }

        } catch (e: Exception) {
            android.util.Log.e("FETCH", "LeetCode failed", e)
        }
    }

    // CODECHEF
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
                            name = contest.contest_name,
                            platform = "CodeChef",
                            startTimeMillis = startMillis,
                            durationSeconds =
                                contest.contest_duration.toLongOrNull() ?: 0L,
                            contestUrl =
                                "https://www.codechef.com/${contest.contest_code}",
                            reminderTimeMillis = 0L,
                            reminderEnabled = false,
                            isManual = false
                        )
                    )
                }else {
                    android.util.Log.d("SKIP", "CodeChef skipped: $startMillis")
                }
            }

            android.util.Log.d("FETCH_DEBUG", "CodeChef done!")

        } catch (e: Exception) {
            android.util.Log.e("FETCH_DEBUG", "CodeChef failed: ${e.message}")
        }
    }
    // HACKERRANK
    private suspend fun fetchHackerrank() {
        try {
            android.util.Log.d("FETCH_DEBUG", "Fetching HackerRank...")
            val response =
                RetrofitInstance.hackerrank.getContests()
            val now = System.currentTimeMillis()
            response.forEach { contest ->
                val startMillis =
                    parseHackerrankTime(contest.created_at)
                if (startMillis > now) {
                    dao.insertContest(
                        ContestEntity(
                            name = contest.name,
                            platform = "HackerRank",
                            startTimeMillis = startMillis,
                            durationSeconds = 0L,
                            contestUrl =
                                "https://www.hackerrank.com/${contest.slug}",
                            reminderTimeMillis = 0L,
                            reminderEnabled = false,
                            isManual = false
                        )
                    )
                }else {
                    android.util.Log.d("SKIP", "hackerrank skipped: $startMillis")
                }
            }

            android.util.Log.d("FETCH_DEBUG", "HackerRank done!")

        } catch (e: Exception) {
            android.util.Log.e("FETCH_DEBUG", "HackerRank failed: ${e.message}")
        }
    }
    // ATCODER
//    private suspend fun fetchAtcoder() {
//        try {
//            android.util.Log.d("FETCH_DEBUG", "Fetching AtCoder...")
//
//            val response = RetrofitInstance.atcoder.getContests()
//            val now = System.currentTimeMillis()
//
//            response.forEach { contest ->
//
//                val startMillis = contest.start_epoch_second * 1000L
//
//                android.util.Log.d(
//                    "PARSE_DEBUG",
//                    "AtCoder: ${contest.title} → $startMillis"
//                )
//
//                // safety check (ONLY needed fix)
//                if (startMillis <= 0L) {
//                    android.util.Log.d("SKIP", "AtCoder invalid time: ${contest.title}")
//                    return@forEach
//                }
//
//                if (startMillis > now) {
//
//                    dao.insertContest(
//                        ContestEntity(
//                            name = contest.title,
//                            platform = "AtCoder",
//                            startTimeMillis = startMillis,
//                            durationSeconds = contest.duration_second,
//                            contestUrl = "https://atcoder.jp/contests/${contest.id}",
//                            reminderTimeMillis = 0L,
//                            reminderEnabled = false,
//                            isManual = false
//                        )
//                    )
//                } else {
//                    android.util.Log.d("SKIP", "AtCoder skipped: $startMillis")
//                }
//            }
//
//            android.util.Log.d("FETCH_DEBUG", "AtCoder done!")
//
//        } catch (e: Exception) {
//            android.util.Log.e("FETCH_DEBUG", "AtCoder failed: ${e.message}")
//        }
//    }

    // TIME PARSERS
    private fun parseCodechefTime(timeStr: String): Long {
        return try {
            val cleaned = timeStr.trim().replace("\\s+".toRegex(), " ")

            val sdf = java.text.SimpleDateFormat(
                "dd MMM yyyy HH:mm:ss",
                java.util.Locale.ENGLISH
            ).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }

            sdf.parse(cleaned)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
    private fun parseHackerrankTime(time: String): Long {
        return try {
            val sdf = java.text.SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                java.util.Locale.getDefault()
            ).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }

            val result = sdf.parse(time)?.time ?: 0L
            android.util.Log.d("PARSE_DEBUG", "HackerRank: $time → $result")
            result

        } catch (e: Exception) {
            android.util.Log.e("PARSE_DEBUG", "HackerRank parse failed: $time", e)
            0L
        }
    }

    // BOOKMARK
    suspend fun toggleBookmark(contest: ContestEntity) {
        dao.updateContest(
            contest.copy(isBookmarked = !contest.isBookmarked)
        )
    }
}