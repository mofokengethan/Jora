package com.example.jora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import com.example.jora.model.AppSettings
import com.example.jora.model.UserProfile
import com.example.jora.ui.theme.JoraTheme
import com.example.jora.views.profileScreen.AppSettingType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {

    private var _user: MutableStateFlow<UserProfile?> = MutableStateFlow(null)
    var user: StateFlow<UserProfile?> = _user

    private var _appSettings: MutableStateFlow<AppSettings?> = MutableStateFlow(null)
    var appSettings: StateFlow<AppSettings?> = _appSettings


    fun setLoginUser(userProfile: UserProfile) {
        _user.value = userProfile
    }

    fun setAppSettings(appSettings: AppSettings) {
        _appSettings.value = appSettings
    }

    fun updateAppSettings(
        settingsType: AppSettingType,
        enable: Boolean
    ) {
        when (settingsType) {
            AppSettingType.EnabledPasscode -> _appSettings.value?.enabledPasscode = enable
            AppSettingType.EnabledDarkMode -> _appSettings.value?.enabledDarkMode = enable
            AppSettingType.EnabledDatingMode -> _appSettings.value?.enabledDatingMode = enable
            AppSettingType.TurnOffFriendRequests -> _appSettings.value?.turnOffFriendRequests = enable
            else -> {}
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoraTheme {
                MainNavigation(MainViewModel())
            }
        }
    }
}