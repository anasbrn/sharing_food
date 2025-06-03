package com.example.sharing_food.ui.navigation.screens.home

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailPage(
    food: Food,
    onBack: () -> Unit
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
                title = { Text(text = food.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            snackBarImage.value?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Snack Bar Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text("Snack Bar: ${snackBarName.value ?: "Loading..."}")
            Spacer(modifier = Modifier.height(16.dp))

            snackBarLat.value?.let { lat ->
                snackBarLng.value?.let { lng ->
                    val location = LatLng(lat, lng)
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = MarkerState(position = location),
                            title = snackBarName.value ?: "SnackBar Location"
                        )
                    }
                }
            }
        }
    }
}
