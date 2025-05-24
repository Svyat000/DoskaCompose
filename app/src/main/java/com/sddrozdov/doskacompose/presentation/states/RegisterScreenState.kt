package com.sddrozdov.doskacompose.presentation.states

import com.google.firebase.auth.FirebaseUser
import kotlin.Result

data class RegisterScreenState(
    val email: String = "",
    val password: String = "",
    var registerResult: Result<FirebaseUser>? = null
)

sealed class RegisterScreenEvent {
    data class EmailUpdated(val newEmail: String) : RegisterScreenEvent()
    data class PasswordUpdated(val newPassword: String) : RegisterScreenEvent()
    data object RegisterBtnClicked : RegisterScreenEvent()
    data object RegisterGoogleBtnClicked: RegisterScreenEvent()
}