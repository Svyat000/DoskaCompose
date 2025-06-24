package com.sddrozdov.presentation.screens.createAdScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sddrozdov.presentation.AppColors
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import com.sddrozdov.presentation.viewModels.createAd.CreateAdViewModel

@Composable
fun EnterPostalCodeScreen(
    navHostController: NavHostController,
    viewModel: CreateAdViewModel
) {

    val state by viewModel.state.collectAsState()

    EnterPostalCodeView(
        state = state,
        onEvent = viewModel::onEvent,
        navHostController = navHostController
    )
}

@Composable
fun EnterPostalCodeView(
    state: CreateAdStates,
    onEvent: (CreateAdEvents) -> Unit,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.primaryBackground)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.cardBackground)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Введите почтовый индекс",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = AppColors.textColor
                )

                OutlinedTextField(
                    value = state.postalCode,
                    onValueChange = { newCode ->
                        // Удаляем все нецифры
                        val cleanCode = newCode.replace("\\D".toRegex(), "")

                        // Проверяем что не больше 6 цифр
                        if (cleanCode.length <= 6) {
                            onEvent(CreateAdEvents.OnPostalCodeChanged(cleanCode))
                        }
                    },
                    label = {
                        Text(
                            text = "Почтовый индекс",
                            color = AppColors.secondaryTextColor,
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = AppColors.textColor,
                        unfocusedTextColor = AppColors.textColor,
                        focusedLabelColor = AppColors.accentColor,
                        unfocusedLabelColor = AppColors.secondaryTextColor,
                        focusedIndicatorColor = AppColors.accentColor,
                        unfocusedIndicatorColor = AppColors.textFieldOutline,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = {
                        Text(
                            text = "Например: 123456",
                            color = AppColors.secondaryTextColor
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = AppColors.textColor
                    ),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navHostController.popBackStack() },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.accentColor,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.back),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = {
                    navHostController.navigate(Screen.EnterPhoneScreen.route)
                },
                enabled = state.postalCode.length == 6,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.postalCode.length == 6) {
                        AppColors.accentColor
                    } else {
                        AppColors.disabledButtonColor
                    },
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Продолжить",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

