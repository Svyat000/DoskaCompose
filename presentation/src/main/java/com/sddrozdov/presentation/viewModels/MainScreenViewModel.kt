package com.sddrozdov.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.domain.repository.AuthRepository
import com.sddrozdov.presentation.states.MainScreenEvent
import com.sddrozdov.presentation.states.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: AuthRepository,

) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.SignOutBtnClicked -> signOut()
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            Log.d("EXIT", "viewModel вышли")
        }
    }

}