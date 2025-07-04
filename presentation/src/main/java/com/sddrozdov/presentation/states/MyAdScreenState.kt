package com.sddrozdov.presentation.states

import android.os.Parcelable
import com.sddrozdov.domain.models.Ad
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyAdScreenState(
    val adKey: String? = null,
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : Parcelable

sealed class MyAdScreenEvent {
    object LoadMyAds : MyAdScreenEvent()
    data class ShowError(val message: String) : MyAdScreenEvent()
    data class DeleteAd(val adKey: String) : MyAdScreenEvent()


}