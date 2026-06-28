package com.komal.myapplication.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth")

class TokenManager(
    private val context: Context
) {

    companion object {

        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USERNAME_KEY = stringPreferencesKey("username")
        val EMAIL_KEY = stringPreferencesKey("email")
    }

    suspend fun saveAuthData(
        token: String,
        username: String,
        email: String
    ) {
        context.dataStore.edit {

            it[TOKEN_KEY] = token
            it[USERNAME_KEY] = username
            it[EMAIL_KEY] = email
        }
    }

    val tokenFlow =
        context.dataStore.data.map {
            it[TOKEN_KEY]
        }

    suspend fun clearToken() {
        context.dataStore.edit {
            it.clear()
        }
    }
}