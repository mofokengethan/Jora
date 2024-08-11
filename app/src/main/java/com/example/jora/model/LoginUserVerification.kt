package com.example.jora.model

import com.example.jora.extensionAPI.FirebaseAuthHelper

class LoginUserVerification {

    data class VerificationResult(val isValid: Boolean, val errorMessage: String? = null)

    // Check if the email is valid
    fun verifyEmail(email: String): VerificationResult {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$".toRegex()
        return if (email.matches(emailRegex)) {
            VerificationResult(true)
        } else {
            VerificationResult(false, "This doesn't look like a valid email. Please trying again.")
        }
    }

    // Check if the password meets the requirements
    fun verifyPassword(password: String): VerificationResult {
        // Example: At least 8 characters, at least one uppercase letter, one lowercase letter, one digit, and one special character
        val passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{12,}\$".toRegex()
        return if (password.matches(passwordRegex)) {
            VerificationResult(true)
        } else {
            VerificationResult(false, "Passwords minimum 12 characters, one uppercase, one lowercase, one digit, and one special character.")
        }
    }

    fun verifyDisplayName(displayName: String): VerificationResult {
        // Define a regular expression pattern for valid display names (only letters and numbers)
        val pattern = "^[a-zA-Z0-9]+$".toRegex()

        return when {
            displayName.isEmpty() -> {
                VerificationResult(false, "Display name cannot be empty.")
            }
            displayName.length !in 3..36 -> {
                VerificationResult(false, "Display name must be between 3 and 36 characters long.")
            }
            !pattern.matches(displayName) -> {
                VerificationResult(false, "Display name can only contain letters and numbers.")
            }
            else -> {
                VerificationResult(true)
            }
        }
    }
}

// Example usage in another part of your code:
// val verification = UserVerification()
// val emailResult = verification.verifyEmail("example@test.com")
// if (!emailResult.isValid) {
//     println(emailResult.errorMessage) // Show this message to the user
// }
