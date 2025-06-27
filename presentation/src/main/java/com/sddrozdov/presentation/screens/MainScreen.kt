package com.sddrozdov.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sddrozdov.presentation.R
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
    val state by viewModel.state.collectAsState()

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Главная",
                style = MaterialTheme.typography.headlineSmall
            )

            IconButton(onClick = { onEvent(MainScreenEvent.LoadAds) }) {
                Icon(
                    painter = painterResource(R.drawable.google_icon),//todo(" add icon refresh")
                    contentDescription = "Обновить"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                ErrorMessage(state.error) {
                    onEvent(MainScreenEvent.LoadAds)
                }
            }

            state.ads.isEmpty() -> {
                EmptyState {
                    navHostController.navigate("create_ad") //todo()
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
                            title = ad.title!!,
                            imageRes = if (ad.mainImage.isNotEmpty()) {
                                // TODO: загрузка изображения
                                R.drawable.ic_def_image
                            } else {
                                R.drawable.ic_def_image
                            },
                            description = ad.description!!,
                            price = "${ad.price} ₽",
                            viewCount = ad.viewsCounter?.toIntOrNull() ?: 0,
                            favCount = ad.favoriteCounter.toInt() ?: 0,
                            publishTime = formatTime(ad.time),
                            onClick = {
                                //onEvent(MainScreenEvent.OpenDescriptionAd(ad.key.toString()))
                                navHostController.navigate("description_ad_screen/${ad.key}")
                                Log.d("TAG", "MAIN SCREEN ${ad.key}")
                            }
//                            onEditClick = { onNavigateTo(Screen.LoginScreen.route) },
//                            onDeleteClick = { onNavigateTo(Screen.LoginScreen.route) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("У вас пока нет объявлений")
        Button(
            onClick = onCreateClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Создать объявление")
        }
    }
}

@Composable
fun ErrorMessage(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Повторить")
        }
    }
}

private fun formatTime(timestamp: String?): String {
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
