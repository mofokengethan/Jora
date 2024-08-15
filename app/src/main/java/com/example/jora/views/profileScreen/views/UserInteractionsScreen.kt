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
import com.example.jora.composable.MainTextFieldVM
import com.example.jora.views.profileScreen.ProfileScreen


@Composable
fun UserInteractionsScreen(
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
fun Preview() {
    UserInteractionsScreen(navController = rememberNavController(), mainViewModel = MainViewModel(), UserProfileScreenVM())

}