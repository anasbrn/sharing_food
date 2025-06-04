package com.example.sharing_food.ui.navigation.screens.home

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order ${food.name}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
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
                        Toast.makeText(
                            context,
                            "Please select your location on the map",
                            Toast.LENGTH_SHORT
                        ).show()
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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                "Tap on the map to select delivery location",
                style = MaterialTheme.typography.bodyLarge
            )

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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                var quantity by remember { mutableStateOf(1) }

                QuantitySelector(
                    quantity = quantity,
                    onQuantityChange = { quantity = it }
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

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Quantity:", style = MaterialTheme.typography.bodyLarge)

        CircleTextButton(
            symbol = "−",
            onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }
        )

        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleMedium
        )

        CircleTextButton(
            symbol = "+",
            onClick = { onQuantityChange(quantity + 1) }
        )
    }
}

@Composable
fun CircleTextButton(
    symbol: String,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer, // background color
        modifier = Modifier
            .size(36.dp)
            .clickable { onClick() },
    ) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

