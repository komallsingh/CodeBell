package com.komal.myapplication.auth

import com.komal.myapplication.auth.LoginRequest
import com.komal.myapplication.auth.RegisterRequest
import com.komal.myapplication.database.RetrofitInstance

class AuthRepository {

    suspend fun register(
        username: String,
        email: String,
        password: String
    ) = RetrofitInstance.auth.register(
        RegisterRequest(
            username = username,
            email = email,
            password = password
        )
    )

    suspend fun login(
        email: String,
        password: String
    ) = RetrofitInstance.auth.login(
        LoginRequest(
            email = email,
            password = password
        )
    )
}