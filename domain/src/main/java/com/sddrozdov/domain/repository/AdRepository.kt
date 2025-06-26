package com.sddrozdov.domain.repository

import com.sddrozdov.domain.models.Ad

interface AdRepository {
    suspend fun createAd(ad: Ad): Result<Unit>
    suspend fun deleteAd(ad: Ad): Result<Unit>
    suspend fun updateAd(ad: Ad): Result<Unit>
    suspend fun readUserAdFromDB(): Result<List<Ad>>
    suspend fun readAllAdFromDB(): Result<List<Ad>>

}