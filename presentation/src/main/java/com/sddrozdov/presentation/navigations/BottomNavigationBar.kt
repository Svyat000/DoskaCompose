package com.sddrozdov.presentation.navigations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.sddrozdov.presentation.R

@Composable
fun BottomBar(navController: NavHostController, currentRoute: String?) {
    val screens = listOf(
        Screen.MainScreen,
        Screen.FavoriteAdScreen,
        Screen.SelectCategoryScreen,
        Screen.MyAdScreen,
        Screen.MyProfileScreen,
    )

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screen.MainScreen -> Icons.Default.Home
                            Screen.FavoriteAdScreen -> Icons.Default.Star
                            Screen.SelectCategoryScreen -> Icons.Default.Add
                            Screen.MyAdScreen -> Icons.Default.MailOutline
                            Screen.MyProfileScreen -> Icons.Default.Star
                            else -> Icons.Default.Star
                        },
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(getLabelRes(screen))) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun getLabelRes(screen: Screen): Int {
    return when (screen) {
        Screen.MainScreen -> R.string.main_screen
        Screen.FavoriteAdScreen -> R.string.favorite_ad
        Screen.SelectCategoryScreen -> R.string.create_ad
        Screen.MyAdScreen -> R.string.my_ad
        Screen.MyProfileScreen -> R.string.profile
        else -> R.string.unknown
    }
}