package com.example.jora.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jora.BackgroundScreen
import com.example.jora.MainViewModel
import com.example.jora.composable.AabanaButton
import com.example.jora.composable.AabanaSecondButton
import com.example.jora.composable.AabanaTextField
import com.example.jora.composable.DualRowContent
import com.example.jora.composable.MainActionButton
import com.example.jora.composable.MainSecondActionButton
import com.example.jora.composable.MainTextField
import com.example.jora.composable.MainTextFieldVM
import com.example.jora.extensionAPI.FirebaseAuthHelper
import com.example.jora.extensionAPI.UserFirebase
import com.example.jora.model.LoginUserVerification
import com.example.jora.ui.theme.background
import com.example.jora.ui.theme.buttonTint
import com.example.jora.ui.theme.buttonTint2
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
    val fbAuthHelper = FirebaseAuthHelper()

    val emailErrorMsg = emailVM.errorMessage.collectAsState().value
    val passwordErrorMsg = passwordVM.errorMessage.collectAsState().value
    val matchPasswordErrorMsg = matchPasswordVM.errorMessage.collectAsState().value

    var loadingUser by remember { mutableStateOf(false) }


    BackgroundScreen(padding = PaddingValues()) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(14.dp)
        ) {
            DualRowContent(
                modifier = Modifier.padding(vertical = 24.dp),
                leftSide = {
                    Text(
                        "Aabana",
                        color = Color.Black,
                        fontFamily = FontFamily.Serif,
                        fontSize = 36.sp
                    )
                }
            )


            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                item {

                    AabanaTextField(label = "Email", "Enter email here", isPassword = false, vm = emailVM) { email ->
                        val emailResult = verification.verifyEmail(email)
                        emailVM.handleLoginValidation(emailErrorMsg, emailResult)
                    }

                    AabanaTextField(label = "Password", "Enter password here", isPassword = true, vm = passwordVM) { password ->
                        val passwordResult = verification.verifyPassword(password)
                        passwordVM.handleLoginValidation(passwordErrorMsg, passwordResult)
                    }

                    AabanaTextField(label = "Match Password", "Match your password", isPassword = false, vm = matchPasswordVM) { password ->
                        val matchPasswordResult = verification.verifyPassword(password)
                        matchPasswordVM.handleLoginValidation(matchPasswordErrorMsg, matchPasswordResult)
                    }

                    AabanaButton(Modifier.padding(vertical = 18.dp), "Create Account") {
                        /** show progress indicator for loading */
                        loadingUser = true
                        /** check empty email, password, error message... check error message */
                        /** return false if vm text is empty or vm error msg contains a string */
                        /** will assign empty error message if vm text is empty  */
                        loadingUser = passwordVM.getText() == matchPasswordVM.getText()
                        if (!loadingUser) {
                            emailVM.updateErrorMessage("Passwords do not match")
                            emailVM.showErrorMessage(true)
                        }
                        loadingUser = emailVM.validateField(emailVM, emailErrorMsg, "Email cannot be empty. Try a valid email address.")
                        loadingUser = passwordVM.validateField(passwordVM, passwordErrorMsg, "Password cannot be empty. Try a valid password.")
                        if (!loadingUser) return@AabanaButton
                        val createAccountResult = fbAuthHelper.createUserWithEmailAndPassword(emailVM.getText(), passwordVM.getText())
                        /** checking for firebase error message - when creating account */
                        if (createAccountResult.errorMessage?.isNotEmpty() == false) {
                            emailVM.updateErrorMessage(createAccountResult.errorMessage)
                            emailVM.showErrorMessage(true)
                            loadingUser = false
                            return@AabanaButton
                        }
                        /** remove textfield text */
                        passwordVM.updateText("")
                        /** add current user to app view model */
                        createAccountResult.userFirebase?.let { userFirebase: UserFirebase ->
                            mainViewModel.setLoginUser(fbAuthHelper.createUserProfile(userFirebase))
                            /** navigate to display name */
                            navController.navigate("displayName")
                        }
                        /** end loading progress indicator*/
                        loadingUser = false
                    }

                    AabanaSecondButton(Modifier, "Cancel") {

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CreateAccountScreen(
        navController = rememberNavController(),
        mainViewModel = MainViewModel(),
        emailVM = MainTextFieldVM(),
        passwordVM = MainTextFieldVM(),
        matchPasswordVM = MainTextFieldVM()
    )
}