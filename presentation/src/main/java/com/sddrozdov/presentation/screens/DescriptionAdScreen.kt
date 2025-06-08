package com.sddrozdov.doskacompose.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.doskacompose.presentation.states.DescriptionAdScreenEvent
import com.sddrozdov.doskacompose.presentation.states.DescriptionAdScreenState
import com.sddrozdov.doskacompose.presentation.viewModels.DescriptionAdViewModel

@Composable
fun DescriptionAdScreen(
    onNavigateTo: (String) -> Unit
) {
    val viewModel = hiltViewModel<DescriptionAdViewModel>()
    val state by viewModel.state.collectAsState()

    DescriptionAdView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )

}

@Composable
fun DescriptionAdView(
    state: DescriptionAdScreenState,
    onEvent: (DescriptionAdScreenEvent) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Column {

        Text("Описание Объявл ЭКРАН ТЕСТ")


    }

}


@Preview(showBackground = true)
@Composable
fun DescriptionAdScreenPreview() {
    DescriptionAdScreen() {

    }
}