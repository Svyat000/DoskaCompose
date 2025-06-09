package com.sddrozdov.presentation.states.createAd

import android.net.Uri
import com.sddrozdov.doskacompose.domain.models.Category

data class CreateAdStates(

    val categories: List<Category> = emptyList(),
    val error: String? = null,
    val selectedCategoryId: Long? = null,

    val title: String = "",
    val description: String = "",
    val price: String = "",
    val email: String = "",
    val phone: String = "",
    val index: String = "",
    val country: String? = null,
    val city: String? = null,
    val category: String? = null,
    val images: List<Uri> = emptyList(),
    val isLoading: Boolean = false,
    val isPublished: Boolean = false,
    val errorMessage: String? = null
)

sealed class CreateAdEvents {

    data class OnCategorySelected(val categoryId: Long) : CreateAdEvents()


    data class OnTitleChanged(val newTitle: String) : CreateAdEvents()
    data class OnDescriptionChanged(val newDescription: String) : CreateAdEvents()
    data class OnPriceChanged(val newPrice: String) : CreateAdEvents()
    data class OnEmailChanged(val newEmail: String) : CreateAdEvents()
    data class OnPhoneChanged(val newPhone: String) : CreateAdEvents()
    data class OnIndexChanged(val newIndex: String) : CreateAdEvents()
    data class OnCountrySelected(val newCountry: String) : CreateAdEvents()
    data class OnCitySelected(val newCity: String) : CreateAdEvents()

    object OpenImagePicker : CreateAdEvents()
    data class ImagesSelected(val uris: List<Uri>) : CreateAdEvents()

    object OnImagesAdded : CreateAdEvents()

    object OnClearError : CreateAdEvents()
    data object OnPublishClicked : CreateAdEvents()
}