package com.komal.myapplication.database

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// ── CODEFORCES ──
interface CodeforcesApiService {
    @GET("contest.list")
    suspend fun getContests(): CodeforcesResponse
}

// ── CODECHEF ──
interface CodechefApiService {
    @GET("api/list/contests/all")  // ← correct endpoint
    suspend fun getContests(): CodechefResponse
}

// ── LEETCODE ──
interface LeetcodeApiService {
    @POST("graphql")
    suspend fun getContests(
        @Body body: okhttp3.RequestBody
    ): LeetcodeResponse
}

object RetrofitInstance {

    private fun buildClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val codeforces: CodeforcesApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://codeforces.com/api/")
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CodeforcesApiService::class.java)
    }

    val codechef: CodechefApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.codechef.com/")  // ← remove "api/"
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CodechefApiService::class.java)
    }

    val leetcode: LeetcodeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://leetcode.com/")
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LeetcodeApiService::class.java)
    }
}