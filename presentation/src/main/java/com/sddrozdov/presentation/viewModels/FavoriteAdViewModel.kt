package com.sddrozdov.doskacompose.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.sddrozdov.doskacompose.domain.useCase.AdUseCase
import com.sddrozdov.doskacompose.presentation.states.FavoriteAdScreenEvent
import com.sddrozdov.doskacompose.presentation.states.FavoriteAdScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteAdViewModel @Inject constructor(private val useCase: AdUseCase) : ViewModel() {


    private val _state = MutableStateFlow(FavoriteAdScreenState())
    val state: StateFlow<FavoriteAdScreenState> = _state

   fun onEvent(event: FavoriteAdScreenEvent){

   }
}