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
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavHostController
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import com.sddrozdov.presentation.viewModels.createAd.CreateAdViewModel


@Composable
fun EnterPhoneScreen(
    navHostController: NavHostController,
    viewModel: CreateAdViewModel
) {

    val state by viewModel.state.collectAsState()

    EnterPhoneView(
        state = state,
        onEvent = viewModel::onEvent,
        navHostController = navHostController
    )
}

@Composable
fun EnterPhoneView(
    state: CreateAdStates,
    onEvent: (CreateAdEvents) -> Unit,
    navHostController: NavHostController
) {
    val colorScheme = MaterialTheme.colorScheme
    var textFieldValue by remember { mutableStateOf(TextFieldValue("+7")) }

    // Синхронизация с состоянием ViewModel
    LaunchedEffect(state.phone) {
        if (state.phone != textFieldValue.text.replace("[^+\\d]".toRegex(), "")) {
            textFieldValue = formatPhoneNumberWithCursor(TextFieldValue(state.phone))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.enter_your_phone_number),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = colorScheme.onSurface
                )

                Text(
                    text = stringResource(R.string.buyers_will_be_able_to_call_this_number),
                    fontSize = 16.sp,
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        // Ограничиваем длину и разрешаем только цифры и +
                        val filtered = newValue.text.take(18).filter { it.isDigit() || it == '+' }

                        // Форматируем с сохранением позиции курсора
                        val formatted = formatPhoneNumberWithCursor(
                            newValue.copy(text = filtered)
                        )

                        textFieldValue = formatted

                        // Сохраняем в состояние только цифры и +
                        val cleanPhone = formatted.text.replace("[^+\\d]".toRegex(), "")
                        onEvent(CreateAdEvents.OnPhoneChanged(cleanPhone))
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.phone_number),
                            color = colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                        focusedIndicatorColor = colorScheme.primary,
                        unfocusedIndicatorColor = colorScheme.outline,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    placeholder = {
                        Text(
                            text = "+7 (XXX) XXX-XX-XX",
                            color = colorScheme.onSurfaceVariant
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = colorScheme.onSurface
                    ),
                    singleLine = true
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
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
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
                    onEvent(CreateAdEvents.OnPublishClicked)
                    navHostController.navigate(Screen.MainScreen.route)
                },
                enabled = isValidPhoneNumber(state.phone),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isValidPhoneNumber(state.phone)) {
                        colorScheme.primary
                    } else {
                        colorScheme.surfaceVariant.copy(alpha = 0.38f)
                    },
                    contentColor = if (isValidPhoneNumber(state.phone)) {
                        colorScheme.onPrimary
                    } else {
                        colorScheme.onSurfaceVariant
                    }
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.publish),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Форматируем номер телефона с учетом позиции курсора
 */
private fun formatPhoneNumberWithCursor(input: TextFieldValue): TextFieldValue {
    val originalText = input.text
    val originalCursor = input.selection.start

    // Удаляем все символы кроме цифр и +
    val digitsOnly = originalText.filter { it.isDigit() || it == '+' }

    // Считаем сколько цифр было до курсора в исходном тексте
    var digitsBeforeCursor = 0
    for (i in 0 until originalCursor.coerceAtMost(originalText.length)) {
        if (originalText[i].isDigit() || originalText[i] == '+') {
            digitsBeforeCursor++
        }
    }

    // Форматируем номер по шаблону +7 (XXX) XXX-XX-XX
    val formatted = StringBuilder()
    var cursorPosition = 0
    var digitsProcessed = 0

    // Код страны
    if (digitsOnly.isNotEmpty() && digitsOnly.first() == '+') {
        formatted.append(digitsOnly.take(1)) // +
        digitsProcessed++
        if (digitsOnly.length > 1) {
            formatted.append(digitsOnly[1]) // 7
            digitsProcessed++
        }
    }

    // Остальные цифры
    val restDigits = digitsOnly.drop(2)
    if (restDigits.isNotEmpty()) {
        formatted.append(" (")

        // Первые 3 цифры
        val firstGroup = restDigits.take(3)
        formatted.append(firstGroup)
        digitsProcessed += firstGroup.length

        if (restDigits.length > 3) {
            formatted.append(") ")

            // Вторая группа из 3 цифр
            val secondGroup = restDigits.drop(3).take(3)
            formatted.append(secondGroup)
            digitsProcessed += secondGroup.length

            if (restDigits.length > 6) {
                formatted.append("-")

                // Третья группа из 2 цифр
                val thirdGroup = restDigits.drop(6).take(2)
                formatted.append(thirdGroup)
                digitsProcessed += thirdGroup.length

                if (restDigits.length > 8) {
                    formatted.append("-")

                    // Четвертая группа из 2 цифр
                    val fourthGroup = restDigits.drop(8).take(2)
                    formatted.append(fourthGroup)
                    digitsProcessed += fourthGroup.length
                }
            }
        }
    }

    // Рассчитываем позицию курсора в отформатированной строке
    cursorPosition = calculateCursorPosition(
        digitsBeforeCursor = digitsBeforeCursor,
        formatted = formatted.toString()
    )

    return TextFieldValue(
        text = formatted.toString(),
        selection = TextRange(cursorPosition)
    )
}

/**
 * Вычисляет позицию курсора в отформатированной строке
 */
private fun calculateCursorPosition(digitsBeforeCursor: Int, formatted: String): Int {
    if (digitsBeforeCursor <= 0) return 0

    var digitsCount = 0
    formatted.forEachIndexed { index, c ->
        if (c.isDigit() || c == '+') {
            digitsCount++
            if (digitsCount == digitsBeforeCursor) {
                // Возвращаем позицию после текущего символа
                return index + 1
            }
        }
    }

    // Если курсор должен быть в конце
    return formatted.length
}

/**
 * Проверка валидности номера телефона 11 цифр с кодом страны
 */
private fun isValidPhoneNumber(phone: String): Boolean {
    return phone.replace("[^\\d]".toRegex(), "").length == 11
}
