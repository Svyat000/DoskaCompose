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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.sddrozdov.doskacompose.presentation.navigations.MainNavigation
import com.sddrozdov.doskacompose.presentation.theme.DoskaComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoskaComposeTheme {

                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {
    MainNavigation(
        navHostController = rememberNavController(),
        modifier = modifier,
        snackbarHostState = snackbarHostState ,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoskaComposeTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        MainContent(
            modifier = Modifier,
            snackbarHostState = snackbarHostState
        )
    }
}