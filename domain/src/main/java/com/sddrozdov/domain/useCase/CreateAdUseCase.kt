package com.sddrozdov.domain.useCase

import com.sddrozdov.domain.models.Ad
import com.sddrozdov.domain.models.UploadResult
import com.sddrozdov.domain.repository.AdRepository
import javax.inject.Inject

interface CreateAdUseCaseInterface {
    suspend fun createAd(ad: Ad): Result<Unit>
    suspend fun deleteAd(adKey: String): Result<Unit>
    suspend fun updateAd(ad: Ad): Result<Unit>
    suspend fun readUserAdFromDb(): Result<List<Ad>>
    suspend fun readAllAdFromDb(): Result<List<Ad>>
    suspend fun getAdByKey(key: String): Result<Ad>
    suspend fun toggleFavoriteAd(key: String, uid: String): Result<Unit>
    suspend fun loadFavAdsForUser(uid: String): Result<List<Ad>>
    suspend fun uploadPhotos(listPhotos: List<String>): Result<List<UploadResult>>
    suspend fun incrementViewCount(adKey: String, uid: String): Result<Unit>
}
class CreateAdUseCase @Inject constructor(private val repository: AdRepository): CreateAdUseCaseInterface {
    override suspend fun createAd(ad: Ad): Result<Unit> {
        return repository.createAd(ad)
    }

    override suspend fun deleteAd(adKey: String): Result<Unit> {
        return repository.deleteAd(adKey)
    }

    override suspend fun updateAd(ad: Ad): Result<Unit> {
        return repository.updateAd(ad)
    }

    override suspend fun readUserAdFromDb(): Result<List<Ad>> {
        return repository.readUserAdFromDB()
    }

    override suspend fun readAllAdFromDb(): Result<List<Ad>> {
        return repository.readAllAdFromDB()
    }

    override suspend fun getAdByKey(key: String): Result<Ad> {
        return repository.getAdByKey(key)
    }

    override suspend fun toggleFavoriteAd(key: String, uid: String): Result<Unit> {
        return repository.toggleFavoriteAd(key, uid)
    }

    override suspend fun loadFavAdsForUser(uid: String) : Result<List<Ad>>{
        return repository.loadFavAdsForUser(uid)
    }

    override suspend fun uploadPhotos(listPhotos: List<String>): Result<List<UploadResult>> {
        return repository.uploadPhotos(listPhotos)
    }

    override suspend fun incrementViewCount(adKey: String, uid: String): Result<Unit>{
        return repository.incrementViewCount(adKey,uid)
    }
}