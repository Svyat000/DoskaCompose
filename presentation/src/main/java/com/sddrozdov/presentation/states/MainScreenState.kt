package com.sddrozdov.doskacompose.presentation.states

data class MainScreenState(
    val signOutResult: Result<Unit>? = null
)

sealed class MainScreenEvent {

    data object SignOutBtnClicked : MainScreenEvent()

}