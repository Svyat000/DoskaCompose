package com.sddrozdov.doskacompose.domain.useCase

import android.util.Log
import com.sddrozdov.doskacompose.domain.models.Ad
import com.sddrozdov.doskacompose.domain.repository.AdRepository
import javax.inject.Inject

class AdUseCase @Inject constructor(private val adRepository: AdRepository) {

    suspend fun createAd(ad: Ad): Result<Unit> {

        if (ad.title.isNullOrBlank()) {
            return Result.failure(Exception("Title is required"))
        }
        if (ad.price.isNullOrBlank()) {
            return Result.failure(Exception("Price is required"))
        }

        if (!isValidPrice(ad.price)) {
            return Result.failure(Exception("Invalid price format"))
        }

//        if (ad.mainImage == "empty" && ad.image2 == "empty" && ad.image3 == "empty") {
//            return Result.failure(Exception("At least one image is required"))
//        }
        Log.e("CreateAdUseCase", "CreateAdUseCase")
        return adRepository.createAd(ad)

    }

    private fun isValidPrice(price: String?): Boolean {
        return try {
            price?.toDouble() ?: 0.0 > 0
        } catch (e: NumberFormatException) {
            false
        }
    }
}