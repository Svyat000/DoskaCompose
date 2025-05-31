package com.sddrozdov.doskacompose.data.repository

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.ClearCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sddrozdov.doskacompose.domain.repository.AuthRepository
import kotlin.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(val firebaseAuth: FirebaseAuth) : AuthRepository {

    override suspend fun deleteCurrentUser(): Result<Unit> = try {
        firebaseAuth.currentUser?.delete()?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUp(email: String, password: String): Result<FirebaseUser> = try {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("User  is null")
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User  is null")

            if (!user.isEmailVerified) {
                throw Exception("Email is not verified. Please verify your email before signing in.")
            }
            Result.success(user)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("No user found with this email."))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid password. Please try again."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendEmailVerification(user: FirebaseUser): Result<Unit> = try {
        user.sendEmailVerification().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun linkEmailToGmail(email: String, password: String): Result<Unit> = try {
        val credential = EmailAuthProvider.getCredential(email, password)
        val currentUser = firebaseAuth.currentUser ?: throw Exception("No current user")
        currentUser.linkWithCredential(credential).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signInWithGoogle(credential: Credential): Result<FirebaseUser> {
        return try {

            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val result = firebaseAuth.signInWithCredential(firebaseCredential).await()
                val user = result.user ?: throw Exception("User  is null")

                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credential type"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun signOut(): Result<Unit> = try {
        firebaseAuth.signOut()
        Result.success(Unit)
    } catch (e: ClearCredentialException) {
        Result.failure(e)

    }

    override suspend fun sendEmailForgotPassword(email: String): Result<Unit> = try {
        firebaseAuth.sendPasswordResetEmail(email)
        Result.success(Unit)
    } catch (e: FirebaseAuthException){
        Result.failure(e)
    }
}