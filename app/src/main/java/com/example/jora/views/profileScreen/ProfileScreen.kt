package com.example.jora.views.profileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Diversity3
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jora.BackgroundScreen
import com.example.jora.MainViewModel
import com.example.jora.composable.DualRowContent
import com.example.jora.composable.MainTextFieldVM
import com.example.jora.composable.ProfileActionButton
import com.example.jora.composable.ProfileSecondActionButton
import com.example.jora.composable.ProfileTextField
import com.example.jora.model.LoginUserVerification
import com.example.jora.ui.theme.background
import com.example.jora.ui.theme.buttonTint2
import com.example.jora.ui.theme.cardBackground
import com.example.jora.ui.theme.errorColor
import com.example.jora.ui.theme.errorColor2
import com.example.jora.ui.theme.montserrat
import com.example.jora.ui.theme.regularBlack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileToggleScreenVM(value: Boolean) : ViewModel() {

    private val _isSwitchChecked = MutableStateFlow(value)
    val isSwitchChecked: StateFlow<Boolean> = _isSwitchChecked

    fun onSwitchToggle(newValue: Boolean) {
        _isSwitchChecked.value = newValue
    }
}

enum class AppSettingType {
    EnabledPasscode,
    EnabledDarkMode,
    EnabledDatingMode,
    TurnOffFriendRequests,
    Region,
    Language
}

@Composable
fun ProfileScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    emailVM: MainTextFieldVM,
    passwordVM: MainTextFieldVM,
    displayNameVM: MainTextFieldVM,
    passcodeVM: MainTextFieldVM
) {
    val user = mainViewModel.user.collectAsState().value


    // setting text field text
    val emailText = emailVM.textFieldText.collectAsState().value
    val displayNameText = displayNameVM.textFieldText.collectAsState().value
    // updating values
    val isUpdatingEmail = emailText == user?.authDetails?.email
    val isUpdatingDisplayName = displayNameText != user?.authDetails?.displayName


    if (!isUpdatingEmail && emailText.isEmpty()) {
        emailVM.updateText(user?.authDetails?.email ?: "")
    }

    if (isUpdatingDisplayName) {
        displayNameVM.updateText(user?.authDetails?.displayName ?: "")
    }

    val appSettings = mainViewModel.appSettings.collectAsState().value
    val enabledDarkMode = appSettings?.enabledDarkMode ?: true
    val turnOffFriendRequests = appSettings?.turnOffFriendRequests ?: false
    val enabledDatingMode = appSettings?.enabledDatingMode ?: true
    val enabledPasscode = appSettings?.enabledPasscode ?: false

    val verification = LoginUserVerification()
    val emailErrorMsg = emailVM.errorMessage.collectAsState().value
    val passwordErrorMsg = emailVM.errorMessage.collectAsState().value
    val displayNameErrorMsg = emailVM.errorMessage.collectAsState().value
    val passcodeErrorMsg = emailVM.errorMessage.collectAsState().value

    BackgroundScreen(padding = PaddingValues(0.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn {
            item {
                ProfileTextField(
                    title = "Display Name",
                    isPassword = false,
                    vm = displayNameVM
                ) { displayName ->
                    val displayNameResult = verification.verifyEmail(displayName)
                    displayNameVM.handleLoginValidation(displayNameErrorMsg, displayNameResult)


                }

                if (!isUpdatingDisplayName) {
                    DualRowContent(leftSide = {
                        ProfileSecondActionButton(
                            modifier = Modifier.padding(end = 8.dp),
                            "Update",
                            color = buttonTint2
                        ) {
                            // TODO: update display name
                        }
                    }, rightSide = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            ProfileSecondActionButton(
                                modifier = Modifier,
                                "Cancel",
                                color = errorColor
                            ) {
                                // TODO: cancel display name update
                            }
                        }
                    })
                }

                ProfileTextField(
                    title = "Email",
                    isPassword = false,
                    vm = emailVM
                ) { email ->
                    val emailResult = verification.verifyEmail(email)
                    emailVM.handleLoginValidation(emailErrorMsg, emailResult)
                }

                if (!isUpdatingEmail) {
                    DualRowContent(leftSide = {
                        ProfileSecondActionButton(
                            modifier = Modifier.padding(end = 8.dp),
                            "Update",
                            color = buttonTint2
                        ) {
                            // TODO: update display name
                        }
                    }, rightSide = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            ProfileSecondActionButton(
                                modifier = Modifier,
                                "Cancel",
                                color = errorColor
                            ) {
                                // TODO: cancel display name update
                            }
                        }
                    })
                }

                ProfileScreenToggle(
                    title = "Dark Mode",
                    imageVector = Icons.Outlined.DarkMode,
                    viewModel = ProfileToggleScreenVM(enabledDarkMode)
                ) { newValue: Boolean ->
                    mainViewModel.updateAppSettings(AppSettingType.EnabledDarkMode, newValue)
                }

                ProfileScreenToggle(
                    title = "Friend Requests",
                    imageVector = Icons.Outlined.Diversity3,
                    viewModel = ProfileToggleScreenVM(turnOffFriendRequests)) { newValue: Boolean ->
                    mainViewModel.updateAppSettings(AppSettingType.EnabledDarkMode, newValue)
                }

                Text(
                    text = "Dating mode allows you to send other users matches, see other user's quiz scores, and more. " +
                            "To use all of the dating features you will need to complete all the dating quizzes and be invited by a friend.",
                    fontFamily = montserrat,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                )

                ProfileScreenToggle(
                    title = "Dating Mode",
                    imageVector = Icons.Outlined.FavoriteBorder,
                    viewModel = ProfileToggleScreenVM(enabledDatingMode)) { newValue: Boolean ->
                    mainViewModel.updateAppSettings(AppSettingType.EnabledDatingMode, newValue)

                }

                Text(
                    text = "Turning on Remember Password... You'll need to sign in one more time with your email and password. " +
                            "Then you will not need to sign in until you to Remember Password off.",
                    fontFamily = montserrat,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                )

                ProfileScreenToggle(
                    title = "Remember Password",
                    imageVector = Icons.Outlined.Lock,
                    viewModel = ProfileToggleScreenVM(enabledPasscode)) { newValue: Boolean ->
                    mainViewModel.updateAppSettings(AppSettingType.EnabledPasscode, newValue)
                }

                Text(
                    text = "You can only update your password after, you have turned on Remember Me, and use successfully " +
                            "logged in using the Remember Me feature.",
                    fontFamily = montserrat,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                )

                ProfileScreenToggle(
                    title = "Update Password",
                    imageVector = Icons.Outlined.Lock,
                    viewModel = ProfileToggleScreenVM(enabledPasscode)) { newValue: Boolean ->
                    mainViewModel.updateAppSettings(AppSettingType.EnabledPasscode, newValue)
                }



                ProfileScreenToggle(
                    title = "Quick Password",
                    imageVector = Icons.Outlined.Security,
                    viewModel = ProfileToggleScreenVM(enabledPasscode)) { newValue: Boolean ->
                    mainViewModel.updateAppSettings(AppSettingType.EnabledPasscode, newValue)
                }

                ProfileTextField(
                    title = "Passcode",
                    isPassword = false,
                    vm = passcodeVM
                ) { passcode ->
                    val passcodeResult = verification.verifyDisplayName(passcode)
                    passcodeVM.handleLoginValidation(passcodeErrorMsg, passcodeResult)

                }

                DualRowContent(leftSide = {
                    ProfileSecondActionButton(
                        modifier = Modifier.padding(end = 8.dp),
                        "Update",
                        color = buttonTint2
                    ) {
                        // TODO: update passcode
                    }
                }, rightSide = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        ProfileSecondActionButton(
                            modifier = Modifier,
                            "Cancel",
                            color = errorColor
                        ) {
                            // TODO: cancel passcode update
                        }
                    }
                })

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "We are sorry to see you go. Your account will be deleted 30 days from now. " +
                            "None of your information will be saved, so be sure you want to delete your " +
                            "account, your friends will miss you.",
                    fontFamily = montserrat,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                )

                ProfileActionButton(
                    modifier = Modifier.padding(8.dp),
                    "Delete Account",
                    color = errorColor2
                ) {
                    // TODO: show delete account alert dialog
                    // TODO: delete user account
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    ProfileScreen(
        rememberNavController(),
        MainViewModel(),
        MainTextFieldVM(),
        MainTextFieldVM(),
        MainTextFieldVM(),
        MainTextFieldVM()
    )
}

val switchColor = SwitchColors(
    checkedThumbColor = Color.White,
    checkedTrackColor = buttonTint2,
    checkedBorderColor = Color.LightGray,
    checkedIconColor = Color.Transparent,
    uncheckedThumbColor = Color.White,
    uncheckedTrackColor = cardBackground,
    uncheckedBorderColor = Color.LightGray,
    uncheckedIconColor = Color.Transparent,
    disabledCheckedThumbColor = Color.Transparent,
    disabledCheckedTrackColor = Color.Transparent,
    disabledCheckedBorderColor = Color.Transparent,
    disabledCheckedIconColor = Color.Transparent,
    disabledUncheckedThumbColor = Color.Transparent,
    disabledUncheckedTrackColor = Color.Transparent,
    disabledUncheckedBorderColor = Color.Transparent,
    disabledUncheckedIconColor = Color.Transparent
)


@Composable
fun ProfileScreenToggle(
    title: String,
    imageVector: ImageVector,
    viewModel: ProfileToggleScreenVM,
    action: (Boolean) -> Unit
) {
    val switchState = viewModel.isSwitchChecked.collectAsState().value

    DualRowContent(
        leftSide = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(45.dp)
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "",
                    tint = regularBlack,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .background(background, shape = RoundedCornerShape(6.dp))
                        .border(1.dp, regularBlack, shape = RoundedCornerShape(6.dp))
                        .padding(8.dp)
                )
                Text(
                    text = title,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        rightSide = {
            Switch(
                checked = switchState,
                onCheckedChange = { newValue ->
                    viewModel.onSwitchToggle(newValue)
                    action(newValue)
                },
                colors = switchColor
            )
        },
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .border(1.38.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
            .padding(8.dp)
    )
}