package com.sddrozdov.presentation.states

import android.os.Parcelable
import com.sddrozdov.domain.models.Ad
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val uid: String?= null,
    val adKey: String? = null,
    val ads: List<Ad> = emptyList(),
    val favoriteAds: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null

) : Parcelable

sealed class MainScreenEvent {
    object LoadAds : MainScreenEvent()
    data class ShowError(val message: String) : MainScreenEvent()
    data class AddFavoriteAd(val key: String): MainScreenEvent()
    object LoadUsersFavoriteAds: MainScreenEvent()
    data class IncrementViewCounter(val adKey: String) : MainScreenEvent()


}