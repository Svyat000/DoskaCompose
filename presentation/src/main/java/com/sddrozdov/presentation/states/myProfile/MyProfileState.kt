package com.sddrozdov.presentation.states.myProfile

data class MyProfileState(
    val userName: String = "User Name",
    val email: String = "user@example.com",
    val photoUrl: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

)

sealed class ProfileScreenEvent {
    object Logout : ProfileScreenEvent()
}
