package com.sddrozdov.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.sddrozdov.domain.repository.AuthRepository
import com.sddrozdov.presentation.states.FilterScreenEvent
import com.sddrozdov.presentation.states.FilterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class FilterViewModel @Inject constructor(
    private val repository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(FilterScreenState())
    val state: StateFlow<FilterScreenState> = _state

    fun onEvent(event : FilterScreenEvent){
        when(event){

            else -> {}
        }
    }
}