package com.sddrozdov.presentation.states.createAd

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateAdStates(

    val isAuthorized: Pair<Boolean, String>? = Pair(false, ""),

    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Long? = null,

    val countries: List<Country> = emptyList(),
    val selectedCountry: Country? = null,
    val selectedCity: String? = null,
    val isCountrySelectionVisible: Boolean = true,
    val isCitySelectionVisible: Boolean = false,
    val countryAndCitySelected: Boolean = false,

    val title: String = "",


    val error: String? = null,


    val description: String = "",
    val price: String = "",
    val email: String = "",
    val phone: String = "",
    val index: String = "",


    val images: List<Uri> = emptyList(),

    val isLoading: Boolean = false,
    val isPublished: Boolean = false,

    ) : Parcelable

@Parcelize
data class Category(
    val id: Int,
    val name: String,
    val iconRes: Int? = null
) : Parcelable

@Parcelize
data class Country(
    val name: String,
    val cities: List<String>
) : Parcelable

sealed class CreateAdEvents {

    data class IsAuthorized(val isAuthorized: Boolean, val message: String) : CreateAdEvents()

    data class OnCategorySelected(val categoryId: Long) : CreateAdEvents()

    object ShowCountrySelection : CreateAdEvents()
    data class OnCountrySelected(val newCountry: Country) : CreateAdEvents()
    object ShowCitySelection : CreateAdEvents()
    data class OnCitySelected(val newCity: String) : CreateAdEvents()
    data class OnCountryAndCitySelected(
        val countryAndCitySelected: Boolean,
        val country: Country,
        val city: String
    ) : CreateAdEvents()

    data class OnTitleChanged(val newTitle: String) : CreateAdEvents()


    data class OnDescriptionChanged(val newDescription: String) : CreateAdEvents()
    data class OnPriceChanged(val newPrice: String) : CreateAdEvents()
    data class OnEmailChanged(val newEmail: String) : CreateAdEvents()
    data class OnPhoneChanged(val newPhone: String) : CreateAdEvents()
    data class OnIndexChanged(val newIndex: String) : CreateAdEvents()


    object OpenImagePicker : CreateAdEvents()
    data class ImagesSelected(val uris: List<Uri>) : CreateAdEvents()

    object OnImagesAdded : CreateAdEvents()

    object OnClearError : CreateAdEvents()
    data object OnPublishClicked : CreateAdEvents()
}