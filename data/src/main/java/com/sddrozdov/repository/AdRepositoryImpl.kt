package com.sddrozdov.repository

import android.util.Log
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
            val adKey = databaseReference.child(ADS).push().key
                ?: return@withContext Result.failure(IllegalStateException("Failed to generate ad key"))

            val adWithKey =
                ad.copy(key = adKey, uid = userId, time = System.currentTimeMillis().toString())

            // Записываем объявление в /ads/{adKey}
            databaseReference.child(ADS).child(adKey)
                .setValue(adWithKey)
                .await()

            // Записываем ссылку в /users/{userId}/ads/{adKey} = true
            databaseReference.child(USERS).child(userId).child(ADS).child(adKey)
                .setValue(true)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteAd(adKey: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val userId = firebaseAuth.uid
                ?: return@withContext Result.failure(IllegalStateException("User not authenticated"))
            if(adKey == null){
                return@withContext Result.failure(IllegalStateException("Ad key is null"))
            }


            // Удаляем объявление из /ads/{adKey}
            databaseReference.child(ADS).child(adKey).removeValue().await()

            // Удаляем ссылку на объявление из /users/{userId}/ads/{adKey}
            databaseReference.child(USERS).child(userId).child(ADS).child(adKey).removeValue().await()

            // Получаем список пользователей у которых это объявление в избранном
            val favoritesSnapshot = databaseReference.child(ADS).child(adKey).child(FAVORITES)
                .get()
                .await()

            // Удаляем ссылки на избранное у всех пользователей
            for (favoriteSnapshot in favoritesSnapshot.children) {
                val favUserId = favoriteSnapshot.key ?: continue
                databaseReference.child(USERS).child(favUserId).child(USERS_FAVORITE_ADS).child(adKey)
                    .removeValue()
                    .await()
            }

            // Удаляем узел favorites из объявления
            databaseReference.child(ADS).child(adKey).child(FAVORITES).removeValue().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun updateAd(ad: Ad): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun readUserAdFromDB(): Result<List<Ad>> = withContext(Dispatchers.IO) {
        try {
            val userId = firebaseAuth.uid
                ?: return@withContext Result.failure(IllegalStateException("User not authenticated"))

            // Получаем список ключей объявлений пользователя
            val keysSnapshot = databaseReference.child(USERS).child(userId).child(ADS)
                .get()
                .await()

            val ads = mutableListOf<Ad>()

            // Для каждого ключа получаем объявление из /ads/{adKey}
            for (keySnapshot in keysSnapshot.children) {
                val adKey = keySnapshot.key ?: continue

                val adSnapshot = databaseReference.child(ADS).child(adKey)
                    .get()
                    .await()

                val ad = adSnapshot.getValue(Ad::class.java)
                ad?.let { ads.add(it) }
            }
            Log.d("TAG", "data layer readAdsFromDb OK!")
            Result.success(ads)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readAllAdFromDB(): Result<List<Ad>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = databaseReference.child(ADS).limitToLast(5).get().await()
            val ads = mutableListOf<Ad>()
            for (child in snapshot.children) {
                val ad = child.getValue(Ad::class.java)
                ad?.let { ads.add(it) }
            }
            Result.success(ads)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAdByKey(key: String): Result<Ad> = withContext(Dispatchers.IO) {
        try {
            val snapshot = databaseReference.child(ADS).child(key).get().await()
            val ad = snapshot.getValue(Ad::class.java) ?: return@withContext Result.failure(
                NullPointerException("Ad not found")
            )
            Result.success(ad)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavoriteAd(key: String, uid: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val adFavoritesRef =
                    databaseReference.child(ADS).child(key).child(FAVORITES).child(uid)
                val userFavoritesRef =
                    databaseReference.child(USERS).child(uid).child(USERS_FAVORITE_ADS).child(key)

                val current = adFavoritesRef.get().await().getValue(Boolean::class.java) ?: false
                val newValue = !current
                adFavoritesRef.setValue(newValue).await()

                if (newValue) {
                    // Если добавляем в избранное создаем запись у пользователя
                    userFavoritesRef.setValue(true).await()
                } else {
                    // Если удаляем из избранного удаляем запись у пользователя
                    userFavoritesRef.removeValue().await()
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun loadFavAdsForUser(uid: String): Result<List<Ad>> =
        withContext(Dispatchers.IO) {
            try {
                val userFavoritesSnapshot = databaseReference
                    .child(USERS)
                    .child(uid)
                    .child(USERS_FAVORITE_ADS)
                    .get()
                    .await()

                val favoriteKeys = userFavoritesSnapshot.children.mapNotNull { it.key }.toSet()

                val adsSnapshot = databaseReference.child(ADS).get().await()

                val favoriteAds = mutableListOf<Ad>()

                for (adSnap in adsSnapshot.children) {
                    val adKey = adSnap.key ?: continue

                    // Проверяем, есть ли объявление в избранных по ключу
                    if (favoriteKeys.contains(adKey)) {
                        val ad = adSnap.getValue(Ad::class.java)
                        ad?.let {
                            favoriteAds.add(it)
                        }
                    }
                }
                Result.success(favoriteAds)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    companion object NodesDb {
        const val ADS = "ads"
        const val USERS_FAVORITE_ADS = "favorite_ads"
        const val USERS = "users"
        const val FAVORITES = "favorites"
    }
}

