package com.sddrozdov.presentation.states

import android.os.Parcelable
import com.sddrozdov.domain.models.Ad
import kotlinx.parcelize.Parcelize

@Parcelize
data class DescriptionAdScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val ad: Ad? = null
) : Parcelable

sealed class DescriptionAdScreenEvent {
    data class LoadAdByKey(val adKey: String) : DescriptionAdScreenEvent()
    data class OpenEdit(val ad: Ad) : DescriptionAdScreenEvent()
    data class DeleteAd(val ad: Ad) : DescriptionAdScreenEvent()
}