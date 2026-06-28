package com.komal.myapplication.auth


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komal.myapplication.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun login(
        email: String,
        password: String,
        onSuccess: (String, String, String) -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                _loading.value = true

                val response =
                    repository.login(email, password)

                onSuccess(
                    response.token,
                    response.user.username,
                    response.user.email
                )

            } catch (e: Exception) {

                onError(
                    e.message ?: "Login failed"
                )

            } finally {

                _loading.value = false
            }
        }
    }
    fun register(
        username: String,
        email: String,
        password: String,
        onSuccess: (String, String, String) -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                _loading.value = true

                val response =
                    repository.register(
                        username,
                        email,
                        password
                    )

                onSuccess(
                    response.token,
                    response.user.username,
                    response.user.email
                )

            } catch (e: Exception) {
                Log.e(
                    "AUTH_ERROR",
                    "Register failed",
                    e
                )
                onError(
                    e.message ?: "Registration failed"
                )

            } finally {

                _loading.value = false
            }
        }
    }
}