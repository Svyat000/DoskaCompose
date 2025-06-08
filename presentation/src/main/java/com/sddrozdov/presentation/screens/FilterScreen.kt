package com.sddrozdov.doskacompose.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.doskacompose.presentation.states.FilterScreenEvent
import com.sddrozdov.doskacompose.presentation.states.FilterScreenState
import com.sddrozdov.doskacompose.presentation.viewModels.FilterViewModel

@Composable
fun FilterScreen(
    onNavigateTo: (String) -> Unit
) {
    val viewModel = hiltViewModel<FilterViewModel>()
    val state by viewModel.state.collectAsState()

    FilterScreenView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )

}

@Composable
fun FilterScreenView(
    state: FilterScreenState,
    onEvent: (FilterScreenEvent) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Column {

        Text("Фильтр ЭКРАН ТЕСТ")


    }

}


@Preview(showBackground = true)
@Composable
fun FilterScreenPreview() {
    FilterScreen() {

    }
}