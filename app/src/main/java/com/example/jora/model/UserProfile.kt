package com.example.jora.model

import com.example.jora.extensionAPI.UserFirebase
import com.example.jora.json.leagueData.QuizData

class UserProfile (
    var authDetails: UserFirebase,
    var quizData: QuizData = QuizData(listOf(QuizData.Category("", listOf(QuizData.Question("", listOf("")))))),
    var userDetails: UserDetails = UserDetails()
)

class UserDetails (
    var passcode: String? = ""
)