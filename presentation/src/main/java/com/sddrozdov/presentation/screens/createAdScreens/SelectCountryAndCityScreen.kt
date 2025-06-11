package com.sddrozdov.presentation.screens.createAdScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.domain.models.Country
import com.sddrozdov.presentation.AppColors
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import com.sddrozdov.presentation.viewModels.createAd.CreateAdViewModel


@Composable
fun SelectCountryAndCityScreen(
    onNavigateTo: (String) -> Unit
) {

    val viewModel: CreateAdViewModel = hiltViewModel<CreateAdViewModel>()
    val state by viewModel.state.collectAsState()

//        LaunchedEffect(state.selectedCategoryId) {
//            state.selectedCategoryId?.let { categoryId ->
//                onNavigateTo("some_route_for_category_$categoryId")
//            }
//        }

    SelectCountryAndCityView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo

    )
}

@Composable
fun SelectCountryAndCityView(
    state: CreateAdStates,
    onEvent: (CreateAdEvents) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.primaryBackground)
            .padding(16.dp)
    )
    {
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
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Укажите местоположение",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = AppColors.textColor
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.selectedCountry != null) {
                        Text(
                            text = "Страна: ${state.selectedCountry.name}",
                            fontSize = 16.sp,
                            color = AppColors.textColor,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    if (state.selectedCity != null) {
                        Text(
                            text = "Город: ${state.selectedCity}",
                            fontSize = 16.sp,
                            color = AppColors.textColor
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onEvent(CreateAdEvents.ShowCountrySelection) },
                        enabled = !state.isCountrySelectionVisible,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!state.isCountrySelectionVisible) AppColors.accentColor else AppColors.secondaryTextColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Страны", fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { onEvent(CreateAdEvents.ShowCitySelection) },
                        enabled = state.selectedCountry != null && !state.isCitySelectionVisible,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.selectedCountry != null && !state.isCitySelectionVisible)
                                AppColors.accentColor else AppColors.secondaryTextColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Города", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    when {
                        state.isCountrySelectionVisible -> {
                            CountrySelectionList(
                                countries = state.countries,
                                onCountrySelected = { onEvent(CreateAdEvents.OnCountrySelected(it)) },
                            )
                        }

                        state.isCitySelectionVisible && state.selectedCountry != null -> {
                            CitySelectionList(
                                cities = state.selectedCountry.cities,
                                onCitySelected = { onEvent(CreateAdEvents.OnCitySelected(it)) },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (state.selectedCity != null) {
                            onEvent(CreateAdEvents.OnCountryAndCitySelected(true))
                            onNavigateTo(Screen.MainScreen.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.accentColor,
                        contentColor = Color.White
                    ),
                    enabled = state.selectedCity != null
                ) {
                    Text(text = "Подтвердить", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun CountrySelectionList(
    countries: List<Country>,
    onCountrySelected: (Country) -> Unit,
) {
    LazyColumn {
        items(countries) { country ->
            CountryCityItem(
                name = country.name,
                onClick = { onCountrySelected(country) },
            )
        }
    }
}

@Composable
private fun CitySelectionList(
    cities: List<String>,
    onCitySelected: (String) -> Unit,
) {
    LazyColumn {
        items(cities) { city ->
            CountryCityItem(
                name = city,
                onClick = { onCitySelected(city) },
            )
        }
    }
}

@Composable
private fun CountryCityItem(
    name: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = AppColors.textColor
        ),
        border = BorderStroke(1.dp, AppColors.accentColor.copy(alpha = 0.3f))
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectCountryAndCityScreenPreview() {
    val fakeState = CreateAdStates(
        countries = listOf(
            Country(name = "Россия", cities = listOf("Москва", "Санкт-Петербург", "Казань")),
            Country(name = "Беларусь", cities = listOf("Минск", "Гомель", "Витебск")),
        ),
        selectedCountry = Country(name = "Россия", cities = listOf("Москва", "Санкт-Петербург")),
        selectedCity = "Москва",
        isCountrySelectionVisible = false,
        isCitySelectionVisible = false
    )

    SelectCountryAndCityView(
        state = fakeState,
        onEvent = { },
        onNavigateTo = { }
    )
}