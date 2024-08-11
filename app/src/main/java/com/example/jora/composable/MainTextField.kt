package com.example.jora.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.jora.model.LoginUserVerification
import com.example.jora.ui.theme.background
import com.example.jora.ui.theme.cardBackground
import com.example.jora.ui.theme.dmSerifDisplay
import com.example.jora.ui.theme.errorColor
import com.example.jora.ui.theme.montserrat
import com.example.jora.ui.theme.regularBlack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainTextFieldVM : ViewModel() {

    private var _textFieldText: MutableStateFlow<String> = MutableStateFlow("")
    var textFieldText: StateFlow<String> = _textFieldText

    private var _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    var errorMessage: StateFlow<String> = _errorMessage

    private var _showErrorMessage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var showErrorMessage: StateFlow<Boolean> = _showErrorMessage

    fun getText(): String {
        return textFieldText.value
    }

    fun updateText(newText: String) {
        _textFieldText.value = newText
    }

    fun isEmptyTextField(): Boolean {
        return textFieldText.value.isEmpty()
    }

    fun validateField(
        fieldVM: MainTextFieldVM,
        currentErrorMsg: String?,
        newErrorMessage: String,
    ): Boolean {
        return when {
            fieldVM.isEmptyTextField() -> {
                fieldVM.updateErrorMessage(newErrorMessage)
                fieldVM.showErrorMessage(true)
                false
            }
            currentErrorMsg?.isNotEmpty() == true -> {
                fieldVM.showErrorMessage(true)
                false
            }
            else -> { true }
        }
    }


    fun handleLoginValidation(errorMsg: String?, result:  LoginUserVerification.VerificationResult) {
        when (errorMsg) {
            "Email cannot be empty. Try a valid email address.",
            "Password cannot be empty. Try a valid password.",
            "Something went wrong. Please try again." -> {
                updateErrorMessage("")
                showErrorMessage(false)
            }
            else -> {
                if (result.isValid) {
                    // allow sign in
                    updateErrorMessage("")
                    showErrorMessage(false)
                } else {
                    updateErrorMessage(result.errorMessage)
                }
            }
        }
    }

    fun showErrorMessage(show: Boolean) {
        _showErrorMessage.value = show
        if (!show) {
            _errorMessage.value = ""
        }
    }

    fun updateErrorMessage(errorMsg: String?) {
        errorMsg?.let {
            _errorMessage.value = errorMsg
        } ?: run {
            _errorMessage.value = ""
        }
    }
}

@Composable
fun MainTextField(
    title: String,
    icon: ImageVector?,
    isPassword: Boolean,
    vm: MainTextFieldVM, // Initialize ViewModel here
    text: (String) -> Unit
) {

    val vmText = vm.textFieldText.collectAsState().value
    val vmShowErrorMessage = vm.showErrorMessage.collectAsState().value
    val vmErrorMessage = vm.errorMessage.collectAsState().value

    BasicTextField(
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
        ),
        modifier = Modifier
            .padding(bottom = 12.dp)
            .background(color = cardBackground)
            .border(1.38.dp, regularBlack, shape = RoundedCornerShape(4.dp))
            .fillMaxWidth(1f),
        value = vmText, onValueChange = { newText ->
            vm.updateText(newText)
            text(newText)
        },
        visualTransformation = PasswordVisualTransformation(),
        cursorBrush = SolidColor(Color.Black),
        decorationBox = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(12.dp)
            ) {
                DualRowContent(
                    modifier = Modifier.padding(bottom = 6.dp),
                    leftSide = {
                        Text(
                            title,
                            color = regularBlack,
                            fontFamily = dmSerifDisplay,
                            fontSize = 32.sp
                        )
                    },
                    rightSide = {
                        if (isPassword) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "",
                                tint = regularBlack,
                                modifier = Modifier
                                    .background(background, shape = RoundedCornerShape(6.dp))
                                    .border(1.dp, regularBlack, shape = RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            )
                        } else {
                            icon?.let {
                                Icon(
                                    imageVector = icon ,
                                    contentDescription = icon.name,
                                    tint = regularBlack,
                                    modifier = Modifier
                                        .background(background, shape = RoundedCornerShape(6.dp))
                                        .border(1.dp, regularBlack, shape = RoundedCornerShape(6.dp))
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                )

                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = if (vmShowErrorMessage) vmErrorMessage else "",
                    fontFamily = montserrat,
                    color = errorColor,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W300
                )
                Text(
                    vmText,
                    color = Color.Black,
                    fontFamily = montserrat,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
                        .background(Color.White, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                )
            }
        }
    )
}

@Composable
fun ProfileTextField(
    title: String,
    isPassword: Boolean,
    vm: MainTextFieldVM, // Initialize ViewModel here
    text: (String) -> Unit
) {

    val vmText = vm.textFieldText.collectAsState().value
    val vmShowErrorMessage = vm.showErrorMessage.collectAsState().value
    val vmErrorMessage = vm.errorMessage.collectAsState().value

    BasicTextField(
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
        ),
        modifier = Modifier
            .padding(8.dp)
            .background(color = cardBackground)
            .border(1.38.dp, regularBlack, shape = RoundedCornerShape(4.dp))
            .fillMaxWidth(1f),
        value = vmText, onValueChange = { newText ->
            vm.updateText(newText)
            text(newText)
        },
        visualTransformation = PasswordVisualTransformation(),
        cursorBrush = SolidColor(Color.Black),
        decorationBox = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(6.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = "$title: ${if (vmShowErrorMessage) vmErrorMessage else ""}",
                    fontFamily = FontFamily.Monospace,
                    color = if (vmShowErrorMessage) errorColor else regularBlack,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W300
                )
                Text(
                    vmText,
                    color = Color.Black,
                    fontFamily = montserrat,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
                        .background(Color.White, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                )
            }
        }
    )
}