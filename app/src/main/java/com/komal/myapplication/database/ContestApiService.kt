package com.komal.myapplication.database

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

// CODEFORCES
interface CodeforcesApiService {

    @GET("/codeforces/contests")
    suspend fun getContests(): List<CodeforcesContest>
}

// CODECHEF
interface CodechefApiService {

    @GET("/codechef/contests")
    suspend fun getContests(): CodechefResponse
}

// HACKERRANK
interface HackerrankApiService {

    @GET("/hackerrank/contests")
    suspend fun getContests(): List<HackerrankContest>
}

// ATCODER
interface AtcoderApiService {

    @GET("/atcoder/contests")
    suspend fun getContests(): List<AtcoderContest>
}

object RetrofitInstance {

    private const val BASE_URL =
        "https://cp-api-backend.onrender.com"

    private fun buildClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val codeforces: CodeforcesApiService by lazy {
        retrofit.create(CodeforcesApiService::class.java)
    }

    val codechef: CodechefApiService by lazy {
        retrofit.create(CodechefApiService::class.java)
    }

    val hackerrank: HackerrankApiService by lazy {
        retrofit.create(HackerrankApiService::class.java)
    }

    val atcoder: AtcoderApiService by lazy {
        retrofit.create(AtcoderApiService::class.java)
    }
}