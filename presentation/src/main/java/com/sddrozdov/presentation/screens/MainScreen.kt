package com.sddrozdov.presentation.screens

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sddrozdov.presentation.AppColors
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.MainScreenEvent
import com.sddrozdov.presentation.states.MainScreenState
import com.sddrozdov.presentation.viewModels.MainScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(
    navHostController: NavHostController
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    MainScreenView(
        state = state,
        onEvent = viewModel::onEvent,
        navHostController = navHostController
    )
}

@Composable
fun MainScreenView(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
    navHostController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.primaryBackground)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.cardBackground)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Главная",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppColors.textColor
                    )

                    IconButton(
                        onClick = { onEvent(MainScreenEvent.LoadAds) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = "Обновить",
                            tint = AppColors.accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AppColors.accentColor)
                        }
                    }

                    state.error != null -> {
                        ErrorMessage(
                            error = state.error,
                            onRetry = { onEvent(MainScreenEvent.LoadAds) }
                        )
                    }

                    state.ads.isEmpty() -> {
                        EmptyState {
                            navHostController.navigate(Screen.SelectCategoryScreen.route)
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.ads) { ad ->
                                AdCard(
                                    favCount = ad.favCount,
                                    adKey = ad.key?:"",
                                    title = ad.title.orEmpty(),
                                    imageRes = if (ad.mainImage.isNotEmpty()) {
                                        // TODO: загрузка изображения
                                        R.drawable.ic_def_image
                                    } else {
                                        R.drawable.ic_def_image
                                    },
                                    description = ad.description.orEmpty(),
                                    price = "${ad.price} ₽",
                                    viewCount = ad.viewsCounter?.toIntOrNull() ?: 0,
                                    publishTime = formatTime(ad.time),
                                    isFavorite = state.uid?.let { uid -> ad.isFavoriteFor(uid) } ?: false,
                                    onClick = {
                                        navHostController.navigate("description_ad_screen/${ad.key}")
                                    },
                                    onEvent = onEvent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "У вас пока нет объявлений",
            color = AppColors.textColor,
            fontSize = 16.sp
        )
        Button(
            onClick = onCreateClick,
            modifier = Modifier.padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.accentColor,
                contentColor = Color.White
            )
        ) {
            Text("Создать объявление", fontSize = 16.sp)
        }
    }
}

@Composable
fun ErrorMessage(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Ошибка: $error",
            color = AppColors.errorColor,
            fontSize = 16.sp
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.accentColor,
                contentColor = Color.White
            )
        ) {
            Text("Повторить", fontSize = 16.sp)
        }
    }
}

fun formatTime(timestamp: String?): String {
    return try {
        val timeLong = timestamp?.toLongOrNull() ?: return ""
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        sdf.format(Date(timeLong))
    } catch (e: Exception) {
        ""
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    //MainScreen() {}
}
