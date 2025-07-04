package com.sddrozdov.domain.useCase

import com.sddrozdov.domain.models.Ad
import com.sddrozdov.domain.repository.AdRepository
import javax.inject.Inject

class CreateAdUseCase @Inject constructor(private val repository: AdRepository) {
    suspend fun createAd(ad: Ad): Result<Unit> {
        return repository.createAd(ad)
    }

    suspend fun deleteAd(adKey: String): Result<Unit> {
        return repository.deleteAd(adKey)
    }

    suspend fun updateAd(ad: Ad): Result<Unit> {
        return repository.updateAd(ad)
    }

    suspend fun readUserAdFromDb(): Result<List<Ad>> {
        return repository.readUserAdFromDB()
    }

    suspend fun readAllAdFromDb(): Result<List<Ad>> {
        return repository.readAllAdFromDB()
    }

    suspend fun getAdByKey(key: String): Result<Ad> {
        return repository.getAdByKey(key)
    }

    suspend fun toggleFavoriteAd(key: String, uid: String): Result<Unit> {
        return repository.toggleFavoriteAd(key, uid)
    }

    suspend fun loadFavAdsForUser(uid: String) : Result<List<Ad>>{
        return repository.loadFavAdsForUser(uid)
    }
}