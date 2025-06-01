package com.sddrozdov.doskacompose.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    onNavigateTo: (Screen) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.loginResult) {
        state.loginResult?.onSuccess {
            onNavigateTo(Screen.MainScreen)
        }?.onFailure { e ->
            snackbarMessage?.let { messageRes ->
                val message = context.getString(messageRes)
                snackbarHostState.showSnackbar(message)
                viewModel.messageShown()
            }
            //println("Google sign in failed: ${e.message}")
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { messageRes ->
            val message = context.getString(messageRes)
            snackbarHostState.showSnackbar(message)
            viewModel.messageShown()

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "С возвращением!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.Black
            )

            TextField(
                value = state.email,
                onValueChange = { onEvent(LoginScreenEvent.EmailUpdated(it)) },
                label = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = state.password,
                onValueChange = { onEvent(LoginScreenEvent.PasswordUpdated(it)) },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(LoginScreenEvent.LoginGoogleBtnClicked) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Войти с Google", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEvent(LoginScreenEvent.LoginBtnClicked) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Войти", color = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.no_account),
                modifier = Modifier
                    .clickable { onNavigateTo(Screen.RegisterScreen) }
                    .padding(vertical = 8.dp),
                color = Color.Blue
            )

            Text(
                text = stringResource(id = R.string.forgot_your_password),
                modifier = Modifier
                    .clickable { onEvent(LoginScreenEvent.ForgotPasswordBtnClicked) }
                    .padding(vertical = 8.dp),
                color = Color.Blue
            )
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
