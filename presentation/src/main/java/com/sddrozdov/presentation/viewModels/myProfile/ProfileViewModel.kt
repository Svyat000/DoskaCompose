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

    fun onEvent(event: ProfileScreenEvent) {
        when (event) {
            ProfileScreenEvent.Logout -> {
                viewModelScope.launch {
                   authUseCase.signOut()
                }
            }
        }
    }
}