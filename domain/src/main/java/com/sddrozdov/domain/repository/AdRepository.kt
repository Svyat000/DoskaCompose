package com.sddrozdov.doskacompose.domain.repository

import com.sddrozdov.doskacompose.domain.models.Ad

interface AdRepository {
    suspend fun createAd(ad: Ad): Result<Unit>
    suspend fun deleteAd(ad: Ad): Result<Unit>
    suspend fun updateAd(ad: Ad): Result<Unit>

}