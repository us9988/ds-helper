package com.dshelper.app.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "token")

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    suspend fun saveToken(accessToken: String) {
        Log.d("LOGIN", "saveToken 호출됨: $accessToken")
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
        }
        Log.d("LOGIN", "saveToken 완료")
    }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { it[ACCESS_TOKEN] }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}