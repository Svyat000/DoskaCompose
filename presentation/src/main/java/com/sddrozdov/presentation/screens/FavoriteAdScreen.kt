package com.sddrozdov.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.presentation.states.FavoriteAdScreenEvent
import com.sddrozdov.presentation.states.FavoriteAdScreenState
import com.sddrozdov.presentation.viewModels.FavoriteAdViewModel

@Composable
fun FavoriteAdScreen(
    onNavigateTo: (String) -> Unit
) {
    val viewModel = hiltViewModel<FavoriteAdViewModel>()
    val state by viewModel.state.collectAsState()

    FavoriteAdScreenView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )

}

@Composable
fun FavoriteAdScreenView(
    state: FavoriteAdScreenState,
    onEvent: (FavoriteAdScreenEvent) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Column {

        Text("Избранные ЭКРАН ТЕСТ")


    }

}


@Preview(showBackground = true)
@Composable
fun FavoriteAdScreenPreview() {
    FavoriteAdScreen() {

    }
}