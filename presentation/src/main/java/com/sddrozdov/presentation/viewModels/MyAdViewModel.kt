package com.sddrozdov.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.domain.useCase.CreateAdUseCase
import com.sddrozdov.presentation.states.MyAdScreenEvent
import com.sddrozdov.presentation.states.MyAdScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAdViewModel @Inject constructor(
    private val createAdUseCase: CreateAdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyAdScreenState())
    val state: StateFlow<MyAdScreenState> = _state.asStateFlow()

    init {
        loadMyAds()
    }

    fun onEvent(event: MyAdScreenEvent) {
        when (event) {
            MyAdScreenEvent.LoadMyAds -> loadMyAds()
            is MyAdScreenEvent.ShowError -> _state.value = _state.value.copy(error = event.message)
            is MyAdScreenEvent.DeleteAd -> deleteAd(event.adKey)
        }
    }

    private fun deleteAd(adKey: String) {
        viewModelScope.launch {
            Log.d("MYTAG", "VIEWMODEL MY AD DELETE for adKey: $adKey")
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = createAdUseCase.deleteAd(adKey)
            result.fold(
                onSuccess = {
                    Log.d("MYTAG", "DELETE SUCCESS for adKey: $adKey")
                    loadMyAds()
                },
                onFailure = { throwable ->
                    Log.e("MYTAG", "DELETE FAILED for adKey: $adKey, error: ${throwable.message}")
                    _state.value = _state.value.copy(isLoading = false, error = throwable.message)
                }
            )
        }
    }

    private fun loadMyAds() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = createAdUseCase.readUserAdFromDb()
            result.fold(
                onSuccess = { adsList ->
                    _state.value = MyAdScreenState(isLoading = false, ads = adsList, error = null)
                },
                onFailure = { throwable ->
                    _state.value = MyAdScreenState(
                        isLoading = false,
                        ads = emptyList(),
                        error = throwable.message
                    )
                }
            )
        }
    }
}