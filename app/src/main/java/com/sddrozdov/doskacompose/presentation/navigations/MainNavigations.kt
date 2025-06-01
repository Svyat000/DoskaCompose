package com.sddrozdov.doskacompose.presentation.navigations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sddrozdov.doskacompose.presentation.screens.LoginScreen
import com.sddrozdov.doskacompose.presentation.screens.MainScreen
import com.sddrozdov.doskacompose.presentation.screens.RegisterScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable data object LoginScreen : Screen(Routes.LOGIN)
    @Serializable data object RegisterScreen : Screen(Routes.REGISTER)
    @Serializable data object MainScreen : Screen(Routes.MAIN)
    @Serializable data object FilterScreen : Screen(Routes.FILTER)
    @Serializable data object DialogsScreen : Screen(Routes.DIALOGS)
    @Serializable data object ChatScreen : Screen(Routes.CHAT)
    @Serializable data object CreateAdScreen : Screen(Routes.CREATE_AD)
    @Serializable data object DescriptionAdScreen : Screen(Routes.DESCRIPTION_AD)
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Screen.RegisterScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)

                }, snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)
                },
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.MainScreen.route) {
            MainScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)
                }
            )
        }
        composable<Screen.FilterScreen> { }
        composable<Screen.DialogsScreen> { }
        composable<Screen.ChatScreen> { }
        composable<Screen.CreateAdScreen> { }
        composable<Screen.DescriptionAdScreen> { }
    }
}

object Routes {
    const val LOGIN = "login_screen"
    const val REGISTER = "register_screen"
    const val MAIN = "main_screen"
    const val FILTER = "filter_screen"
    const val DIALOGS = "dialogs_screen"
    const val CHAT = "chat_screen"
    const val CREATE_AD = "create_ad_screen"
    const val DESCRIPTION_AD = "description_ad_screen"
}