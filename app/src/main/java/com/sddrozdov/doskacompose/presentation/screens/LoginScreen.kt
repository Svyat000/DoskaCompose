package com.sddrozdov.doskacompose.presentation.screens

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
import com.sddrozdov.doskacompose.R
import com.sddrozdov.doskacompose.presentation.navigations.Screen
import com.sddrozdov.doskacompose.presentation.states.LoginScreenEvent
import com.sddrozdov.doskacompose.presentation.states.LoginScreenState
import com.sddrozdov.doskacompose.presentation.viewModels.LoginViewModel


@Composable
fun LoginScreen(
    onNavigateTo: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.loginResult) {
        state.loginResult?.onSuccess {
            onNavigateTo(Screen.MainScreen.route)
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
                    text = stringResource(R.string.welcome_back),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = textColor
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onEvent(LoginScreenEvent.EmailUpdated(it)) },
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
                    onValueChange = { onEvent(LoginScreenEvent.PasswordUpdated(it)) },
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
                    onClick = { onEvent(LoginScreenEvent.LoginGoogleBtnClicked) },
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
                            text = stringResource(id = R.string.sign_in_with_google),
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onEvent(LoginScreenEvent.LoginBtnClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(id = R.string.sign_in), fontSize = 16.sp)
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
                onClick = { onNavigateTo(Screen.RegisterScreen.route) },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.no_account),
                    color = accentColor,
                    fontSize = 14.sp
                )
            }

            TextButton(
                onClick = { onEvent(LoginScreenEvent.ForgotPasswordBtnClicked) }
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_your_password),
                    color = accentColor,
                    fontSize = 14.sp
                )
            }
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
