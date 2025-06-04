package com.example.sharing_food.ui.navigation.screens.home

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sharing_food.Activity.data.model.Food
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailPage(
    food: Food,
    onBack: () -> Unit,
    onReserveClick: (Food) -> Unit = {}
) {
    val snackBarName = remember { mutableStateOf<String?>(null) }
    val snackBarImage = remember { mutableStateOf<String?>(null) }
    val snackBarLat = remember { mutableStateOf<Double?>(null) }
    val snackBarLng = remember { mutableStateOf<Double?>(null) }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(food.snackBar) {
        val snackBarRef = food.snackBar?.let {
            FirebaseFirestore.getInstance().collection("snackbars").document(it.id)
        }
        val snapshot = snackBarRef?.get()?.await()
        snackBarName.value = snapshot?.getString("name")
        snackBarImage.value = snapshot?.getString("imageUrl")
        val location = snapshot?.getGeoPoint("location")
        snackBarLat.value = location?.latitude
        snackBarLng.value = location?.longitude
    }

    LaunchedEffect(snackBarLat.value, snackBarLng.value) {
        val lat = snackBarLat.value
        val lng = snackBarLng.value
        if (lat != null && lng != null) {
            val location = LatLng(lat, lng)
            val cameraPosition = CameraPosition.fromLatLngZoom(location, 15f)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            cameraPositionState.move(cameraUpdate)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = food.name, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp) // Add padding to prevent content behind button
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                AsyncImage(
                    model = food.imageUrl,
                    contentDescription = "Food Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${food.price} DH",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = food.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Provided by: ${snackBarName.value ?: "Loading..."}",
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (snackBarLat.value != null && snackBarLng.value != null) {
                    val location = LatLng(snackBarLat.value!!, snackBarLng.value!!)
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = MarkerState(position = location),
                            title = snackBarName.value ?: "Snack Bar"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Fixed bottom button
            Button(
                onClick = { onReserveClick(food) },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Order", color = Color.White)
            }
        }
    }
}
