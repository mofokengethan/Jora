package com.example.jora.extensionAPI

import com.example.jora.model.UserProfile
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserFirebase(
    var uid: String,
    var displayName: String,
    var isEmailVerified: Boolean,
    var email: String,
    var creationTimestamp: String,
    var lastSignInTimestamp: String
)

class FirebaseAuthHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    class CreateAccountResultHelper(
        var authResult: AuthResult? = null,
        var errorMessage: String? = null,
        var userFirebase: UserFirebase? = null
    )

    fun createUserFirebase(firebaseUser: FirebaseUser): UserFirebase {
        return UserFirebase(
            uid = firebaseUser.uid,
            displayName = firebaseUser.displayName ?: "",
            isEmailVerified = firebaseUser.isEmailVerified,
            email = firebaseUser.email ?: "",
            creationTimestamp = formatTimestamp(firebaseUser.metadata?.creationTimestamp ?: 0),
            lastSignInTimestamp = formatTimestamp(firebaseUser.metadata?.lastSignInTimestamp ?: 0)
        )
    }

    fun createUserProfile(userFirebase: UserFirebase): UserProfile {
        return UserProfile(UserFirebase(
            userFirebase.uid,
            userFirebase.displayName,
            userFirebase.isEmailVerified,
            userFirebase.email,
            userFirebase.creationTimestamp,
            userFirebase.lastSignInTimestamp
        ))
    }

    fun formatTimestamp(timestamp: Long): String {
        if (timestamp == 0L) return ""
        val date = Date(timestamp)
        val format = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun isCompleteUserFirebase(userFirebase: UserFirebase): Boolean {
        if (userFirebase.displayName.isEmpty()) return false
        if (userFirebase.email.isEmpty()) return false
        if (userFirebase.creationTimestamp.isEmpty()) return false
        if (userFirebase.lastSignInTimestamp.isEmpty()) return false
        return true
    }

    // Get current user
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Get current user's email
    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    // Get current user's UID
    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    // Check if current user's email is verified
    fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
    }

    // Get current user's display name
    fun getCurrentUserDisplayName(): String? {
        return auth.currentUser?.displayName
    }

    // Verify before updating email
    suspend fun verifyBeforeUpdateEmail(newEmail: String) {
        auth.currentUser?.verifyBeforeUpdateEmail(newEmail)?.await()
    }

    // Delete current user
    suspend fun deleteUser() {
        auth.currentUser?.delete()?.await()
    }

    // Update profile
    suspend fun updateProfileDisplayName(displayName: String?) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()
        auth.currentUser?.updateProfile(profileUpdates)?.await()
    }

    // Send email verification
    suspend fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()?.await()
    }

    // Get user metadata
    fun getUserCreationTimestamp(): Long? {
        return auth.currentUser?.metadata?.creationTimestamp
    }

    fun getUserLastSignInTimestamp(): Long? {
        return auth.currentUser?.metadata?.lastSignInTimestamp
    }

    // Get language code
    fun getLanguageCode(): String? {
        return auth.languageCode
    }

    // Get auth UID
    fun getAuthUid(): String? {
        return auth.uid ?: ""
    }

    // Sign out
    fun signOut() {
        auth.signOut()
    }

    // Sign in with email and password
    fun signInWithEmailAndPassword(email: String, password: String, onComplete: (CreateAccountResultHelper) -> Unit) {
        val createAccountResult = CreateAccountResultHelper()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result: Task<AuthResult> ->

            // success
            result.addOnSuccessListener {
                it.user?.let { firebaseUser: FirebaseUser ->
                    // successful user returned
                    val userFirebase = createUserFirebase(firebaseUser)
                    // complete firebase user
                    createAccountResult.userFirebase = userFirebase
                    onComplete(createAccountResult)
                } ?: run { // no firebase user returned
                    createAccountResult.errorMessage = "Failed to find all the information for your profile."
                    onComplete(createAccountResult)
                }
            }

            // failure
            result.addOnFailureListener {
                createAccountResult.errorMessage = it.message
                onComplete(createAccountResult)
            }
        }
    }

    // Send password reset email
    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    // Confirm password reset
    suspend fun confirmPasswordReset(code: String, newPassword: String) {
        auth.confirmPasswordReset(code, newPassword).await()
    }

    // Verify password reset code
    suspend fun verifyPasswordResetCode(code: String): String {
        return auth.verifyPasswordResetCode(code).await()
    }

    // Create user with email and password
     fun createUserWithEmailAndPassword(email: String, password: String): CreateAccountResultHelper {
        val createAccountResult = CreateAccountResultHelper()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result: Task<AuthResult> ->
            result.addOnSuccessListener {
                it.user?.let { firebaseUser: FirebaseUser ->
                    // successful user returned
                    val userFirebase = createUserFirebase(firebaseUser)
                    // check for missing data
                    if (isCompleteUserFirebase(userFirebase)) {
                        // complete firebase user
                        createAccountResult.userFirebase = userFirebase
                    } else {
                        createAccountResult.errorMessage = "Failed to find all the information for your profile."
                    }
                } ?: run { // no firebase user returned
                    createAccountResult.errorMessage = "Failed to find all the information for your profile."
                }
            }
            result.addOnFailureListener {
                println("FirebaseAuthHelper: ")
                createAccountResult.errorMessage = it.message
            }
         }
        return createAccountResult
    }
}