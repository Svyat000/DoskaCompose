package com.sddrozdov.domain.useCase

import com.sddrozdov.domain.AuthError
import com.sddrozdov.domain.models.GoogleSignInData
import com.sddrozdov.domain.models.User
import com.sddrozdov.domain.repository.AuthRepository
import javax.inject.Inject
import kotlin.Result

class AuthUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend fun signUp(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(AuthError.EmptyCredentials)
        }

        val result = repository.signUp(email, password)
        return if (result.isSuccess) {
            result
        } else {
            handleSignUpFailure(result, email, password)
        }
    }

    private suspend fun handleSignUpFailure(
        result: Result<User>,
        email: String,
        password: String
    ): Result<User> {
        val exception = result.exceptionOrNull()
        return if (exception is AuthError.EmailAlreadyInUse) {
            linkEmailToGoogleAccount(email, password)
        } else {
            Result.failure(exception ?: AuthError.UnknownError())
        }
    }

    private suspend fun linkEmailToGoogleAccount(email: String, password: String): Result<User> {
        val linkResult = repository.linkEmailToGmail(email, password)
        return if (linkResult.isSuccess) {
            val user = repository.getCurrentUser()
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(AuthError.UserNotFound)
            }
        } else {

            Result.failure(AuthError.UnknownError(linkResult.exceptionOrNull()))
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        if (email.isEmpty() || password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Email or password is empty"))
        }

//        val deleteResult = repository.deleteCurrentUser()
//        if (deleteResult.isFailure) {
//            Log.w("AuthUseCase", "Delete current user failed: ${deleteResult.exceptionOrNull()}")
//        }

        return repository.signIn(email, password)
    }

    suspend fun sendVerificationEmail(user: User): Result<Unit> {
        return repository.sendEmailVerification(user)
    }

    suspend fun signInWithGoogle(googleSignInData: GoogleSignInData): Result<User> {
        return repository.signInWithGoogle(googleSignInData)
    }

    suspend fun signOut(): Result<Unit> {
        return repository.signOut()

    }

    suspend fun sendEmailForgotPassword(email: String): Result<Unit> {
        return repository.sendEmailForgotPassword(email)
    }

}
