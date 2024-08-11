package com.example.jora.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.jora.model.LoginUserVerification
import com.example.jora.ui.theme.background
import com.example.jora.ui.theme.buttonTint
import com.example.jora.ui.theme.dmDisplay

@Composable
fun CreateAccountScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    emailVM: MainTextFieldVM,
    passwordVM: MainTextFieldVM,
    matchPasswordVM: MainTextFieldVM
) {

    val verification = LoginUserVerification()
    val emailErrorMsg = emailVM.errorMessage.collectAsState().value
    val passwordErrorMsg = passwordVM.errorMessage.collectAsState().value
    val password = passwordVM.textFieldText.collectAsState().value
    val matchPassword = matchPasswordVM.textFieldText.collectAsState().value

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(background)
            .fillMaxSize(1f)
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
        }

        item {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "",
                tint = buttonTint,
                modifier = Modifier
                    .clickable {

                    }
                    .padding(24.dp)
            )
        }

        item {
            MainTextField(
                title = "Email",
                icon = Icons.Outlined.Email,
                isPassword = false,
                vm = emailVM
            ) { email ->
                val emailResult = verification.verifyEmail(email)
                if (emailResult.isValid) {
                    // allow sign in
                    emailVM.showErrorMessage(false)
                } else {
                    emailVM.updateErrorMessage(emailResult.errorMessage)
                }
            }
        }

        item {
            MainTextField(
                title = "Password",
                icon = null,
                isPassword = true,
                vm = passwordVM
            ) { password ->
                val passwordResult = verification.verifyPassword(password)
                if (passwordResult.isValid) {
                    // allow sign in
                    passwordVM.showErrorMessage(false)
                } else {
                    passwordVM.updateErrorMessage(passwordResult.errorMessage)
                }
            }
        }

        item {
            MainTextField(
                title = "Match Password",
                icon = null,
                isPassword = true,
                vm = matchPasswordVM
            ) { password ->
                val passwordResult = verification.verifyPassword(matchPassword)
                if (passwordResult.isValid) {
                    // allow sign in
                    matchPasswordVM.showErrorMessage(false)
                } else {
                    matchPasswordVM.updateErrorMessage(passwordResult.errorMessage)
                }
            }
        }

        item {
            MainActionButton(Modifier,"Create Account") {
                // Email Requirement
                if (!emailVM.isEmptyTextField()) {
                    if (emailErrorMsg.isNotEmpty()) {
                        emailVM.showErrorMessage(true)
                        return@MainActionButton
                    }
                }

                // Password Requirement
                if (!passwordVM.isEmptyTextField()) {
                    if (passwordErrorMsg.isNotEmpty()) {
                        // Show error message
                        passwordVM.showErrorMessage(true)
                        return@MainActionButton
                    }

                    // Match Patch Requirement
                    if (password == matchPassword) {
                        val result = FirebaseAuthHelper().createUserWithEmailAndPassword(emailVM.getText(), passwordVM.getText())
                    } else {
                        matchPasswordVM.updateErrorMessage("Passwords do not match.")
                    }
                }
            }
        }

        item {
            MainSecondActionButton(
                modifier = Modifier.padding(bottom = 32.dp),
                "Cancel"
            ) {
                navController.navigate("home")
            }
        }
    }
}