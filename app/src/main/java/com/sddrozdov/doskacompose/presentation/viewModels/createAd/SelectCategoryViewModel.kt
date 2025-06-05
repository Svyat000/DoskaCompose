package com.sddrozdov.doskacompose.presentation.viewModels.createAd

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sddrozdov.doskacompose.R
import com.sddrozdov.doskacompose.domain.models.Category
import com.sddrozdov.doskacompose.presentation.states.createAd.SelectCategoryEvent
import com.sddrozdov.doskacompose.presentation.states.createAd.SelectCategoryState
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
class SelectCategoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SelectCategoryState())
    val state: StateFlow<SelectCategoryState> = _state.asStateFlow()

    private val gson = Gson()

    init {
        loadCategories()
    }

    fun onEvent(event: SelectCategoryEvent) {
        when (event) {
            is SelectCategoryEvent.OnCategorySelected -> {
                _state.value = _state.value.copy(selectedCategoryId = event.categoryId)
            }
        }
    }

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