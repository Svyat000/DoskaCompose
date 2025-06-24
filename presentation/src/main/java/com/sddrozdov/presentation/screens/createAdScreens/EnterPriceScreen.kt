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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sddrozdov.presentation.AppColors
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import com.sddrozdov.presentation.viewModels.createAd.CreateAdViewModel

@Composable
fun EnterPriceScreen(
    navHostController: NavHostController
) {
    val viewModel: CreateAdViewModel = hiltViewModel<CreateAdViewModel>()
    val state by viewModel.state.collectAsState()

    EnterPriceView(
        state = state,
        onEvent = viewModel::onEvent,
        navHostController = navHostController
    )
}

@Composable
fun EnterPriceView(
    state: CreateAdStates,
    onEvent: (CreateAdEvents) -> Unit,
    navHostController: NavHostController
) {
    var priceValue by remember { mutableStateOf(TextFieldValue(state.price)) }

    LaunchedEffect(state.price) {
        if (state.price != priceValue.text.replace(" ", "")) {
            // Форматируем строку из состояния и ставим курсор в конец
            val formatted = formatPriceWithCursor(TextFieldValue(state.price))
            priceValue = formatted
        }
    }

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
                    text = "Укажите цену товара",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = AppColors.textColor
                )

                OutlinedTextField(
                    value = priceValue,
                    onValueChange = { newValue ->
                        // Форматируем новый ввод с сохранением позиции курсора
                        val formattedValue = formatPriceWithCursor(newValue)
                        priceValue = formattedValue

                        // Отправляем в ViewModel только цифры без пробелов
                        val cleanPrice = formattedValue.text.replace(" ", "")
                        if (cleanPrice.length <= 8) {
                            onEvent(CreateAdEvents.OnPriceChanged(cleanPrice))
                        }
                    },
                    label = {
                        Text(
                            text = "Цена",
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
                            text = "Например: 1000 или 999.99",
                            color = AppColors.secondaryTextColor
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = AppColors.textColor
                    ),
                    singleLine = true,
                    trailingIcon = {
                        Text(
                            text = "₽",
                            color = AppColors.textColor,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
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
                    navHostController.navigate(Screen.EnterPostalCodeScreen.route)
                },
                enabled = state.price.isNotEmpty() && state.price.toDoubleOrNull() != null,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.price.isNotEmpty() && state.price.toDoubleOrNull() != null) {
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

// Функция форматирования с сохранением позиции курсора
fun formatPriceWithCursor(input: TextFieldValue): TextFieldValue {
    val originalText = input.text
    val originalSelection = input.selection.start
    // Удаляем все кроме цифр
    val digitsOnly = originalText.filter { it.isDigit() }
    // Ограничиваем длину до 8 цифр
    val limitedDigits = if (digitsOnly.length > 8) digitsOnly.take(8) else digitsOnly
    // Форматируем число с пробелами по тысячам
    val formatted = limitedDigits.reversed().chunked(3).joinToString(" ").reversed()
    // Рассчитываем позицию курсора после форматирования
    // Считаем сколько цифр было до курсора в оригинальном тексте
    val digitsBeforeCursor = originalText.take(originalSelection).count { it.isDigit() }
    // Позиция курсора — это длина форматированного текста, соответствующая digitsBeforeCursor
    var cursorPos = 0
    var digitsCounted = 0
    while (cursorPos < formatted.length && digitsCounted < digitsBeforeCursor) {
        if (formatted[cursorPos].isDigit()) digitsCounted++
        cursorPos++
    }

    return TextFieldValue(
        text = formatted,
        selection = TextRange(cursorPos)
    )
}