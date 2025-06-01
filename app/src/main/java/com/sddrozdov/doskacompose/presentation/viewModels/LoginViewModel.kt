package com.sddrozdov.doskacompose.presentation.viewModels

import android.content.Context
import android.util.Patterns
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
            LoginScreenEvent.ForgotPasswordBtnClicked -> forgotPass()
        }
    }

    private fun forgotPass() {
        viewModelScope.launch {
            if (_state.value.email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(_state.value.email)
                    .matches()
            ) {
                authUseCase.sendEmailForgotPassword(_state.value.email)
                _state.value = _state.value.copy(emailErrorMessage = "Письмо с восстановлением пароля успешно отправлено!")
            } else
                checkingEmailSymbols()
        }
    }

    private fun checkingEmailSymbols() {
        when {
            _state.value.email.isEmpty() -> {
                _state.value = _state.value.copy(emailErrorMessage = "The Email field must not be empty")
            }
            !Patterns.EMAIL_ADDRESS.matcher(_state.value.email).matches() -> {
                _state.value = _state.value.copy(emailErrorMessage = "Please check if your email is entered correctly")
            }
            else -> {
                _state.value = _state.value.copy(emailErrorMessage = null)
            }
        }
    }

    fun clearEmailError() {
        _state.value = _state.value.copy(emailErrorMessage = null)
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