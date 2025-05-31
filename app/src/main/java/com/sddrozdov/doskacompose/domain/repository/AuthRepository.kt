package com.sddrozdov.doskacompose.domain.repository

import androidx.credentials.Credential
import com.google.firebase.auth.FirebaseUser
import kotlin.Result

interface AuthRepository {
    suspend fun deleteCurrentUser(): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser>
    suspend fun signIn(email: String, password: String): Result<FirebaseUser>
    suspend fun sendEmailVerification(user: FirebaseUser): Result<Unit>
    suspend fun linkEmailToGmail(email: String, password: String): Result<Unit>
    suspend fun signInWithGoogle(credential: Credential): Result<FirebaseUser>
    suspend fun signOut(): Result<Unit>
    suspend fun sendEmailForgotPassword(email: String) : Result<Unit>
}