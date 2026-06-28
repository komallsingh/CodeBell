package com.komal.myapplication.auth

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class User(
    val id: String,
    val username: String,
    val email: String
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class ProfileResponse(
    val _id: String,
    val username: String,
    val email: String
)