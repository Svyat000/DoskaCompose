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
import com.sddrozdov.doskacompose.presentation.states.LoginScreenEvent
import com.sddrozdov.doskacompose.presentation.states.LoginScreenState
import com.sddrozdov.doskacompose.presentation.viewModels.LoginViewModel

@Composable
fun LoginScreen(
    onNavigateTo: (Screen) -> Unit
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.loginResult) {
        state.loginResult?.onSuccess {
            onNavigateTo(Screen.MainScreen)
        }?.onFailure { e ->
            println("Google sign in failed: ${e.message}")
        }
    }

    LoginView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )
}

@Composable
fun LoginView(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
    onNavigateTo: (Screen) -> Unit
) {
    Column {
        TextField(
            value = state.email,
            onValueChange = { onEvent(LoginScreenEvent.EmailUpdated(it)) },
            label = { Text("Email") }
        )
        TextField(
            value = state.password,
            onValueChange = { onEvent(LoginScreenEvent.PasswordUpdated(it)) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = { onEvent(LoginScreenEvent.LoginGoogleBtnClicked) }) {
            Text("Login Google")
        }
        Button(onClick = { onEvent(LoginScreenEvent.LoginBtnClicked) }) {
            Text("Login")
        }
        Text(
            text = stringResource(id = R.string.no_account),
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 20.dp)
                .clickable {
                    onNavigateTo(Screen.RegisterScreen)
                }
        )

        state.loginResult?.onFailure { exception ->
            Text(text = "Login failed: ${exception.localizedMessage}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val fakeState = LoginScreenState(email = "", password = "")
    LoginView(
        state = fakeState,
        onEvent = {},
        onNavigateTo = {}
    )
}