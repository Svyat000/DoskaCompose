package com.sddrozdov.doskacompose.domain.useCase

import android.util.Log
import androidx.credentials.Credential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.sddrozdov.doskacompose.domain.repository.AuthRepository
import javax.inject.Inject
import kotlin.Result

class AuthUseCase @Inject constructor(private val repository: AuthRepository) {


    suspend fun signUp(email: String, password: String): Result<FirebaseUser> {
        if (email.isEmpty() || password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Email or password is empty"))
        }

        val signUpResult = repository.signUp(email, password)

        if (signUpResult.isFailure) {
            val ex = signUpResult.exceptionOrNull()
            if (ex is FirebaseAuthUserCollisionException) {
                val linkResult = repository.linkEmailToGmail(email, password)
                return if (linkResult.isSuccess) {
                    repository.currentUser?.let { user ->
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

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        if (email.isEmpty() || password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Email or password is empty"))
        }

//        val deleteResult = repository.deleteCurrentUser()
//        if (deleteResult.isFailure) {
//            Log.w("AuthUseCase", "Delete current user failed: ${deleteResult.exceptionOrNull()}")
//        }

        return repository.signIn(email, password)
    }

    suspend fun sendVerificationEmail(user: FirebaseUser): Result<Unit> {
        return repository.sendEmailVerification(user)
    }

    suspend fun signInWithGoogle(credential: Credential): Result<FirebaseUser> {
        return repository.signInWithGoogle(credential)
    }

    suspend fun signOut(): Result<Unit> {
        Log.d("EXIT", "ЮЗ КЕйс вышли ")
        return repository.signOut()

    }

    suspend fun sendEmailForgotPassword(email: String): Result<Unit> {
        return repository.sendEmailForgotPassword(email)
    }

}