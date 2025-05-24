package com.sddrozdov.doskacompose.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.doskacompose.domain.useCase.AuthUseCase
import com.sddrozdov.doskacompose.presentation.states.RegisterScreenEvent
import com.sddrozdov.doskacompose.presentation.states.RegisterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _state = MutableStateFlow(RegisterScreenState())
    val state: StateFlow<RegisterScreenState> = _state

    fun onEvent(event: RegisterScreenEvent) {
        when (event) {
            is RegisterScreenEvent.EmailUpdated -> {
                _state.value = _state.value.copy(email = event.newEmail)
            }

            is RegisterScreenEvent.PasswordUpdated -> {
                _state.value = _state.value.copy(password = event.newPassword)
            }

            RegisterScreenEvent.RegisterBtnClicked -> {
                register()
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            val result = authUseCase.signUp(_state.value.email, _state.value.password)
            _state.value = _state.value.copy(registerResult = result)
            result.onSuccess { user ->
                authUseCase.sendVerificationEmail(user)
            }
            result.onFailure { exeption ->

            }
        }
    }
}