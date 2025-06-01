package com.sddrozdov.doskacompose.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.sddrozdov.doskacompose.presentation.states.AuthType
import com.sddrozdov.doskacompose.presentation.states.RegisterScreenEvent
import com.sddrozdov.doskacompose.presentation.states.RegisterScreenState
import com.sddrozdov.doskacompose.presentation.viewModels.RegisterViewModel

@Composable
fun RegisterScreen(
    onNavigateTo: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val viewModel = hiltViewModel<RegisterViewModel>()
    val state by viewModel.state.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.registerResult) {
        state.registerResult?.let { result ->
            result.onSuccess {
                when (state.authType) {
                    AuthType.EMAIL -> onNavigateTo(Screen.LoginScreen.route)
                    AuthType.GOOGLE -> onNavigateTo(Screen.MainScreen.route)
                }
            }
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { messageRes ->
            val message = context.getString(messageRes)
            snackbarHostState.showSnackbar(message)
            viewModel.messageShown()

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
    onNavigateTo: (String) -> Unit
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
                text = "Зарегистрируйся!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.Black
            )

            TextField(
                value = state.email,
                onValueChange = { onEvent(RegisterScreenEvent.EmailUpdated(it)) },
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
                onValueChange = { onEvent(RegisterScreenEvent.PasswordUpdated(it)) },
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
                onClick = { onEvent(RegisterScreenEvent.RegisterGoogleBtnClicked) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Зарегистрироваться с Google", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEvent(RegisterScreenEvent.RegisterBtnClicked) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Зарегистрироваться", color = Color.White)
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
                text = stringResource(id = R.string.already_have_an_acc),
                modifier = Modifier
                    .clickable { onNavigateTo(Screen.LoginScreen.route) }
                    .padding(vertical = 8.dp),
                color = Color.Blue
            )
        }


//        state.registerResult?.onFailure { exception ->
//            Text(
//                text = "Ошибка регистрации: ${exception.localizedMessage}",
//                color = Color.Red,
//                modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)
//            )
//        }
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