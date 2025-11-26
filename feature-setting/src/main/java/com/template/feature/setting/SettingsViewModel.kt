package com.template.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.data.datastore.SettingsManager
import com.template.data.datastore.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {
    val userSettings = settingsManager.getUserSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun updateSettings(newSettings: UserSettings) {
        viewModelScope.launch {
            settingsManager.saveUserSettings(newSettings)
        }
    }
}