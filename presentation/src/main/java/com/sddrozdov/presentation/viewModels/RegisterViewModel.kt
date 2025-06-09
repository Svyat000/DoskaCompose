package com.sddrozdov.presentation.viewModels

import android.app.Application
import androidx.annotation.StringRes
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.sddrozdov.domain.models.GoogleSignInData
import com.sddrozdov.domain.repository.AuthRepository
import com.sddrozdov.presentation.states.AuthType
import com.sddrozdov.presentation.states.RegisterScreenEvent
import com.sddrozdov.presentation.states.RegisterScreenState
import com.sddrozdov.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val defaultWebClientId: String,
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext

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
            val result = repository.signUp(_state.value.email, _state.value.password)
            _state.value = _state.value.copy(registerResult = result)
            result.onSuccess { user ->
                repository.sendEmailVerification(user)
            }
            result.onFailure {

            }
        }
    }

    private fun startGoogleSignUp() {
        viewModelScope.launch {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(defaultWebClientId)
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
            val googleSignInData = mapCredentialToGoogleSignInData(credential)

            if (googleSignInData != null) {
                val result = repository.signInWithGoogle(googleSignInData)
                _state.value = _state.value.copy(registerResult = result)
            } else {
                _state.value = _state.value.copy(
                    registerResult = Result.failure(Exception("Invalid Google credential"))
                )
                showMessage(R.string.google_sign_in_failed)
            }
        }
    }
    private fun mapCredentialToGoogleSignInData(credential: Credential): GoogleSignInData? {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            return GoogleSignInData(idToken = googleIdTokenCredential.idToken)
        }
        return null
    }
}