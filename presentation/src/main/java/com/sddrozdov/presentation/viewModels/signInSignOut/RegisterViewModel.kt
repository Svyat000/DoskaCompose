package com.sddrozdov.presentation.viewModels.signInSignOut

import android.content.Context
import androidx.annotation.StringRes
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.sddrozdov.domain.models.GoogleSignInData
import com.sddrozdov.domain.useCase.AuthUseCase
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.states.signInSignOut.AuthType
import com.sddrozdov.presentation.states.signInSignOut.RegisterScreenEvent
import com.sddrozdov.presentation.states.signInSignOut.RegisterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val REGISTER_STATE = "register_state"

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val state: StateFlow<RegisterScreenState> = savedStateHandle.getStateFlow(
        key = REGISTER_STATE,
        initialValue = RegisterScreenState()
    )

    private val _snackbarMessage = MutableStateFlow<Int?>(null)
    val snackbarMessage: StateFlow<Int?> = _snackbarMessage

    private val credentialManager by lazy { CredentialManager.create(context) }

    fun onEvent(event: RegisterScreenEvent) {
        when (event) {
            is RegisterScreenEvent.EmailUpdated -> {
                savedStateHandle[REGISTER_STATE] = state.value.copy(email = event.newEmail)
            }

            is RegisterScreenEvent.PasswordUpdated -> {
                savedStateHandle[REGISTER_STATE] = state.value.copy(password = event.newPassword)
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
            savedStateHandle[REGISTER_STATE] = state.value.copy(authType = AuthType.EMAIL)
            val result = authUseCase.signUp(state.value.email, state.value.password)
            savedStateHandle[REGISTER_STATE] = state.value.copy(registerResult = result)
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
                savedStateHandle[REGISTER_STATE] = state.value.copy(
                    registerResult = Result.failure(e)
                )
            }
        }
    }

    private fun handleGoogleCredential(credential: Credential) {
        viewModelScope.launch {
            val googleSignInData = mapCredentialToGoogleSignInData(credential)

            if (googleSignInData != null) {
                val result = authUseCase.signInWithGoogle(googleSignInData)
                savedStateHandle[REGISTER_STATE] = state.value.copy(registerResult = result)
            } else {
                savedStateHandle[REGISTER_STATE] = state.value.copy(
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