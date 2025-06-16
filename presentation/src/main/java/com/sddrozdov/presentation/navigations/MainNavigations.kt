package com.sddrozdov.presentation.navigations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
//import com.sddrozdov.presentation.screens.createAdScreens.CreateAdScreen
import com.sddrozdov.presentation.screens.DescriptionAdScreen
import com.sddrozdov.presentation.screens.DialogsScreen
import com.sddrozdov.presentation.screens.FavoriteAdScreen
import com.sddrozdov.presentation.screens.FilterScreen
import com.sddrozdov.presentation.screens.signInSignOut.LoginScreen
import com.sddrozdov.presentation.screens.MainScreen
import com.sddrozdov.presentation.screens.signInSignOut.RegisterScreen
import com.sddrozdov.presentation.screens.createAdScreens.EditAdDescriptionScreen
import com.sddrozdov.presentation.screens.createAdScreens.EditAdTitleScreen
import com.sddrozdov.presentation.screens.createAdScreens.SelectCategoryScreen
import com.sddrozdov.presentation.screens.createAdScreens.SelectCountryAndCityScreen
import com.sddrozdov.presentation.screens.myProfile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {

    @Serializable
    data object LoginScreen : Screen(Routes.LOGIN)

    @Serializable
    data object RegisterScreen : Screen(Routes.REGISTER)

    @Serializable
    data object MainScreen : Screen(Routes.MAIN)

    @Serializable
    data object FilterScreen : Screen(Routes.FILTER)

    @Serializable
    data object DialogsScreen : Screen(Routes.DIALOGS)

    @Serializable
    data object ChatScreen : Screen(Routes.CHAT)

    @Serializable
    data object CreateAdScreen : Screen(Routes.CREATE_AD)

    @Serializable
    data object MyProfileScreen : Screen(Routes.MY_PROFILE_SCREEN)

    @Serializable
    data object DescriptionAdScreen : Screen(Routes.DESCRIPTION_AD)

    @Serializable
    data object FavoriteAdScreen : Screen(Routes.FAVORITE_AD)

    @Serializable
    data object SelectCategoryScreen : Screen(Routes.SELECT_CATEGORY_AD)

    @Serializable
    data object SelectCountryAndCityScreen : Screen(Routes.SELECT_COUNTRY_AND_CITY_AD)

    @Serializable
    data object EditAdTitleScreen : Screen(Routes.EDIT_TITLE_SCREEN_AD)

    @Serializable
    data object EditAdDescriptionScreen : Screen(Routes.EDIT_DESCRIPTION_SCREEN_AD)


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
        startDestination = Screen.MainScreen.route
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
        composable(Screen.FilterScreen.route) {
            FilterScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)
                }
            )
        }
        composable(Screen.DialogsScreen.route) {
            DialogsScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)
                }
            )
        }
        composable<Screen.ChatScreen> { }

//        composable(Screen.CreateAdScreen.route) {
//            CreateAdScreen(
//                onNavigateTo = { route ->
//                    navHostController.navigate(route)
//                }
//            )
//        }

        composable(Screen.DescriptionAdScreen.route) {
            DescriptionAdScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)
                }
            )
        }

        composable(Screen.MyProfileScreen.route) {
            ProfileScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)
                }
            )
        }
        composable(Screen.FavoriteAdScreen.route) {
            FavoriteAdScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)
                }
            )
        }
        composable(Screen.SelectCategoryScreen.route) {
            SelectCategoryScreen(
                onNavigateTo = { route ->
                    navHostController.navigate(route)
                }
            )
        }
        composable(Screen.SelectCountryAndCityScreen.route) {
            SelectCountryAndCityScreen(navHostController = navHostController)
        }
        composable(Screen.EditAdTitleScreen.route) {
            EditAdTitleScreen(navHostController = navHostController)
        }
        composable(Screen.EditAdDescriptionScreen.route) {
            EditAdDescriptionScreen(navHostController = navHostController)
        }
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
    const val FAVORITE_AD = "favorite_ad_screen"
    const val SELECT_CATEGORY_AD = "select_category_ad_screen"
    const val SELECT_COUNTRY_AND_CITY_AD = "select_country_and_city_ad_screen"
    const val EDIT_TITLE_SCREEN_AD = "edit_title_screen_ad"
    const val EDIT_DESCRIPTION_SCREEN_AD = "edit_description_screen_ad"
    const val MY_PROFILE_SCREEN = "my_profile_screen"


}