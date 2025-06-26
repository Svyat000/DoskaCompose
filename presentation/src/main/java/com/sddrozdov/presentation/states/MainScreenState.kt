package com.sddrozdov.presentation.states

import com.sddrozdov.domain.models.Ad

data class MainScreenState(
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null

)

sealed class MainScreenEvent {
    object LoadAds : MainScreenEvent()
    data class ShowError(val message: String) : MainScreenEvent()


}