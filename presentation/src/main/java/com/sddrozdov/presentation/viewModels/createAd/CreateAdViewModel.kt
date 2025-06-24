package com.sddrozdov.presentation.viewModels.createAd

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.states.createAd.Category
import com.sddrozdov.presentation.states.createAd.Country
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

const val CREATE_AD_STATE = "create_ad_state"

@HiltViewModel
class CreateAdViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val state =
        savedStateHandle.getStateFlow(key = CREATE_AD_STATE, initialValue = CreateAdStates())

    init {
        loadCategories()
        loadCountries()
    }


    fun onEvent(event: CreateAdEvents) {
        when (event) {
            is CreateAdEvents.IsAuthorized -> TODO()

            is CreateAdEvents.OnCategorySelected -> {
                savedStateHandle[CREATE_AD_STATE] =
                    state.value.copy(selectedCategoryId = event.categoryId)
            }

            CreateAdEvents.ShowCountrySelection -> {
                showCountrySelection()
            }

            is CreateAdEvents.OnCountrySelected -> {
                selectCountry(event.newCountry)
            }

            CreateAdEvents.ShowCitySelection -> {
                showCitySelection()
            }

            is CreateAdEvents.OnCitySelected -> {
                selectCity(event.newCity)
            }

            is CreateAdEvents.OnCountryAndCitySelected ->
                savedStateHandle[CREATE_AD_STATE] = state.value.copy(
                    selectedCountry = event.country,
                    selectedCity = event.city,
                    countryAndCitySelected = true
                )


            is CreateAdEvents.OnTitleChanged -> {
                savedStateHandle[CREATE_AD_STATE] =
                    state.value.copy(title = event.newTitle)
            }

            is CreateAdEvents.OnDescriptionChanged -> {
                savedStateHandle[CREATE_AD_STATE] =
                    state.value.copy(description = event.newDescription)
            }

            is CreateAdEvents.OnEmailChanged -> {
                savedStateHandle[CREATE_AD_STATE] = state.value.copy(email = event.newEmail)

            }


            is CreateAdEvents.ImagesSelected -> TODO()

            CreateAdEvents.OnClearError -> TODO()


            CreateAdEvents.OnImagesAdded -> TODO()
            is CreateAdEvents.OnIndexChanged -> TODO()
            is CreateAdEvents.OnPhoneChanged -> TODO()
            is CreateAdEvents.OnPriceChanged -> TODO()
            CreateAdEvents.OnPublishClicked -> TODO()

            CreateAdEvents.OpenImagePicker -> TODO()

        }
    }


    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = loadCategoriesFromRaw()
                savedStateHandle[CREATE_AD_STATE] = state.value.copy(
                    categories = categories,
                    isLoading = false
                )
            } catch (e: Exception) {
                savedStateHandle[CREATE_AD_STATE] = state.value.copy(
                    error = "Failed to load categories",
                    isLoading = false
                )
            }
        }
    }


    private fun loadCategoriesFromRaw(): List<Category> {
        val inputStream = context.resources.openRawResource(R.raw.categories)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(jsonString, type) ?: emptyList()
    }

    private fun loadCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val countries = loadCountriesFromRaw()
                savedStateHandle[CREATE_AD_STATE] = state.value.copy(
                    countries = countries,
                    isLoading = false
                )

            } catch (e: Exception) {
                savedStateHandle[CREATE_AD_STATE] = state.value.copy(
                    error = "Failed to load countries",
                    isLoading = false
                )
            }
        }
    }


    private fun loadCountriesFromRaw(): List<Country> {
        val inputStream = context.resources.openRawResource(R.raw.countries)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Country>>() {}.type
        return gson.fromJson<List<Country>>(jsonString, type) ?: emptyList()
    }

    private fun selectCountry(country: Country) {
        savedStateHandle[CREATE_AD_STATE] = state.value.copy(
            selectedCountry = country,
            isCountrySelectionVisible = false,
            isCitySelectionVisible = true,
            selectedCity = null
        )
    }

    private fun selectCity(city: String) {
        savedStateHandle[CREATE_AD_STATE] = state.value.copy(
            selectedCity = city,
            isCitySelectionVisible = false
        )
    }

    private fun showCountrySelection() {
        savedStateHandle[CREATE_AD_STATE] = state.value.copy(
            isCountrySelectionVisible = true,
            isCitySelectionVisible = false
        )
    }

    private fun showCitySelection() {
        savedStateHandle[CREATE_AD_STATE] = state.value.copy(
            isCountrySelectionVisible = false,
            isCitySelectionVisible = true
        )
    }
}
