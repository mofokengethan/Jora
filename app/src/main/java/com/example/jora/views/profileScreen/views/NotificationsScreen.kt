package com.example.jora.views.profileScreen.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jora.BackgroundScreen
import com.example.jora.MainViewModel
import com.example.jora.UserProfileScreenVM

@Composable
fun NotificationsScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    contentVM: UserProfileScreenVM,

    ) {
    BackgroundScreen(padding = PaddingValues()) {
        Text("User Interactions")
    }
}

@Preview
@Composable
fun PreviewNotifications() {
    NotificationsScreen(navController = rememberNavController(), mainViewModel = MainViewModel(), UserProfileScreenVM())
}