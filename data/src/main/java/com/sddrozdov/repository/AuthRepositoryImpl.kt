package com.sddrozdov.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sddrozdov.domain.models.GoogleSignInData
import com.sddrozdov.domain.models.User
import com.sddrozdov.domain.repository.AuthRepository
import kotlin.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthRepository {

    private fun FirebaseUser.toDomainUser(): User = User(
        uid = this.uid,
        email = this.email ?: "",
        isEmailVerified = this.isEmailVerified,
        isAnonymous = this.isAnonymous,
        displayName = this.displayName,
        phoneNumber = this.phoneNumber,
        photoUrl = this.photoUrl.toString()
    )

    override suspend fun deleteCurrentUser(): Result<Unit> = try {
        firebaseAuth.currentUser?.delete()?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUp(email: String, password: String): Result<User> = try {
        deleteCurrentUser()
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("User is null")
        Result.success(firebaseUser.toDomainUser())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signIn(email: String, password: String): Result<User> = try {
        deleteCurrentUser()
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("User is null")

        if (!user.isEmailVerified) {
            throw Exception("Email is not verified. Please verify your email before signing in.")
        }
        Result.success(user.toDomainUser())
    } catch (e: FirebaseAuthInvalidUserException) {
        Result.failure(Exception("No user found with this email."))
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        Result.failure(Exception("Invalid password. Please try again."))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sendEmailVerification(user: User): Result<Unit> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null || firebaseUser.uid != user.uid) {
                Result.failure(Exception("Current user does not match"))
            } else {
                firebaseUser.sendEmailVerification().await()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun linkEmailToGmail(email: String, password: String): Result<Unit> = try {
        val credential = EmailAuthProvider.getCredential(email, password)
        firebaseAuth.currentUser?.linkWithCredential(credential)?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signInWithGoogle(googleSignInData: GoogleSignInData): Result<User> {
        return try {
            deleteCurrentUser()
            val idToken = googleSignInData.idToken

            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()

            val firebaseUser = authResult.user ?: throw Exception("Firebase user is null")

            Result.success(firebaseUser.toDomainUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> = try {
        firebaseAuth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sendEmailForgotPassword(email: String): Result<Unit> = try {
        firebaseAuth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: FirebaseAuthException) {
        Result.failure(e)
    }

    override suspend fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomainUser()
    }

    override suspend fun signInAnonymously(): Result<Unit> = try {
        firebaseAuth.signInAnonymously().await()
        Result.success(Unit)
    } catch (e: FirebaseAuthException) {
        Result.failure(e)
    }

    override suspend fun isUserAnonymous(): Boolean =
        firebaseAuth.currentUser?.isAnonymous ?: false
}