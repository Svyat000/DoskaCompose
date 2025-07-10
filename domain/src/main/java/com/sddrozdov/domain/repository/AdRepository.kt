package com.sddrozdov.domain.repository

import com.sddrozdov.domain.models.Ad
import com.sddrozdov.domain.models.UploadResult

interface AdRepository {
    suspend fun createAd(ad: Ad): Result<Unit>
    suspend fun deleteAd(adKey: String): Result<Unit>
    suspend fun updateAd(ad: Ad): Result<Unit>
    suspend fun readUserAdFromDB(): Result<List<Ad>>
    suspend fun readAllAdFromDB(): Result<List<Ad>>
    suspend fun getAdByKey(key: String): Result<Ad>
    suspend fun toggleFavoriteAd(key: String, uid: String): Result<Unit>
    suspend fun loadFavAdsForUser(uid: String): Result<List<Ad>>
    suspend fun uploadPhotos(uris: List<String>) : Result<List<UploadResult>>

}