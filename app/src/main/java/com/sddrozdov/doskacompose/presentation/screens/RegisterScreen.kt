package com.sddrozdov.doskacompose.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.doskacompose.R
import com.sddrozdov.doskacompose.presentation.navigations.Screen
import com.sddrozdov.doskacompose.presentation.states.AuthType
import com.sddrozdov.doskacompose.presentation.states.RegisterScreenEvent
import com.sddrozdov.doskacompose.presentation.states.RegisterScreenState
import com.sddrozdov.doskacompose.presentation.viewModels.RegisterViewModel

@Composable
fun RegisterScreen(
    onNavigateTo: (Screen) -> Unit
) {
    val viewModel = hiltViewModel<RegisterViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.registerResult) {
        state.registerResult?.let { result ->
            result.onSuccess {
                when (state.authType) {
                    AuthType.EMAIL -> onNavigateTo(Screen.LoginScreen)
                    AuthType.GOOGLE -> onNavigateTo(Screen.MainScreen)
                }
            }
        }
    }

    RegisterView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )
}

@Composable
fun RegisterView(
    state: RegisterScreenState,
    onEvent: (RegisterScreenEvent) -> Unit,
    onNavigateTo: (Screen) -> Unit
) {
    Column {
        TextField(
            value = state.email,
            onValueChange = { onEvent(RegisterScreenEvent.EmailUpdated(it)) },
            label = { Text("Email") }
        )
        TextField(
            value = state.password,
            onValueChange = { onEvent(RegisterScreenEvent.PasswordUpdated(it)) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = { onEvent(RegisterScreenEvent.RegisterGoogleBtnClicked) }) {
            Text("Зарегистрироваться с Google")
        }
        Button(onClick = { onEvent(RegisterScreenEvent.RegisterBtnClicked) }) {
            Text("Зарегистрироваться")
        }

        Text(
            text = stringResource(id = R.string.already_have_an_acc),
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 20.dp)
                .clickable {
                    onNavigateTo(Screen.LoginScreen)
                }
        )

        state.registerResult?.let { result ->
            result.onFailure { exception ->
                Text(text = "Register failed: ${exception.localizedMessage}")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val fakeState = RegisterScreenState(email = "", password = "")
    RegisterView(
        state = fakeState,
        onEvent = {},
        onNavigateTo = {}
    )
}