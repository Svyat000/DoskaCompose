package com.sddrozdov.domain.repository

import com.sddrozdov.domain.models.GoogleSignInData
import com.sddrozdov.domain.models.User
import kotlin.Result

interface AuthRepository {

    suspend fun deleteCurrentUser(): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun sendEmailVerification(user: User): Result<Unit>
    suspend fun linkEmailToGmail(email: String, password: String): Result<Unit>
    suspend fun signInWithGoogle(googleSignInData: GoogleSignInData): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun sendEmailForgotPassword(email: String) : Result<Unit>
    suspend fun getCurrentUser(): User?
}