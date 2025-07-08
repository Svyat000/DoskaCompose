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
    object CallOnThePhone : DescriptionAdScreenEvent()
    object SendEmail : DescriptionAdScreenEvent()
}

sealed class DescriptionAdScreenUiEvent {
    data class InitiatePhoneCall(val phoneNumber: String?) : DescriptionAdScreenUiEvent()
    data class InitiateEmail(val email: String?, val subject: String, val body: String) : DescriptionAdScreenUiEvent()
    data class ShowToast(val message: String) : DescriptionAdScreenUiEvent()
}