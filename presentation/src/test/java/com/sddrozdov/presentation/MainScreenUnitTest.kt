package com.sddrozdov.presentation

import com.sddrozdov.domain.models.Ad
import com.sddrozdov.domain.useCase.AuthUseCaseInterface
import com.sddrozdov.domain.useCase.CreateAdUseCaseInterface
import com.sddrozdov.presentation.viewModels.MainScreenViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    private val createAdUseCase: CreateAdUseCaseInterface = mock()
    private val authUseCase: AuthUseCaseInterface = mock()

    private lateinit var mainScreenViewModel: MainScreenViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mainScreenViewModel = MainScreenViewModel(createAdUseCase, authUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAds should update state with ads list on success`() = runTest {
        // Arrange
        val adsList = listOf(Ad(), Ad())
        `when`(createAdUseCase.readAllAdFromDb()).thenReturn(Result.success(adsList))

        // Act
        mainScreenViewModel.loadAds()
        testDispatcher.scheduler.advanceUntilIdle() // Дожидаемся выполнения всех корутин

        // Assert
        val state = mainScreenViewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(adsList, state.ads)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadAds should update state with error on failure`() = runTest {
        // Arrange
        val errorMessage = "Failed to load ads"
        val exception = Exception(errorMessage)
        `when`(createAdUseCase.readAllAdFromDb()).thenReturn(Result.failure(exception))

        // Act
        mainScreenViewModel.loadAds()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = mainScreenViewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(emptyList<Ad>(), state.ads)
        assertEquals(errorMessage, state.error)
    }

}