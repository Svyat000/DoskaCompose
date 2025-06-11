package com.sddrozdov.presentation.screens.createAdScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.doskacompose.domain.models.Category
import com.sddrozdov.presentation.AppColors
import com.sddrozdov.presentation.R
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.createAd.CreateAdEvents
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import com.sddrozdov.presentation.viewModels.createAd.CreateAdViewModel


@Composable
fun SelectCategoryScreen(
    onNavigateTo: (String) -> Unit,
) {
    val viewModel = hiltViewModel<CreateAdViewModel>()
    val state by viewModel.state.collectAsState()

//    LaunchedEffect(state.selectedCategoryId) {
//        state.selectedCategoryId?.let {
//            onNavigateTo(Screen.SelectCountryAndCityScreen.route)
//        }
//    }

    SelectCategoryView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo

    )
}

@Composable
fun SelectCategoryView(
    state: CreateAdStates,
    onEvent: (CreateAdEvents) -> Unit,
    onNavigateTo: (String) -> Unit
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
                Text(
                    text = stringResource(R.string.select_category),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.textColor,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    state.isLoading -> LoadingIndicator()
                    state.error != null -> ErrorState(message = state.error)
                    else -> CategoryGrid(
                        categories = state.categories.take(10),
                        onCategorySelected = { categoryId ->
                            onEvent(CreateAdEvents.OnCategorySelected(categoryId))
                        },
                    )
                }

            }
        }
        Button(
            onClick = {
                state.selectedCategoryId?.let {
                    onNavigateTo(Screen.SelectCountryAndCityScreen.route)
                }
            }, enabled = state.selectedCategoryId != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.accentColor,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(id = R.string.next_screen), fontSize = 16.sp)
        }
    }
}

@Composable
fun CategoryGrid(
    categories: List<Category>,
    onCategorySelected: (Long) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.heightIn(max = (5 * 100).dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onClick = { onCategorySelected(category.id.toLong()) },
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.cardBackground),
        border = BorderStroke(1.dp, AppColors.accentColor)
    ) {
        Text(
            text = category.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.textColor,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AppColors.accentColor)
    }
}

@Composable
fun ErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Red,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectCategoryViewPreview() {
    val sampleCategories = listOf(
        Category(1, "Категория 1"),
        Category(2, "Категория 2"),
        Category(3, "Категория 3"),
        Category(4, "Категория 4"),
        Category(5, "Категория 5")
    )

    val sampleState = CreateAdStates(
        categories = sampleCategories,
        isLoading = false,
        error = null,
        selectedCategoryId = null
    )

    SelectCategoryView(
        state = sampleState,
        onEvent = { },
        onNavigateTo = { }
    )
}