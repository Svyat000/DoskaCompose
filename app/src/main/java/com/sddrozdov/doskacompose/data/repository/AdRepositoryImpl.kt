package com.sddrozdov.doskacompose.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.sddrozdov.doskacompose.databaseNodes.DbNodes
import com.sddrozdov.doskacompose.domain.models.Ad
import com.sddrozdov.doskacompose.domain.repository.AdRepository
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
            val uid = firebaseAuth.currentUser?.uid
                ?: return@withContext Result.failure(Exception("User not authenticated"))

            val adKey = databaseReference.child(DbNodes.AD).push().key
                ?: return@withContext Result.failure(Exception("Failed to generate ad key"))

            val adWithData = ad.copy(
                key = adKey,
                uid = uid,
                time = System.currentTimeMillis().toString(),
                viewsCounter = "0",
                emailsCounter = "0",
                callsCounter = "0",
                favoriteCounter = "0"
            )

            val updates = hashMapOf<String, Any>(
                "${DbNodes.AD}/$adKey" to adWithData,
                // "${DbNodes.USERS}/$uid/${DbNodes.USER_ADS}/$adKey" to true
            )
            databaseReference.updateChildren(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    private suspend fun DatabaseReference.setValue(value: Any?) = suspendCoroutine<Unit> { cont ->
//        setValue(value).addOnCompleteListener { task ->
//            if (task.isSuccessful) cont.resume(Unit)
//            else cont.resumeWithException(task.exception ?: Exception("Firebase error"))
//        }
//    }

    override suspend fun deleteAd(ad: Ad): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAd(ad: Ad): Result<Unit> {
        TODO("Not yet implemented")
    }

}