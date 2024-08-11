package com.example.jora

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jora.composable.DualRowContent
import com.example.jora.composable.MainTextFieldVM
import com.example.jora.model.UserProfile
import com.example.jora.ui.theme.background
import com.example.jora.ui.theme.buttonBackground
import com.example.jora.ui.theme.buttonTint
import com.example.jora.ui.theme.buttonTint2
import com.example.jora.ui.theme.dmDisplay
import com.example.jora.ui.theme.regularBlack
import com.example.jora.views.commentsScreen.CommentsScreen
import com.example.jora.views.friendsScreen.FriendsScreen
import com.example.jora.views.impressionsScreen.ImpressionsScreen
import com.example.jora.views.matchesScreen.MatchesScreen
import com.example.jora.views.postsScreen.PostsScreen
import com.example.jora.views.profileScreen.ProfileScreen
import com.example.jora.views.shareScreen.SharesScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    UserProfileScreen(navController, UserProfileScreenVM(), mainViewModel)
}

@Composable
fun BackgroundScreen(padding: PaddingValues, content: @Composable ()-> Unit) {
    Column(modifier = Modifier
        .fillMaxSize(1f)
        .background(background)
        .padding(padding)
    ) {
        content()
    }
}


class HeaderSelectionContentVM: ViewModel() {
    private var profileScreenHeaders = listOf("Profile", "Friends", "Matches", "Posts", "Shares", "Comments", "Impressions")

    private var _profileHeaders: MutableStateFlow<List<String>> = MutableStateFlow(profileScreenHeaders)
    var profileHeaders: StateFlow<List<String>> = _profileHeaders

    private var _selectedSection: MutableStateFlow<String> = MutableStateFlow("")
    var selectedSection: StateFlow<String> = _selectedSection

    fun setSelectedSection(sectionTitle: String) {
        _selectedSection.value = sectionTitle
        val index = _profileHeaders.value.indexOf(sectionTitle)
        val updatedList = ArrayList(_profileHeaders.value)
        updatedList.removeAt(index)
        updatedList.add(0, sectionTitle)
        _profileHeaders.value = updatedList
    }
}

@Composable
fun HeaderSelectionContent(
    contentVM: HeaderSelectionContentVM,
    action: (String) -> Unit
) {
    val headers = contentVM.profileHeaders.collectAsState().value
    val selectSection = contentVM.selectedSection.collectAsState().value

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(headers) { sectionTitle ->
            val isSelected = sectionTitle == selectSection
            Text(
                text = sectionTitle,
                fontFamily = dmDisplay,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Black,
                color = if (isSelected) buttonTint else buttonTint2,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .border(1.38.dp, if (isSelected) Color.DarkGray else Color.LightGray, RoundedCornerShape(6.dp))
                    .background(if (isSelected) buttonBackground else Color.White, RoundedCornerShape(6.dp))
                    .padding(6.dp)
                    .clickable {
                        action(sectionTitle)
                    }
            )
        }
    }
}

enum class UserProfileScreenType {
    Profile, Friends, Matches, Posts, Comments, Shares, Impressions
}

class UserProfileScreenVM: ViewModel() {
    private var _userProfileScreenView: MutableStateFlow<UserProfileScreenType> = MutableStateFlow(
        UserProfileScreenType.Profile
    )
    var userProfileScreenView: StateFlow<UserProfileScreenType> = _userProfileScreenView

    private fun changeView(screenType: UserProfileScreenType) {
        _userProfileScreenView.value = screenType
    }

    fun setView(sectionTitle: String) {
        when (sectionTitle) {
            "Profile" -> { changeView(UserProfileScreenType.Profile) }
            "Friends" -> { changeView(UserProfileScreenType.Friends) }
            "Matches" -> { changeView(UserProfileScreenType.Matches) }
            "Posts" -> { changeView(UserProfileScreenType.Posts) }
            "Comments" -> { changeView(UserProfileScreenType.Comments) }
            "Shares" -> { changeView(UserProfileScreenType.Shares) }
            "Impressions" -> { changeView(UserProfileScreenType.Impressions) }
        }
    }
}

val iconModifier = Modifier
    .background(background, shape = RoundedCornerShape(6.dp))
    .border(1.dp, regularBlack, shape = RoundedCornerShape(6.dp))
    .padding(8.dp)

@Composable
fun UserProfileScreen(
    navController: NavController,
    contentVM: UserProfileScreenVM,
    mainViewModel: MainViewModel
) {
    val user = mainViewModel.user.collectAsState().value
    val screenType = contentVM.userProfileScreenView.collectAsState().value
    val headerVM by remember { mutableStateOf(HeaderSelectionContentVM()) }

    val emailVM = MainTextFieldVM()
    val passwordVM = MainTextFieldVM()
    val displayNameVM = MainTextFieldVM()
    val passcodeVM = MainTextFieldVM()

    Scaffold { innerPadding ->
        BackgroundScreen(innerPadding) {
            DualRowContent(leftSide = {
                Text(
                    text = user?.authDetails?.creationTimestamp ?: "August 8, 2024",
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Text(
                    text = user?.authDetails?.email ?: "MysticTraveler_2024_Explorer",
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Black
                )
            }, rightSide = {
                Icon(
                    imageVector = Icons.Outlined.PersonOutline,
                    contentDescription = "",
                    tint = regularBlack,
                    modifier = iconModifier
                )
            }, modifier = Modifier.padding(12.dp))

            HeaderSelectionContent(headerVM) { sectionTitle: String ->
                headerVM.setSelectedSection(sectionTitle)
                contentVM.setView(sectionTitle)
            }

            when (screenType) {
                UserProfileScreenType.Profile -> ProfileScreen(
                    navController,
                    mainViewModel,
                    emailVM,
                    passwordVM,
                    displayNameVM,
                    passcodeVM
                )
                UserProfileScreenType.Friends -> FriendsScreen(navController = navController)
                UserProfileScreenType.Matches -> MatchesScreen(navController = navController)
                UserProfileScreenType.Posts -> PostsScreen(navController = navController)
                UserProfileScreenType.Comments -> CommentsScreen(navController = navController)
                UserProfileScreenType.Shares -> SharesScreen(navController = navController)
                UserProfileScreenType.Impressions -> ImpressionsScreen(navController = navController)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController(), mainViewModel = MainViewModel())
}