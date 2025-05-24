package com.sddrozdov.doskacompose.presentation.viewModels

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.sddrozdov.doskacompose.R
import com.sddrozdov.doskacompose.domain.useCase.AuthUseCase
import com.sddrozdov.doskacompose.presentation.states.LoginScreenEvent
import com.sddrozdov.doskacompose.presentation.states.LoginScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state: StateFlow<LoginScreenState> = _state

    private val credentialManager by lazy { CredentialManager.create(context) }

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

            LoginScreenEvent.LoginGoogleBtnClicked -> startGoogleSignIn()
        }
    }


    private fun login() {
        viewModelScope.launch {
            val result = authUseCase.signIn(_state.value.email, _state.value.password)
            _state.value = _state.value.copy(loginResult = result)
        }
    }

    private fun startGoogleSignIn() {
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
                    loginResult = Result.failure(e)
                )
            }
        }
    }

    private fun handleGoogleCredential(credential: Credential) {
        viewModelScope.launch {
            val result = authUseCase.signInWithGoogle(credential)
            _state.value = _state.value.copy(loginResult = result)
        }
    }


}