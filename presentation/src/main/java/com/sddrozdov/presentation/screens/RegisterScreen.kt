package com.sddrozdov.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.AuthType
import com.sddrozdov.presentation.states.RegisterScreenEvent
import com.sddrozdov.presentation.states.RegisterScreenState
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.viewModels.RegisterViewModel

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

    val context = LocalContext.current

    val primaryBackground = Color(ContextCompat.getColor(context, R.color.primaryBackground))
    val cardBackground = Color(ContextCompat.getColor(context, R.color.cardBackground))
    val accentColor = Color(ContextCompat.getColor(context, R.color.accentColor))
    val googleButtonColor = Color(ContextCompat.getColor(context, R.color.googleButtonColor))
    val textColor = Color(ContextCompat.getColor(context, R.color.textColor))
    val secondaryTextColor = Color(ContextCompat.getColor(context, R.color.secondaryTextColor))
    val textFieldOutline = Color(ContextCompat.getColor(context, R.color.textFieldOutline))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
            .padding(16.dp)
    ) {

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = textColor
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onEvent(RegisterScreenEvent.EmailUpdated(it)) },
                    label = {
                        Text(
                            text = stringResource(id = R.string.email),
                            color = secondaryTextColor,
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = secondaryTextColor,
                        focusedIndicatorColor = accentColor,
                        unfocusedIndicatorColor = textFieldOutline,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onEvent(RegisterScreenEvent.PasswordUpdated(it)) },
                    label = {
                        Text(
                            text = stringResource(id = R.string.password),
                            color = secondaryTextColor,
                            fontSize = 14.sp
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = secondaryTextColor,
                        focusedIndicatorColor = accentColor,
                        unfocusedIndicatorColor = textFieldOutline,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onEvent(RegisterScreenEvent.RegisterGoogleBtnClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = googleButtonColor,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, googleButtonColor)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = "Google",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(id = R.string.sign_up_with_google),
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onEvent(RegisterScreenEvent.RegisterBtnClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(id = R.string.sign__up), fontSize = 16.sp)
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = { onNavigateTo(Screen.LoginScreen.route) },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.already_have_an_acc),
                    color = accentColor,
                    fontSize = 14.sp
                )
            }

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