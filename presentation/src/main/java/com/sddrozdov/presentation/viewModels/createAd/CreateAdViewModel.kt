package com.sddrozdov.presentation.viewModels.createAd

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sddrozdov.doskacompose.domain.models.Category
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAdViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(CreateAdStates())
    val state: StateFlow<CreateAdStates> = _state.asStateFlow()

    private var countryCityMap: Map<String, List<String>> = emptyMap()

    private val gson = Gson()

    init {
        loadCategories()
    }





    fun onEvent(event: CreateAdEvents) {
        when (event) {
            is CreateAdEvents.ImagesSelected -> TODO()
            is CreateAdEvents.OnCitySelected -> TODO()
            CreateAdEvents.OnClearError -> TODO()
            is CreateAdEvents.OnCountrySelected -> TODO()
            is CreateAdEvents.OnDescriptionChanged -> TODO()
            is CreateAdEvents.OnEmailChanged -> TODO()
            CreateAdEvents.OnImagesAdded -> TODO()
            is CreateAdEvents.OnIndexChanged -> TODO()
            is CreateAdEvents.OnPhoneChanged -> TODO()
            is CreateAdEvents.OnPriceChanged -> TODO()
            CreateAdEvents.OnPublishClicked -> TODO()
            is CreateAdEvents.OnTitleChanged -> TODO()
            CreateAdEvents.OpenImagePicker -> TODO()
            is CreateAdEvents.OnCategorySelected -> {
                _state.value = _state.value.copy(selectedCategoryId = event.categoryId)
            }
        }
    }

//    fun onEventCountryCity(event: CountryCityEvent) {
//        when (event) {
//            is CountryCityEvent.LoadData -> loadData()
//            is CountryCityEvent.SelectCountry -> selectCountry(event.countryName)
//        }
//    }

//    private fun loadData() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                countryCityMap = loadCountryCityMap(context, "countries_cities.json")
//                val countries = mapToCountryList(countryCityMap)
//                _state.value = CountryCityState(countries = countries, isLoading = false)
//            } catch (e: Exception) {
//                _state.value = CountryCityState(error = e.message, isLoading = false)
//            }
//        }
//    }

//    fun mapToCountryList(map: Map<String, List<String>>): List<Country> =
//        map.map { Country(it.key, it.value) }
//
//    private fun selectCountry(countryName: String) {
//        val country = _state.value.countries.find { it.name == countryName }
//        _state.value = _state.value.copy(selectedCountry = country)
//    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val categories = loadCategoriesFromRaw()
            _state.update { currentState ->
                currentState.copy(categories = categories)
            }
        }
    }

    private fun loadCategoriesFromRaw(): List<Category> {
        val inputStream = context.resources.openRawResource(R.raw.categories)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(jsonString, type) ?: emptyList()
    }
}