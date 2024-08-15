package com.example.jora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.example.jora.composable.MainTextFieldVM
import com.example.jora.json.leagueData.QuizApp
import com.example.jora.model.UserProfile
import com.example.jora.onboarding.CreateAccountScreen
import com.example.jora.ui.theme.JoraTheme
import com.example.jora.views.profileScreen.AppSettingType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppSettings: ViewModel() {
    private var _language: MutableStateFlow<String> = MutableStateFlow("English")
    var language: StateFlow<String> = _language

    private var _region: MutableStateFlow<String> = MutableStateFlow("North America")
    var region: StateFlow<String> = _region

    private var _passcode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var passcode: StateFlow<Boolean> = _passcode

    private var _darkMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var darkMode: StateFlow<Boolean> = _darkMode

    private var _datingMode: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var datingMode: StateFlow<Boolean> = _datingMode

    private var _friendRequests: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var friendRequests: StateFlow<Boolean> = _friendRequests

    private var _rememberMe: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var rememberMe: StateFlow<Boolean> = _rememberMe

    private var _updatePassword: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var updatePassword: StateFlow<Boolean> = _updatePassword

    private var _naturalLanguage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var naturalLanguage: StateFlow<Boolean> = _naturalLanguage

    fun updateLanguage(newLanguage: String) {
        _language.value = newLanguage
    }

    fun updateRegion(newRegion: String) {
        _region.value = newRegion
    }

    fun updateAppSettings(settingsType: AppSettingType, value: Boolean) {
        when (settingsType) {
            AppSettingType.EnabledPasscode -> _passcode.value = value
            AppSettingType.EnabledDarkMode -> _darkMode.value = value
            AppSettingType.EnabledDatingMode -> _datingMode.value = value
            AppSettingType.TurnOffFriendRequests -> _friendRequests.value = value
            AppSettingType.RememberMe -> _rememberMe.value = value
            AppSettingType.UpdatePassword -> _updatePassword.value = value
            AppSettingType.NaturalLanguage -> _naturalLanguage.value = value

            AppSettingType.Region, AppSettingType.Language -> {}
        }
    }
}

class MainViewModel: ViewModel() {

    private var _user: MutableStateFlow<UserProfile?> = MutableStateFlow(null)
    var user: StateFlow<UserProfile?> = _user

    private var _appSettings: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())
    var appSettings: StateFlow<AppSettings> = _appSettings



    fun setLoginUser(userProfile: UserProfile) {
        _user.value = userProfile
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoraTheme {
                // MainNavigation(MainViewModel())
                //  QuizApp(applicationContext)
                CreateAccountScreen(
                    navController = rememberNavController(),
                    mainViewModel = MainViewModel(),
                    emailVM = MainTextFieldVM(),
                    passwordVM = MainTextFieldVM(),
                    matchPasswordVM = MainTextFieldVM()
                )
            }
        }
    }
}