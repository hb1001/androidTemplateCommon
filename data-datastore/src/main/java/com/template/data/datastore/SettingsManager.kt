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

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

data class UserSettings(
    val autoLogin: Boolean = false,
    val mapJumpTo: MapJumpOption = MapJumpOption.NO_JUMP,
    val mapDefaultSize: Int = 10
)

enum class MapJumpOption(
    val value: String,
    val label: String      // 中文名
) {
    NO_JUMP("no_jump", "不跳转"),
    USER_LOCATION("user_location", "用户当前位置"),
    LAST_POSITION("last_position", "上次退出的位置");

    companion object {
        fun fromValue(value: String?): MapJumpOption {
            return MapJumpOption.entries.find { it.value == value } ?: NO_JUMP
        }
        fun fromLabel(label: String?): MapJumpOption {
            return MapJumpOption.entries.find { it.label == label } ?: NO_JUMP
        }
    }
}


interface SettingsManager {
    fun getUserSettings(): Flow<UserSettings>
    suspend fun saveUserSettings(settings: UserSettings)
    suspend fun clearUserSettings()
}

@Singleton
class SettingsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsManager {

    companion object {
        private val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")
        private val MAP_JUMP_KEY = stringPreferencesKey("map_jump_to")
        private val MAP_SIZE_KEY = intPreferencesKey("map_default_size")
    }

    override fun getUserSettings(): Flow<UserSettings> {
        return context.settingsDataStore.data.map { preferences ->
            UserSettings(
                autoLogin = preferences[AUTO_LOGIN_KEY] ?: false,
                mapJumpTo = MapJumpOption.fromValue(preferences[MAP_JUMP_KEY]),
                mapDefaultSize = preferences[MAP_SIZE_KEY] ?: 10
            )
        }
    }

    override suspend fun saveUserSettings(settings: UserSettings) {
        context.settingsDataStore.edit { preferences ->
            preferences[AUTO_LOGIN_KEY] = settings.autoLogin
            preferences[MAP_JUMP_KEY] = settings.mapJumpTo.value
            preferences[MAP_SIZE_KEY] = settings.mapDefaultSize.coerceIn(5, 20)
        }
    }

    override suspend fun clearUserSettings() {
        context.settingsDataStore.edit { preferences ->
            preferences.remove(AUTO_LOGIN_KEY)
            preferences.remove(MAP_JUMP_KEY)
            preferences.remove(MAP_SIZE_KEY)
        }
    }
}
