package com.sddrozdov.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.presentation.states.DialogsScreenEvent
import com.sddrozdov.presentation.states.DialogsScreenState
import com.sddrozdov.presentation.viewModels.DialogsViewModel

@Composable
fun DialogsScreen(
    onNavigateTo: (String) -> Unit
) {
    val viewModel = hiltViewModel<DialogsViewModel>()
    val state by viewModel.state.collectAsState()

    DialogsScreenView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )

}

@Composable
fun DialogsScreenView(
    state: DialogsScreenState,
    onEvent: (DialogsScreenEvent) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Column {

        Text("Диалоги ЭКРАН ТЕСТ")


    }

}


@Preview(showBackground = true)
@Composable
fun DialogsScreenPreview() {
    DialogsScreen() {

    }
}