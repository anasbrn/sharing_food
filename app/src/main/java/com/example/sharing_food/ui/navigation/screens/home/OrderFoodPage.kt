package com.example.sharing_food.ui.navigation.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sharing_food.Activity.data.model.Food
import com.example.sharing_food.Activity.data.model.Order
import com.example.sharing_food.Activity.data.model.User
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFoodPage(
    food: Food,
    currentUser: User,
    onOrderConfirmed: (Order) -> Unit,
    onBack: () -> Unit
) {
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var quantity by remember { mutableStateOf(1) }
    val latLang = remember { mutableStateOf<Double?>(null) }
    val latLng = remember { mutableStateOf<Double?>(null) }
    val location = food.snackBar?.location
    latLang.value = location?.latitude
    latLng.value = location?.longitude

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(latLang.value!!, latLng.value!!), 10f // Default San Francisco
        )
    }

    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Order ${food.name}") },
                navigationIcon = {
                    IconButton (onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button (
                onClick = {
                    if (selectedLocation != null) {
                        val order = Order(
                            id = UUID.randomUUID().toString(),
                            client = currentUser,
                            food = food,
                            producer = food.producer,
                            status = "Pending",
                            clientLocation = GeoPoint(
                                selectedLocation!!.latitude,
                                selectedLocation!!.longitude
                            ),
                            timestamp = Timestamp.now()
                        ).copy(
                            quantity = quantity,
                            totalPrice = quantity * food.price
                        )

                        onOrderConfirmed(order)
                    } else {
                        Toast.makeText(context, "Please select your location on the map", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(56.dp)
            ) {
                Text("Confirm Order", color = Color.White)
            }
        }
    ) { padding ->
        Column (
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Tap on the map to select delivery location", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedLocation = latLng
                }
            ) {
                selectedLocation?.let {
                    Marker(state = MarkerState(position = it), title = "Your Location")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Quantity:", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = {
                        quantity = it.toIntOrNull()?.coerceAtLeast(1) ?: 1
                    },
                    singleLine = true,
                    modifier = Modifier.width(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total: ${"%.2f".format(quantity * food.price)} DH",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
