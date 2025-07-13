package com.sddrozdov.domain.useCase

import com.sddrozdov.domain.models.GoogleSignInData
import com.sddrozdov.domain.models.User
import com.sddrozdov.domain.repository.AuthRepository
import javax.inject.Inject

interface AuthUseCaseInterface {

    suspend fun signUp(email: String, password: String): Result<User>

    suspend fun signIn(email: String, password: String): Result<User>

    suspend fun sendVerificationEmail(user: User): Result<Unit>

    suspend fun signInWithGoogle(googleSignInData: GoogleSignInData): Result<User>

    suspend fun signOut(): Result<Unit>

    suspend fun sendEmailForgotPassword(email: String): Result<Unit>

    suspend fun signInAnonymously(): Result<Unit>

    suspend fun isUserAnonymousOrAuthorized(): Boolean

    suspend fun getCurrentUser(): User?
}

class AuthUseCase @Inject constructor(private val repository: AuthRepository): AuthUseCaseInterface {

    override suspend fun signUp(email: String, password: String): Result<User> {
        if (email.isEmpty() || password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Email or password is empty"))
        }
        val signUpResult = repository.signUp(email, password)

        if (signUpResult.isFailure) {
            val ex = signUpResult.exceptionOrNull()
            if (ex is AuthError.EmailAlreadyInUse) {
                val linkResult = repository.linkEmailToGmail(email, password)
                return if (linkResult.isSuccess) {
                    repository.getCurrentUser()?.let { user ->
                        Result.success(user)
                    } ?: Result.failure(Exception("User not found after linking"))
                } else {
                    Result.failure(
                        linkResult.exceptionOrNull() ?: Exception("Unknown linking error")
                    )
                }
            }
            return Result.failure(ex ?: Exception("Unknown sign up error"))
        }

        return signUpResult
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        if (email.isEmpty() || password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Email or password is empty"))
        }

//        val deleteResult = repository.deleteCurrentUser()
//        if (deleteResult.isFailure) {
//            Log.w("AuthUseCase", "Delete current user failed: ${deleteResult.exceptionOrNull()}")
//        }

        return repository.signIn(email, password)
    }

    override suspend fun sendVerificationEmail(user: User): Result<Unit> {
        return repository.sendEmailVerification(user)
    }

    override suspend fun signInWithGoogle(googleSignInData: GoogleSignInData): Result<User> {
        return repository.signInWithGoogle(googleSignInData)
    }

    override suspend fun signOut(): Result<Unit> {
        return repository.signOut()
    }

    override suspend fun sendEmailForgotPassword(email: String): Result<Unit> {
        return repository.sendEmailForgotPassword(email)
    }

    override suspend fun signInAnonymously(): Result<Unit> {
        return repository.signInAnonymously()
    }

    override suspend fun isUserAnonymousOrAuthorized(): Boolean {
        return repository.isUserAnonymous()
    }

    override suspend fun getCurrentUser(): User? {
        return repository.getCurrentUser()
    }
}