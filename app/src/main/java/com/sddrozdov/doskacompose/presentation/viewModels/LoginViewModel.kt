package com.sddrozdov.doskacompose.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.doskacompose.domain.useCase.AuthUseCase
import com.sddrozdov.doskacompose.presentation.states.LoginScreenEvent
import com.sddrozdov.doskacompose.presentation.states.LoginScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state: StateFlow<LoginScreenState> = _state

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.EmailUpdated -> {
                _state.value = _state.value.copy(email = event.newEmail)
            }
            is LoginScreenEvent.PasswordUpdated -> {
                _state.value = _state.value.copy(password = event.newPassword)
            }
            LoginScreenEvent.LoginBtnClicked -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            val result = authUseCase.signIn(_state.value.email, _state.value.password)
            _state.value = _state.value.copy(loginResult = result)
        }
    }
}