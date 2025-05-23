package com.sddrozdov.doskacompose.presentation.navigations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object RegisterScreen : Screen()

    @Serializable
    data object MainScreen : Screen()

    @Serializable
    data object FilterScreen : Screen()

    @Serializable
    data object DialogsScreen : Screen()

    @Serializable
    data object ChatScreen : Screen()

    @Serializable
    data object CreateAdScreen : Screen()

    @Serializable
    data object DescriptionAdScreen : Screen()
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Screen.MainScreen
    ) {
        composable<Screen.LoginScreen> {  }
        composable<Screen.RegisterScreen> {  }
        composable<Screen.MainScreen> {  }
        composable<Screen.FilterScreen> {  }
        composable<Screen.DialogsScreen> {  }
        composable<Screen.ChatScreen> {  }
        composable<Screen.CreateAdScreen> {  }
        composable<Screen.DescriptionAdScreen> {  }


    }


}