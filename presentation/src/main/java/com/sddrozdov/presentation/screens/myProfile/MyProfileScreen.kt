package com.sddrozdov.presentation.screens.myProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sddrozdov.presentation.navigations.Screen
import com.sddrozdov.presentation.states.myProfile.MyProfileState
import com.sddrozdov.presentation.states.myProfile.ProfileScreenEvent
import com.sddrozdov.presentation.viewModels.myProfile.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateTo: (String) -> Unit,
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsState()

    ProfileView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo,
    )
}

@Composable
fun ProfileView(
    state: MyProfileState,
    onEvent: (ProfileScreenEvent) -> Unit,
    onNavigateTo: (String) -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profile Screen", modifier = Modifier.padding(bottom = 32.dp))

            Button(
                onClick = { onNavigateTo(Screen.LoginScreen.route) },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Go to Authorization")
            }

            Button(
                onClick = {onEvent(ProfileScreenEvent.Logout)}
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {

    val fakeState = MyProfileState()

    ProfileView(
        onNavigateTo = {},
        onEvent = {},
        state = fakeState,
    )
}