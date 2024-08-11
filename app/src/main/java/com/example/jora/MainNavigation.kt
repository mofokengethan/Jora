package com.example.jora

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jora.onboarding.CreateAccountScreen
import com.example.jora.onboarding.LoginScreen
import com.example.jora.composable.MainTextFieldVM

@Composable
fun MainNavigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()

    val emailVM = MainTextFieldVM()
    val passwordVM = MainTextFieldVM()
    val matchPasswordVM = MainTextFieldVM()

    NavHost(
        navController = navController,
        startDestination = "loginScreen"
    ) {
        composable("loginScreen") {
            LoginScreen(navController, mainViewModel, emailVM, passwordVM)
        }
        composable("createAccount") {
            CreateAccountScreen(navController, mainViewModel, emailVM, passwordVM, matchPasswordVM)
        }
        composable("mainScreen") {
            MainScreen(navController, mainViewModel)
        }
    }
}