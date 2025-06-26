package com.sddrozdov.presentation.viewModels.myProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.domain.useCase.AuthUseCase
import com.sddrozdov.presentation.states.myProfile.MyProfileState
import com.sddrozdov.presentation.states.myProfile.ProfileScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyProfileState())
    val state: StateFlow<MyProfileState> = _state

    init {
        loadUserProfile()
    }

    fun onEvent(event: ProfileScreenEvent) {
        when (event) {
            ProfileScreenEvent.Logout -> logout()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = authUseCase.getCurrentUser()
            if (user != null) {
                _state.value = MyProfileState(
                    userName = user.displayName ?: "No name",
                    email = user.email ?: "No email",
                    photoUrl = user.photoUrl?.toString()
                )
            } else {
                _state.value = MyProfileState(
                    userName = "",
                    email = "",
                    photoUrl = null
                )
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authUseCase.signOut()
        }
    }
}