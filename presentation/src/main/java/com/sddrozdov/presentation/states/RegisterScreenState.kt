package com.sddrozdov.presentation.states

import com.sddrozdov.domain.models.User
import kotlin.Result

data class RegisterScreenState(
    val email: String = "",
    val password: String = "",
    var registerResult: Result<User>? = null,
    var authType: AuthType = AuthType.EMAIL
)

sealed class RegisterScreenEvent {
    data class EmailUpdated(val newEmail: String) : RegisterScreenEvent()
    data class PasswordUpdated(val newPassword: String) : RegisterScreenEvent()
    data object RegisterBtnClicked : RegisterScreenEvent()
    data object RegisterGoogleBtnClicked: RegisterScreenEvent()
}

enum class AuthType {
    EMAIL, GOOGLE
}