package com.sddrozdov.presentation.states

import android.os.Parcelable
import com.sddrozdov.domain.models.Ad
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val adKey: String? = null,
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null

) : Parcelable

sealed class MainScreenEvent {
    //data class OpenDescriptionAd(val adKey: String) : MainScreenEvent()
    object LoadAds : MainScreenEvent()
    data class ShowError(val message: String) : MainScreenEvent()


}