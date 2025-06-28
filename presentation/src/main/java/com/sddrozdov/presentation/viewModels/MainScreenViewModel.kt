package com.sddrozdov.presentation.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.domain.useCase.CreateAdUseCase
import com.sddrozdov.presentation.states.MainScreenEvent
import com.sddrozdov.presentation.states.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val AD_FROM_MAIN_TO_DESC_SCREEN = "ad_from_main_screen_to_description_screen"

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val createAdUseCase: CreateAdUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        loadAds()
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.LoadAds -> loadAds()
            is MainScreenEvent.ShowError -> _state.value = _state.value.copy(error = event.message)
        }
    }

    private fun loadAds() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = createAdUseCase.readAllAdFromDb()
            result.fold(
                onSuccess = { adsList ->
                    _state.value = MainScreenState(isLoading = false, ads = adsList, error = null)
                },
                onFailure = { throwable ->
                    _state.value = MainScreenState(
                        isLoading = false,
                        ads = emptyList(),
                        error = throwable.message
                    )
                }
            )
        }
    }
}