package com.sddrozdov.presentation.states.myProfile

data class MyProfileState(
    val uid: String? = null,
    val userName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

)

sealed class ProfileScreenEvent {
    object Logout : ProfileScreenEvent()
}
