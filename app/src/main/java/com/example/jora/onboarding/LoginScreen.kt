package com.example.jora.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jora.MainViewModel
import com.example.jora.composable.MainActionButton
import com.example.jora.composable.MainSecondActionButton
import com.example.jora.composable.MainTextField
import com.example.jora.composable.MainTextFieldVM
import com.example.jora.extensionAPI.FirebaseAuthHelper
import com.example.jora.extensionAPI.UserFirebase
import com.example.jora.model.LoginUserVerification
import com.example.jora.ui.theme.background
import com.example.jora.ui.theme.buttonTint2
import com.example.jora.ui.theme.dmDisplay

@Composable
fun LoginScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    emailVM: MainTextFieldVM,
    passwordVM: MainTextFieldVM
) {
    val verification = LoginUserVerification()
    val fbAuthHelper = FirebaseAuthHelper()

    emailVM.updateText("demo@gmail.com")
    passwordVM.updateText("Password@1234")

    val emailErrorMsg = emailVM.errorMessage.collectAsState().value
    val passwordErrorMsg = passwordVM.errorMessage.collectAsState().value

    var loadingUser by remember { mutableStateOf(false) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize(1f)
            .background(background)
            .padding(12.dp)
    ) {
        item {
            Text(
                "abc Social",
                color = Color.Black,
                fontFamily = dmDisplay,
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (loadingUser) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(24.dp).size(24.dp),
                    color = buttonTint2,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "",
                    tint = buttonTint2,
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(24.dp)
                )
            }

            MainTextField(
                title = "Email",
                icon = Icons.Outlined.Email,
                isPassword = false,
                vm = emailVM
            ) { email ->
                val emailResult = verification.verifyEmail(email)
                emailVM.handleLoginValidation(emailErrorMsg, emailResult)
            }

            MainTextField(
                title = "Password",
                icon = null,
                isPassword = true,
                vm = passwordVM
            ) { password ->
                val passwordResult = verification.verifyPassword(password)
                passwordVM.handleLoginValidation(passwordErrorMsg, passwordResult)
            }

            MainActionButton(Modifier,"Sign In") { 
                // show progress indicator
                loadingUser = true
                // check for empty textfield and error message
                // return false if textfield is invalid
                loadingUser = emailVM.validateField(emailVM, emailErrorMsg, "Email cannot be empty. Try a valid email address.")
                loadingUser = passwordVM.validateField(passwordVM, passwordErrorMsg, "Password cannot be empty. Try a valid password.")
                // check invalid text
                if (!loadingUser) return@MainActionButton
                // sign in with valid text
                fbAuthHelper.signInWithEmailAndPassword(
                    emailVM.getText(),
                    passwordVM.getText()
                ) { result: FirebaseAuthHelper.CreateAccountResultHelper ->
                    // handle error
                    result.errorMessage?.let { message: String ->
                        loadingUser = false
                        // error message - firebase
                        emailVM.updateErrorMessage(message)
                        emailVM.showErrorMessage(true)
                    } ?: run { // on success
                        // auth user - firebase
                        result.userFirebase?.let { user: UserFirebase ->
                            // add current user to app view model
                            mainViewModel.setLoginUser(fbAuthHelper.createUserProfile(user))
                            // remove textfield text
                            passwordVM.updateText("")
                            // main screen - navigate
                            navController.navigate("mainScreen")
                            // hide progress indicator
                            loadingUser = false
                        } ?: run {
                            // hide progress indicator
                            loadingUser = false
                            // update and show error message
                            emailVM.updateErrorMessage("Something went wrong. Please try again.")
                            emailVM.showErrorMessage(true)
                        }
                    }
                }
            }

            MainSecondActionButton(
                modifier = Modifier.padding(bottom = 32.dp),
                "Create Account"
            ) {
                navController.navigate("createAccount")
            }
        }
    }
}