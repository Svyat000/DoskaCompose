package com.sddrozdov.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.MainScreenEvent
import com.sddrozdov.presentation.states.MainScreenState
import com.sddrozdov.presentation.viewModels.MainScreenViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainScreen(
    onNavigateTo: (String) -> Unit
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state by viewModel.state.collectAsState()

    MainScreenView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )
}

@Composable
fun MainScreenView(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
    onNavigateTo: (String) -> Unit
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
                    onNavigateTo("create_ad")
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
                            onEditClick = { onNavigateTo(Screen.LoginScreen.route) },
                            onDeleteClick = { onNavigateTo(Screen.LoginScreen.route) }
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
private fun ErrorMessage(error: String, onRetry: () -> Unit) {
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
    MainScreen() {}
}
