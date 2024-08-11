package com.example.jora.model

class AppSettings(
    var enabledDarkMode: Boolean = false,
    var turnOffFriendRequests: Boolean = true,
    var enabledDatingMode: Boolean = true,
    var enabledPasscode: Boolean = false,
    var region: String = "U.S.",
    var language: String = "English",
)