package com.sddrozdov.doskacompose.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.doskacompose.presentation.states.MainScreenEvent
import com.sddrozdov.doskacompose.presentation.states.MainScreenState
import com.sddrozdov.doskacompose.presentation.viewModels.MainScreenViewModel

@Composable
fun MainScreen(
    onNavigateTo: (String) -> Unit
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state by viewModel.state.collectAsState()

    MainScreenView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )

}

@Composable
fun MainScreenView(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Column {

        Text("ГЛАВНЫЙ ЭКРАН ТЕСТ")
        Button(onClick = { onEvent(MainScreenEvent.SignOutBtnClicked)
        Log.d("EXIT","Screen ВЫШЛИ")
        }) {
            Text("ВЫХОД")

        }

    }

}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen() {

    }
}
