package com.sddrozdov.doskacompose.presentation.states

import com.google.firebase.auth.FirebaseUser

data class MainScreenState(
    val email: String = "",
    val password: String = "",
    var loginResult: Result<FirebaseUser>? = null
)

sealed class MainScreenEvent {

    data object SignOutBtnClicked : MainScreenEvent()

}