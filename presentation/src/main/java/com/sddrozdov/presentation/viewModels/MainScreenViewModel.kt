package com.sddrozdov.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sddrozdov.domain.useCase.AuthUseCase
import com.sddrozdov.domain.useCase.CreateAdUseCase
import com.sddrozdov.presentation.states.MainScreenEvent
import com.sddrozdov.presentation.states.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val AD_FROM_MAIN_TO_DESC_SCREEN = "ad_from_main_screen_to_description_screen"

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val createAdUseCase: CreateAdUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        loadInitialData()
    }
    private fun loadInitialData() {
        viewModelScope.launch {
            if (_state.value.ads.isEmpty()) {
                loadAds()
            }
        }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.LoadAds -> loadAds()
            is MainScreenEvent.ShowError -> _state.value = _state.value.copy(error = event.message)
            is MainScreenEvent.AddFavoriteAd -> toggleFavoriteAd(event.key)
            MainScreenEvent.LoadUsersFavoriteAds -> loadFavoriteAds()
        }
    }


    private fun toggleFavoriteAd(key: String) {
        viewModelScope.launch {
            val user = authUseCase.getCurrentUser() ?: run {
                _state.value = _state.value.copy(error = "Пользователь не авторизован")
                return@launch
            }

            val uid = user.uid ?: run {
                _state.value = _state.value.copy(error = "Ошибка: UID не найден")
                return@launch
            }

            _state.value = _state.value.copy(uid = uid)

            user.uid?.let {
                createAdUseCase.toggleFavoriteAd(key, uid).fold(
                    onSuccess = { updateAdAfterFavoriteToggle(key, uid) },
                    onFailure = { error ->
                        _state.value =
                            _state.value.copy(error = "Ошибка избранного: ${error.message}")
                    }
                )
            }
        }
    }

    private fun updateAdAfterFavoriteToggle(key: String, uid: String) {
        _state.value = _state.value.copy(ads = _state.value.ads.map { ad ->
            if (ad.key == key) {
                val currentFav = ad.favorites[uid] ?: false
                val updatedFavs = ad.favorites.toMutableMap().apply {
                    this[uid] = !currentFav
                }
                ad.copy(favorites = updatedFavs)
            } else {
                ad
            }
        })
    }

    private fun loadAds() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = createAdUseCase.readAllAdFromDb()
            result.fold(
                onSuccess = { adsList ->
                    _state.value = MainScreenState(isLoading = false, ads = adsList, error = null)
                },
                onFailure = { throwable ->
                    _state.value = MainScreenState(
                        isLoading = false,
                        ads = emptyList(),
                        error = throwable.message
                    )
                }
            )
        }
    }

    private fun loadFavoriteAds() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {

            val user = authUseCase.getCurrentUser() ?: run {
                _state.value = _state.value.copy(error = "Пользователь не авторизован")
                return@launch
            }

            val uid = user.uid ?: run {
                _state.value = _state.value.copy(error = "Ошибка: UID не найден")
                return@launch
            }

            val result = createAdUseCase.loadFavAdsForUser(uid)
            result.fold(
                onSuccess = { adsList ->
                    _state.value =
                        MainScreenState(isLoading = false, favoriteAds = adsList, error = null)
                },
                onFailure = { throwable ->
                    _state.value = MainScreenState(
                        isLoading = false,
                        favoriteAds = emptyList(),
                        error = throwable.message
                    )
                }
            )
        }
    }

}