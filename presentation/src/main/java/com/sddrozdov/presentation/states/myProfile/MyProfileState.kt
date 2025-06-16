package com.sddrozdov.presentation.states.myProfile

data class MyProfileState(
    val test : String = "",

)

sealed class ProfileScreenEvent {
    object Logout : ProfileScreenEvent()
}
