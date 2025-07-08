package com.sddrozdov.presentation.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.domain.useCase.CreateAdUseCase
import com.sddrozdov.presentation.states.DescriptionAdScreenEvent
import com.sddrozdov.presentation.states.DescriptionAdScreenState
import com.sddrozdov.presentation.states.DescriptionAdScreenUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DESCRIPTION_AD_STATE = "description_ad"
const val AD_KEY_ARG = "adKey"

@HiltViewModel
class DescriptionAdViewModel @Inject constructor(
    private val createAdUseCase: CreateAdUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val _state = MutableStateFlow(DescriptionAdScreenState())
    val state: StateFlow<DescriptionAdScreenState> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<DescriptionAdScreenUiEvent>()
    val uiEvent: SharedFlow<DescriptionAdScreenUiEvent> = _uiEvent.asSharedFlow()


    private val adKey: String? = savedStateHandle.get<String>(AD_KEY_ARG)


    init {
        adKey?.let {
            loadAd(it)
        } ?: run {
            _state.value = _state.value.copy(error = "Ключ объявления не передан")
        }
    }

    private fun loadAd(key: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            Log.d("DescriptionAdViewModel", "Loading ad with key: $key")
            try {
                val result = createAdUseCase.getAdByKey(key)
                if (result.isSuccess) {
                    val ad = result.getOrNull()
                    if (ad != null) {
                        _state.value = _state.value.copy(ad = ad, isLoading = false)
                    } else {
                        _state.value = _state.value.copy(
                            error = "Объявление не найдено",
                            isLoading = false
                        )
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    _state.value = _state.value.copy(
                        error = exception?.localizedMessage ?: "Неизвестная ошибка",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.localizedMessage ?: "Ошибка при загрузке объявления",
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: DescriptionAdScreenEvent) {
        when (event) {
            is DescriptionAdScreenEvent.LoadAdByKey -> {
                loadAd(event.adKey)
            }
            DescriptionAdScreenEvent.CallOnThePhone -> initiatePhoneCall()
            DescriptionAdScreenEvent.SendEmail -> initiateEmailSending()
        }
    }


    private fun initiatePhoneCall() {
        viewModelScope.launch {
            val phoneNumber = _state.value.ad?.phone
            _uiEvent.emit(DescriptionAdScreenUiEvent.InitiatePhoneCall(phoneNumber))
        }
    }

    private fun initiateEmailSending() {
        viewModelScope.launch {
            val email = _state.value.ad?.email
            val title = _state.value.ad?.title
            if (!email.isNullOrEmpty()) {
                _uiEvent.emit(
                    DescriptionAdScreenUiEvent.InitiateEmail(
                        email = email,
                        subject = title ?: "",
                        body = "Здравствуйте, я заинтересован в вашем объявлении."
                    )
                )
            } else {
                _uiEvent.emit(DescriptionAdScreenUiEvent.ShowToast("Email не указан"))
            }
        }
    }


}




