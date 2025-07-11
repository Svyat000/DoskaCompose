package com.sddrozdov.presentation.screens

import android.util.Log
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sddrozdov.presentation.AppColors
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.MyAdScreenEvent
import com.sddrozdov.presentation.states.MyAdScreenState
import com.sddrozdov.presentation.viewModels.MyAdViewModel

@Composable
fun MyAdScreen(
    navHostController: NavHostController
) {
    val viewModel = hiltViewModel<MyAdViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyAdScreenView(
        state = state,
        onEvent = viewModel::onEvent,
        navHostController = navHostController
    )
}

@Composable
fun MyAdScreenView(
    state: MyAdScreenState,
    onEvent: (MyAdScreenEvent) -> Unit,
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
                        text = "Мои объявления",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppColors.textColor
                    )

                    IconButton(
                        onClick = { onEvent(MyAdScreenEvent.LoadMyAds) },
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
                            onRetry = { onEvent(MyAdScreenEvent.LoadMyAds) }
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
                                AdMyCard(
                                    title = ad.title.orEmpty(),
                                    imageUri = ad.mainImage,
                                    description = ad.description.orEmpty(),
                                    price = "${ad.price} ₽",
                                    publishTime = formatTime(ad.time),
                                    onClick = {
                                        navHostController.navigate("description_ad_screen/${ad.key}")
                                    },
                                    onDeleteClick = {
                                        Log.d("MYTAG", " SCREEN MY AD CLICKED DELETE")
                                        ad.key?.let { key ->
                                            onEvent(MyAdScreenEvent.DeleteAd(key))
                                        }
                                    },
                                    onEditClick = {}
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
fun AdMyCard(
    title: String,
    imageUri: String,
    description: String,
    price: String,
    publishTime: String,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    val primarySurface = Color(0xFFF7F9FC)
    val accentColor = Color(0xFF6C5CE7)
    val darkText = Color(0xFF2D3436)
    val lightText = Color(0xFF636E72)
    val priceBg = Color(0xFF00B894)
    val iconColor = Color(0xFF636E72)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = primarySurface)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                color = accentColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .crossfade(true)
                    .error(R.drawable.ic_def_image)
                    .placeholder(R.drawable.ic_def_image)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = darkText,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 3
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = price,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = priceBg,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFDFE6E9)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete_image),
                        contentDescription = "Delete",
                        tint = Color(0xFFE17055),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = publishTime,
                    color = lightText,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit",
                        tint = accentColor,
                        modifier = Modifier.size(32.dp)
                    )
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


@Preview(showBackground = true)
@Composable
fun MyAdScreenPreview() {
    //MainScreen() {}
}
