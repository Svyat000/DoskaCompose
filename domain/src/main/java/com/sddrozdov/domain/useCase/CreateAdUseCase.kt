package com.sddrozdov.domain.useCase

import com.sddrozdov.domain.models.Ad
import com.sddrozdov.domain.repository.AdRepository
import javax.inject.Inject

class CreateAdUseCase @Inject constructor(private val repository: AdRepository) {
    suspend fun createAd(ad: Ad): Result<Unit> {
        return repository.createAd(ad)
    }

    suspend fun deleteAd(ad: Ad): Result<Unit> {
        return repository.createAd(ad)
    }

    suspend fun updateAd(ad: Ad): Result<Unit> {
        return repository.createAd(ad)
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

}