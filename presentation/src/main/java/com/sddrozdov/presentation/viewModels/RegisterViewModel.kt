package com.sddrozdov.doskacompose.presentation.viewModels

import android.content.Context
import androidx.annotation.StringRes
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.sddrozdov.doskacompose.R
import com.sddrozdov.doskacompose.domain.useCase.AuthUseCase
import com.sddrozdov.doskacompose.presentation.states.AuthType
import com.sddrozdov.doskacompose.presentation.states.RegisterScreenEvent
import com.sddrozdov.doskacompose.presentation.states.RegisterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterScreenState())
    val state: StateFlow<RegisterScreenState> = _state

    private val _snackbarMessage = MutableStateFlow<Int?>(null)
    val snackbarMessage: StateFlow<Int?> = _snackbarMessage

    private val credentialManager by lazy { CredentialManager.create(context) }

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

            RegisterScreenEvent.RegisterGoogleBtnClicked -> startGoogleSignUp()
        }
    }

    private fun showMessage(@StringRes messageRes: Int) {
        _snackbarMessage.value = messageRes
    }

    fun messageShown() {
        _snackbarMessage.value = null
    }

    private fun register() {
        viewModelScope.launch {
            _state.value = _state.value.copy(authType = AuthType.EMAIL)
            val result = authUseCase.signUp(_state.value.email, _state.value.password)
            _state.value = _state.value.copy(registerResult = result)
            result.onSuccess { user ->
                authUseCase.sendVerificationEmail(user)
            }
            result.onFailure {

            }
        }
    }

    private fun startGoogleSignUp() {
        viewModelScope.launch {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val credentialResponse = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleGoogleCredential(credentialResponse.credential)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    registerResult = Result.failure(e)
                )
            }
        }
    }

    private fun handleGoogleCredential(credential: Credential) {
        viewModelScope.launch {
            val result = authUseCase.signInWithGoogle(credential)
            _state.value = _state.value.copy(registerResult = result)
        }
    }
}