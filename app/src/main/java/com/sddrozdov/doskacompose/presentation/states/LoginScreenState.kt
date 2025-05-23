package com.sddrozdov.doskacompose.presentation.states

import com.google.firebase.auth.FirebaseUser

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    var loginResult: Result<FirebaseUser>? = null
)

sealed class LoginScreenEvent {
    data class EmailUpdated(val newEmail: String) : LoginScreenEvent()
    data class PasswordUpdated(val newPassword: String) : LoginScreenEvent()
    data object LoginBtnClicked : LoginScreenEvent()
}