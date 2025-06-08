package com.sddrozdov.doskacompose.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.sddrozdov.doskacompose.domain.useCase.AuthUseCase
import com.sddrozdov.doskacompose.presentation.states.DialogsScreenEvent
import com.sddrozdov.doskacompose.presentation.states.DialogsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class DialogsViewModel@Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _state = MutableStateFlow(DialogsScreenState())
    val state: StateFlow<DialogsScreenState> = _state

    fun onEvent(event : DialogsScreenEvent){
        when(event){

            else -> {}
        }
    }

}

