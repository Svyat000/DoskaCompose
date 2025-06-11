package com.sddrozdov.presentation.viewModels.createAd

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sddrozdov.domain.models.Country
import com.sddrozdov.doskacompose.domain.models.Category
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAdViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : ViewModel() {

    private val _state = MutableStateFlow(CreateAdStates())
    val state: StateFlow<CreateAdStates> = _state

    init {
        loadCategories()
        loadCountries()
    }


    fun onEvent(event: CreateAdEvents) {
        when (event) {
            is CreateAdEvents.IsAuthorized -> TODO()

            is CreateAdEvents.OnCategorySelected -> {
                _state.value = _state.value.copy(selectedCategoryId = event.categoryId)
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
                _state.update { currentState ->
                    currentState.copy(
                        selectedCountry = event.country,
                        selectedCity = event.city,
                        countryAndCitySelected = true
                    )
                }


            is CreateAdEvents.ImagesSelected -> TODO()

            CreateAdEvents.OnClearError -> TODO()

            is CreateAdEvents.OnDescriptionChanged -> TODO()
            is CreateAdEvents.OnEmailChanged -> TODO()
            CreateAdEvents.OnImagesAdded -> TODO()
            is CreateAdEvents.OnIndexChanged -> TODO()
            is CreateAdEvents.OnPhoneChanged -> TODO()
            is CreateAdEvents.OnPriceChanged -> TODO()
            CreateAdEvents.OnPublishClicked -> TODO()
            is CreateAdEvents.OnTitleChanged -> TODO()
            CreateAdEvents.OpenImagePicker -> TODO()

        }
    }


    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = loadCategoriesFromRaw()
                _state.update { currentState ->
                    currentState.copy(
                        categories = categories,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        error = "Failed to load categories",
                        isLoading = false
                    )
                }
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
                _state.update { currentState ->
                    currentState.copy(
                        countries = countries,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        error = "Failed to load countries",
                        isLoading = false
                    )
                }
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
        _state.update { currentState ->
            currentState.copy(
                selectedCountry = country,
                isCountrySelectionVisible = false,
                isCitySelectionVisible = true,
                selectedCity = null
            )
        }
    }

    private fun selectCity(city: String) {
        _state.update { currentState ->
            currentState.copy(
                selectedCity = city,
                isCitySelectionVisible = false
            )
        }
    }

    private fun showCountrySelection() {
        _state.update { currentState ->
            currentState.copy(
                isCountrySelectionVisible = true,
                isCitySelectionVisible = false
            )
        }
    }

    private fun showCitySelection() {
        _state.update { currentState ->
            currentState.copy(
                isCountrySelectionVisible = false,
                isCitySelectionVisible = true
            )
        }
    }
}
