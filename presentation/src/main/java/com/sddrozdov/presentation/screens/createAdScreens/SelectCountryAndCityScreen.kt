package com.sddrozdov.presentation.screens.createAdScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.createAd.Country
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import com.sddrozdov.presentation.viewModels.createAd.CreateAdViewModel

@Composable
fun SelectCountryAndCityScreen(
    navHostController: NavHostController,
    viewModel: CreateAdViewModel
) {

    val state by viewModel.state.collectAsState()

    SelectCountryAndCityView(
        state = state,
        onEvent = viewModel::onEvent,
        navHostController = navHostController
    )
}

@Composable
fun SelectCountryAndCityView(
    state: CreateAdStates,
    onEvent: (CreateAdEvents) -> Unit,
    navHostController: NavHostController
) {
    val colorScheme = MaterialTheme.colorScheme

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
                    text = "Укажите местоположение",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = colorScheme.onSurface
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
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    if (state.selectedCity != null) {
                        Text(
                            text = "Город: ${state.selectedCity}",
                            fontSize = 16.sp,
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold
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
                            containerColor = if (!state.isCountrySelectionVisible) {
                                colorScheme.primary
                            } else {
                                colorScheme.onSurfaceVariant
                            },
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(text = stringResource(id = R.string.countries), fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { onEvent(CreateAdEvents.ShowCitySelection) },
                        enabled = state.selectedCountry != null && !state.isCitySelectionVisible,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.selectedCountry != null && !state.isCitySelectionVisible) {
                                colorScheme.primary
                            } else {
                                colorScheme.onSurfaceVariant
                            },
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(text = stringResource(id = R.string.cities), fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    when {
                        state.isCountrySelectionVisible -> {
                            CountrySelectionList(
                                countries = state.countries,
                                selectedCountry = state.selectedCountry,
                                onCountrySelected = { onEvent(CreateAdEvents.OnCountrySelected(it)) },
                            )
                        }

                        state.isCitySelectionVisible && state.selectedCountry != null -> {
                            CitySelectionList(
                                cities = state.selectedCountry.cities,
                                selectedCity = state.selectedCity,
                                onCitySelected = { onEvent(CreateAdEvents.OnCitySelected(it)) },
                            )
                        }
                    }
                }
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
                    if (state.selectedCity != null) {
                        onEvent(
                            CreateAdEvents.OnCountryAndCitySelected(
                                true,
                                state.selectedCountry!!,
                                state.selectedCity
                            )
                        )
                        navHostController.navigate(Screen.EditAdTitleScreen.route)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.selectedCity != null) {
                        colorScheme.primary
                    } else {
                        colorScheme.onSurfaceVariant
                    },
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                enabled = state.selectedCity != null
            ) {
                Text(
                    text = stringResource(id = R.string.next_screen),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CountrySelectionList(
    countries: List<Country>,
    selectedCountry: Country?,
    onCountrySelected: (Country) -> Unit,
) {
    LazyColumn {
        items(countries) { country ->
            CountryCityItem(
                name = country.name,
                isSelected = selectedCountry?.name == country.name,
                onClick = { onCountrySelected(country) },
            )
        }
    }
}

@Composable
private fun CitySelectionList(
    cities: List<String>,
    selectedCity: String?,
    onCitySelected: (String) -> Unit,
) {
    LazyColumn {
        items(cities) { city ->
            CountryCityItem(
                name = city,
                isSelected = selectedCity == city,
                onClick = { onCitySelected(city) },
            )
        }
    }
}

@Composable
private fun CountryCityItem(
    name: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                colors.primary
            } else {
                colors.surface
            }
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) colors.primary
            else colors.outline
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) colors.primary
            else colors.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectCountryAndCityScreenPreview(
) {
    val fakeNavHostController = rememberNavController()
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
        navHostController = fakeNavHostController
    )
}