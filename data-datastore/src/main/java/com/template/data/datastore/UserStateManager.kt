package com.template.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userStateDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_state")

data class UserState(
    val lastLoginTime: Long = 0L,
    val lastLocationLat: Double? = null,
    val lastLocationLng: Double? = null
)

interface UserStateManager {
    fun getUserState(): Flow<UserState>
    suspend fun saveLastLoginTime(time: Long)
    suspend fun saveLastLocation(lat: Double, lng: Double)
    suspend fun clearUserState()
}

@Singleton
class UserStateManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserStateManager {

    companion object {
        private val LAST_LOGIN_KEY = longPreferencesKey("last_login_time")
        private val LAST_LAT_KEY = doublePreferencesKey("last_location_lat")
        private val LAST_LNG_KEY = doublePreferencesKey("last_location_lng")
    }

    override fun getUserState(): Flow<UserState> {
        return context.userStateDataStore.data.map { preferences ->
            UserState(
                lastLoginTime = preferences[LAST_LOGIN_KEY] ?: 0L,
                lastLocationLat = preferences[LAST_LAT_KEY],
                lastLocationLng = preferences[LAST_LNG_KEY]
            )
        }
    }

    override suspend fun saveLastLoginTime(time: Long) {
        context.userStateDataStore.edit { it[LAST_LOGIN_KEY] = time }
    }

    override suspend fun saveLastLocation(lat: Double, lng: Double) {
        context.userStateDataStore.edit {
            it[LAST_LAT_KEY] = lat
            it[LAST_LNG_KEY] = lng
        }
    }

    override suspend fun clearUserState() {
        context.userStateDataStore.edit {
            it.clear()
        }
    }
}
