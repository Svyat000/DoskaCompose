package com.sddrozdov.presentation.states

import android.os.Parcelable
import com.sddrozdov.domain.models.User
import kotlinx.parcelize.Parcelize
import kotlin.Result

@Parcelize
data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    var loginResult: Result<User>? = null,
) : Parcelable

sealed class LoginScreenEvent {
    data class EmailUpdated(val newEmail: String) : LoginScreenEvent()
    data class PasswordUpdated(val newPassword: String) : LoginScreenEvent()
    data object LoginBtnClicked : LoginScreenEvent()
    data object LoginGoogleBtnClicked : LoginScreenEvent()
    data object ForgotPasswordBtnClicked : LoginScreenEvent()
}