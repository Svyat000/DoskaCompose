package com.sddrozdov.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.sddrozdov.domain.models.Ad
import com.sddrozdov.presentation.states.DescriptionAdScreenEvent
import com.sddrozdov.presentation.states.DescriptionAdScreenState
import com.sddrozdov.presentation.viewModels.DescriptionAdViewModel

@Composable
fun DescriptionAdScreen(
    navHostController: NavHostController,
    adKey: String
) {
    val viewModel = hiltViewModel<DescriptionAdViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(adKey) {
        viewModel.onEvent(DescriptionAdScreenEvent.LoadAdByKey(adKey))
    }

    DescriptionAdView(
        state = state,
        onEvent = viewModel::onEvent,
        navHostController = navHostController
    )
}

@Composable
fun DescriptionAdView(
    state: DescriptionAdScreenState,
    onEvent: (DescriptionAdScreenEvent) -> Unit,
    navHostController: NavHostController,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                ErrorMessage(error = state.error) {

                }
            }

            state.ad != null -> {
                val ad = state.ad

                Text(text = ad.title ?: "", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (ad.mainImage.isNotEmpty()) {
                    AsyncImage(
                        model = ad.mainImage,
                        contentDescription = "Ad image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = ad.description ?: "", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Цена: ${ad.price} ₽", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Просмотры: ${ad.viewsCounter ?: 0}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { onEvent(DescriptionAdScreenEvent.OpenEdit(ad)) }) {
                        Text("Редактировать")
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        onClick = { onEvent(DescriptionAdScreenEvent.DeleteAd(ad)) }
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DescriptionAdViewPreview() {
    val sampleAd = Ad(
        key = "1",
        title = "Пример объявления",
        mainImage = "https://via.placeholder.com/600x400.png?text=Ad+Image",
        description = "Это подробное описание объявления. Здесь можно разместить всю информацию о товаре или услуге.",
        price = 12345.toString(),
        viewsCounter = 789.toString(),
        favoriteCounter = 42.toString(),
        time = System.currentTimeMillis().toString()
    )

    val sampleState = DescriptionAdScreenState(
        isLoading = false,
        error = null,
        ad = sampleAd
    )

    val navController = androidx.navigation.compose.rememberNavController()

    DescriptionAdView(
        state = sampleState,
        onEvent = {},
        navHostController = navController
    )
}