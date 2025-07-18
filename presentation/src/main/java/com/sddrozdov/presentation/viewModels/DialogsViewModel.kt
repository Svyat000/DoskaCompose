package com.sddrozdov.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.sddrozdov.domain.repository.AuthRepository
import com.sddrozdov.presentation.states.DialogsScreenEvent
import com.sddrozdov.presentation.states.DialogsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class DialogsViewModel@Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(DialogsScreenState())
    val state: StateFlow<DialogsScreenState> = _state

    fun onEvent(event : DialogsScreenEvent){
        when(event){

            else -> {}
        }
    }

}

