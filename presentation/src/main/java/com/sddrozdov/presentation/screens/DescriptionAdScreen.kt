package com.sddrozdov.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.sddrozdov.domain.models.Ad
import com.sddrozdov.presentation.AppColors
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.states.DescriptionAdScreenEvent
import com.sddrozdov.presentation.states.DescriptionAdScreenState
import com.sddrozdov.presentation.states.DescriptionAdScreenUiEvent
import com.sddrozdov.presentation.viewModels.DescriptionAdViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DescriptionAdScreen(
    navHostController: NavHostController,
    adKey: String
) {
    val viewModel = hiltViewModel<DescriptionAdViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(adKey) {
        viewModel.onEvent(DescriptionAdScreenEvent.LoadAdByKey(adKey))
    }

    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is DescriptionAdScreenUiEvent.InitiatePhoneCall -> {
                    val phoneNumber = event.phoneNumber
                    if (!phoneNumber.isNullOrEmpty()) {
                        val callUri = "tel:$phoneNumber"
                        val intentCall = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse(callUri)
                        }
                        try {
                            context.startActivity(intentCall)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Не удалось открыть приложение телефона", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Номер телефона не указан", Toast.LENGTH_SHORT).show()
                    }
                }
                is DescriptionAdScreenUiEvent.InitiateEmail -> {
                    val emailIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(event.email))
                        putExtra(Intent.EXTRA_SUBJECT, event.subject)
                        putExtra(Intent.EXTRA_TEXT, event.body)
                    }
                    try {
                        context.startActivity(Intent.createChooser(emailIntent, "Открыть с помощью"))
                    } catch (e: Exception) {
                        Toast.makeText(context, "Нет приложения для отправки почты", Toast.LENGTH_SHORT).show()
                    }
                }
                is DescriptionAdScreenUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.primaryBackground)
            .padding(16.dp)
    ) {
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.accentColor)
                }
            }

            state.error != null -> {
                ErrorMessage(error = state.error) {
                }
            }

            state.ad != null -> {
                val ad = state.ad

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .widthIn(max = 480.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = AppColors.cardBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = ad.title ?: "",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AppColors.textColor
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (ad.mainImage.isNotEmpty()) {
                            AsyncImage(
                                model = ad.mainImage,
                                contentDescription = "Ad image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = ad.description ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(color = AppColors.secondaryTextColor),
                            maxLines = 6,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Цена: ${ad.price} ₽",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppColors.accentColor
                                )
                            )

                            Text(
                                text = "Просмотры: ${ad.viewsCounter ?: 0}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AppColors.secondaryTextColor
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { onEvent(DescriptionAdScreenEvent.CallOnThePhone) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.accentColor)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_phone),
                                    contentDescription = "Позвонить",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                //Text("Позвонить", color = Color.White)
                            }

                            Button(
                                onClick = { onEvent(DescriptionAdScreenEvent.SendEmail) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.accentColor)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_email),
                                    contentDescription = "Почта",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                //Text("Почта", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    navHostController.navigate("ChatScreen/${ad.key}")
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.accentColor)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chat),
                                    contentDescription = "Чат",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                //Text("Чат", color = Color.White)
                            }
                        }
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