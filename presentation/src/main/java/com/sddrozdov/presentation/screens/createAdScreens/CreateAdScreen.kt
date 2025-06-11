//package com.sddrozdov.presentation.screens.createAdScreens
//
//import android.util.Log
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.sddrozdov.presentation.states.createAd.CreateAdEvents
//import com.sddrozdov.presentation.states.createAd.CreateAdStates
//import com.sddrozdov.presentation.viewModels.createAd.CreateAdViewModel
//import com.sddrozdov.presentation.R
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateAdScreen(
//    onNavigateTo: (String) -> Unit
//) {
//    val viewModel = hiltViewModel<CreateAdViewModel>()
//    val state by viewModel.state.collectAsState()
//
//    CreateAdScreenView(
//        state = state,
//        onEvent = viewModel::onEvent,
//        onNavigateTo = onNavigateTo
//    )
//}
//
//@Composable
//fun CreateAdScreenView(
//    state: CreateAdStates,
//    onEvent: (CreateAdEvents) -> Unit,
//    onNavigateTo: (String) -> Unit
//) {
//
//    val primaryBackground = Color(0xFFF7F9FC)
//    val cardBackground = Color.White
//    val accentColor = Color(0xFF6C5CE7)
//    val googleButtonColor = Color(0xFF4285F4)
//    val textColor = Color(0xFF2D3436)
//    val secondaryTextColor = Color(0xFF636E72)
//    val textFieldOutline = Color(0xFFDFE6E9)
//
//    val verticalScrollState = rememberScrollState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(verticalScrollState)
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.Gray)
//        ) {
//            Column(
//                modifier = Modifier.padding(12.dp)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                ) {
//                    Text("Image ViewPager Placeholder", modifier = Modifier.align(Alignment.Center))
//                }
//
//                FloatingActionButton(
//                    onClick = { onEvent(CreateAdEvents.OnImagesAdded) },
//                    containerColor = Color.Blue
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_edit),
//                        contentDescription = "Add Image"
//                    )
//                }
//            }
//        }
//
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Text("Country", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                TextButton(onClick = { onEvent(CreateAdEvents.OnCountrySelected("TEST COUNTRY")) }) {
//                    Text("Select Country", color = Color.Black)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text("City", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                TextButton(onClick = { onEvent(CreateAdEvents.OnCitySelected("TEST CITY")) }) {
//                    Text("Select City", color = Color.Black)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text("Email", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                OutlinedTextField(
//                    value = state.email,
//                    onValueChange = { onEvent(CreateAdEvents.OnEmailChanged(it)) },
//                    label = { Text("Email") },
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedTextColor = textColor,
//                        unfocusedTextColor = textColor,
//                        focusedLabelColor = accentColor,
//                        unfocusedLabelColor = secondaryTextColor,
//                        focusedIndicatorColor = accentColor,
//                        unfocusedIndicatorColor = textFieldOutline,
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text("Phone", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                OutlinedTextField(
//                    value = state.phone,
//                    onValueChange = { onEvent(CreateAdEvents.OnPhoneChanged(it)) },
//                    label = { Text("Phone Number") },
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedTextColor = textColor,
//                        unfocusedTextColor = textColor,
//                        focusedLabelColor = accentColor,
//                        unfocusedLabelColor = secondaryTextColor,
//                        focusedIndicatorColor = accentColor,
//                        unfocusedIndicatorColor = textFieldOutline,
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text("Index", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                OutlinedTextField(
//                    value = state.index,
//                    onValueChange = { onEvent(CreateAdEvents.OnIndexChanged(it)) },
//                    label = { Text("Index") },
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedTextColor = textColor,
//                        unfocusedTextColor = textColor,
//                        focusedLabelColor = accentColor,
//                        unfocusedLabelColor = secondaryTextColor,
//                        focusedIndicatorColor = accentColor,
//                        unfocusedIndicatorColor = textFieldOutline,
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("Title", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                OutlinedTextField(
//                    value = state.title,
//                    onValueChange = { onEvent(CreateAdEvents.OnTitleChanged(it)) },
//                    label = { Text("Title") },
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedTextColor = textColor,
//                        unfocusedTextColor = textColor,
//                        focusedLabelColor = accentColor,
//                        unfocusedLabelColor = secondaryTextColor,
//                        focusedIndicatorColor = accentColor,
//                        unfocusedIndicatorColor = textFieldOutline,
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text("Category", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                TextButton(onClick = { onEvent(CreateAdEvents.OnCategorySelected(1)) }) {
//                    Text("Select Category", color = Color.Black)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text("Price", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                OutlinedTextField(
//                    value = state.price,
//                    onValueChange = { onEvent(CreateAdEvents.OnPriceChanged(it)) },
//                    label = { Text("Price") },
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedTextColor = textColor,
//                        unfocusedTextColor = textColor,
//                        focusedLabelColor = accentColor,
//                        unfocusedLabelColor = secondaryTextColor,
//                        focusedIndicatorColor = accentColor,
//                        unfocusedIndicatorColor = textFieldOutline,
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text("Description", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                OutlinedTextField(
//                    value = state.description,
//                    onValueChange = { onEvent(CreateAdEvents.OnDescriptionChanged(it)) },
//                    label = { Text("Description") },
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedTextColor = textColor,
//                        unfocusedTextColor = textColor,
//                        focusedLabelColor = accentColor,
//                        unfocusedLabelColor = secondaryTextColor,
//                        focusedIndicatorColor = accentColor,
//                        unfocusedIndicatorColor = textFieldOutline,
//                    ),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(100.dp),
//                    maxLines = 5
//                )
//            }
//        }
//
//        Button(
//            onClick = { onEvent(CreateAdEvents.OnPublishClicked)
//                Log.e("CreateAdScreen", "Clicked")
//                      },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
//        ) {
//            Text("Publish", color = Color.White)
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewCreateAdScreen() {
//    val fakeState = CreateAdStates(
//        email = "",
//        phone = "",
//        index = "",
//        title = "",
//        price = "",
//        description = ""
//    )
//
//    CreateAdScreenView(
//        state = fakeState,
//        onEvent = {},
//        onNavigateTo = {}
//    )
//}
//
//
