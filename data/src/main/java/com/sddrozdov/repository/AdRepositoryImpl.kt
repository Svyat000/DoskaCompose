package com.sddrozdov.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.sddrozdov.domain.models.Ad
import com.sddrozdov.domain.repository.AdRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : AdRepository {

    override suspend fun createAd(ad: Ad): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val userId = firebaseAuth.uid
                ?: return@withContext Result.failure(IllegalStateException("User not authenticated"))

            // Генерируем новый ключ если его нет
            val adKey = ad.key ?: databaseReference.push().key
            ?: return@withContext Result.failure(IllegalStateException("Failed to generate ad key"))

            val adWithKey = ad.copy(key = adKey, uid = userId)

            // await для suspend корутин
            databaseReference.child(adKey).child(userId).child("AD")
                .setValue(adWithKey)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteAd(ad: Ad): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAd(ad: Ad): Result<Unit> {
        TODO("Not yet implemented")
    }

}