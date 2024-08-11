package com.example.jora.model

import com.example.jora.extensionAPI.UserFirebase

class UserProfile (
    var authDetails: UserFirebase,
    var appSettings: AppSettings = AppSettings()
)