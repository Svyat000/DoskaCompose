package com.sddrozdov.doskacompose.presentation.states.createAd

import com.sddrozdov.doskacompose.domain.models.Country


data class CountryCityState(
    val countries: List<Country> = emptyList(),
    val selectedCountry: Country? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CountryCityEvent {
    object LoadData : CountryCityEvent()
    data class SelectCountry(val countryName: String) : CountryCityEvent()
}