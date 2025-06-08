package com.sddrozdov.doskacompose.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.sddrozdov.doskacompose.domain.useCase.AuthUseCase
import com.sddrozdov.doskacompose.presentation.states.FilterScreenEvent
import com.sddrozdov.doskacompose.presentation.states.FilterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class FilterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase) : ViewModel() {

    private val _state = MutableStateFlow(FilterScreenState())
    val state: StateFlow<FilterScreenState> = _state

    fun onEvent(event : FilterScreenEvent){
        when(event){

            else -> {}
        }
    }
}