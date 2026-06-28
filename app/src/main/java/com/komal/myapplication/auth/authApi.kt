package com.komal.myapplication.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @GET("/auth/me")
    suspend fun getProfile(): ProfileResponse
}