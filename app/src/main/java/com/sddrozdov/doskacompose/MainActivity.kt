package com.sddrozdov.doskacompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sddrozdov.doskacompose.presentation.navigations.BottomBar
import com.sddrozdov.doskacompose.presentation.navigations.MainNavigation
import com.sddrozdov.doskacompose.presentation.navigations.Routes
import com.sddrozdov.doskacompose.presentation.theme.DoskaComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoskaComposeTheme {

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

//                val showBottomBar = remember {
//                    derivedStateOf {
//                        when (navController.currentDestination?.route) {
//                            Screen.MainScreen.route,
//                            Screen.DialogsScreen.route,
//                            Screen.FilterScreen.route -> true
//
//                            else -> false
//                        }
//                    }
//                }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = when (currentRoute) {
                    Routes.MAIN,
                    Routes.DIALOGS,
                    Routes.FILTER -> true
                    else -> false
                }


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        if (showBottomBar) {
                            BottomBar(navController = navController, currentRoute = currentRoute)
                        }
                    }
                ) { innerPadding ->
                    MainNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navHostController = navController,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

//@Composable
//fun MainContent(
//    modifier: Modifier = Modifier,
//    snackbarHostState: SnackbarHostState
//) {
//    MainNavigation(
//        navHostController = rememberNavController(),
//        modifier = modifier,
//        snackbarHostState = snackbarHostState,
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    DoskaComposeTheme {
//        val snackbarHostState = remember { SnackbarHostState() }
//        MainContent(
//            modifier = Modifier,
//            snackbarHostState = snackbarHostState
//        )
//    }
//}