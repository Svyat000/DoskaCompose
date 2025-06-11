package com.sddrozdov.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.domain.useCase.AuthUseCase
import com.sddrozdov.presentation.states.MainScreenEvent
import com.sddrozdov.presentation.states.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,

) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    private fun test(){
        viewModelScope.launch {
            authUseCase.signInAnonymously()
        }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.SignOutBtnClicked -> test()
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            authUseCase.signOut()
            Log.d("EXIT", "viewModel вышли")
        }
    }

}