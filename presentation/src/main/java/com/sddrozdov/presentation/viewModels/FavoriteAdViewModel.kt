package com.sddrozdov.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.sddrozdov.domain.repository.AuthRepository
import com.sddrozdov.presentation.states.FavoriteAdScreenEvent
import com.sddrozdov.presentation.states.FavoriteAdScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteAdViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {


    private val _state = MutableStateFlow(FavoriteAdScreenState())
    val state: StateFlow<FavoriteAdScreenState> = _state

   fun onEvent(event: FavoriteAdScreenEvent){

   }
}