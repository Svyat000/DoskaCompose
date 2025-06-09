package com.sddrozdov.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.sddrozdov.domain.repository.AuthRepository
import com.sddrozdov.presentation.states.DescriptionAdScreenEvent
import com.sddrozdov.presentation.states.DescriptionAdScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DescriptionAdViewModel@Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(DescriptionAdScreenState())
    val state: StateFlow<DescriptionAdScreenState> = _state

    fun onEvent(event : DescriptionAdScreenEvent){
        when(event){

            else -> {}
        }
    }
}



