package com.example.sharing_food.ui.navigation.screens.order

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sharing_food.Activity.data.model.Order
import com.example.sharing_food.Activity.data.model.User
import com.example.sharing_food.Activity.data.repository.order.OrderRepositoryImpl
import com.example.sharing_food.Activity.data.repository.user.UserRepository
import com.example.sharing_food.ViewModel.OrderViewModel
import com.example.sharing_food.ui.components.global.SearchBar
import com.example.sharing_food.utils.Resource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderPage() {
    val context = LocalContext.current
    val firestore = remember { FirebaseFirestore.getInstance() }
    val orderRepo = remember { OrderRepositoryImpl(firestore) }
    val userRepo = remember { UserRepository() }
    var currentUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        currentUser = userRepo.getCurrentUser()
    }
    val viewModel = remember { OrderViewModel(orderRepo, userRepo) }

    var orderToShowLocation by remember { mutableStateOf<Order?>(null) }
    val orders by remember { derivedStateOf { viewModel.filteredOrders } }
    val orderState = viewModel.orderState

    fun updateOrderStatus(order: Order, status: String) {
        viewModel.updateOrderStatus(order, status) { success, message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(orderState) {
        if (orderState is Resource.Error) {
            Toast.makeText(context, orderState.message, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            SearchBar(
                query = viewModel.searchQuery,
                onQueryChanged = viewModel::updateSearchQuery
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (orderState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is Resource.Error -> {
                    Toast.makeText(context, orderState.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Success -> {
                    LazyColumn {
                        items(orders) { order ->
                            val isProducer = currentUser?.uid == order.producer.uid
                            OrderItem(
                                order = order,
                                isProducer = isProducer,
                                onConfirm = { updateOrderStatus(it, "Confirmed") },
                                onReject = { updateOrderStatus(it, "Rejected") },
                                onPreparing = { updateOrderStatus(it, "Preparing") },
                                onDeliver = { updateOrderStatus(it, "Delivred") },
                                onShowLocation = { orderToShowLocation = it }
                            )
                        }
                    }
                }
            }
        }
    }

    orderToShowLocation?.let { order ->
        val location = order.clientLocation
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLng, 15f)
        }

        AlertDialog(
            onDismissRequest = { orderToShowLocation = null },
            title = { Text("User Location") },
            text = {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = latLng),
                        title = "User Location"
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { orderToShowLocation = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun OrderItem(
    order: Order,
    isProducer: Boolean,
    onConfirm: (Order) -> Unit,
    onReject: (Order) -> Unit,
    onPreparing: (Order) -> Unit,
    onDeliver: (Order) -> Unit,
    onShowLocation: (Order) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Food: ${order.food.name}", style = MaterialTheme.typography.titleMedium)
            Text(
                "Status: ${order.status}",
                color = when (order.status) {
                    "Pending" -> Color.Yellow
                    "Confirmed" -> Color.Green
                    "Rejected" -> Color.Red
                    "Preparing" -> Color.Cyan
                    "Delivered" -> Color.Blue
                    else -> Color.Gray
                }
            )
            Text("Date: ${order.timestamp.toDate()}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isProducer) {
                    when (order.status) {
                        "Pending" -> {
                            Button(onClick = { onConfirm(order) }, modifier = Modifier.weight(1f)) {
                                Text("Confirm")
                            }
                            Button(onClick = { onReject(order) }, modifier = Modifier.weight(1f)) {
                                Text("Reject")
                            }
                        }

                        "Confirmed" -> {
                            Button(
                                onClick = { onPreparing(order) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Preparing")
                            }
                        }

                        "Preparing" -> {
                            Button(onClick = { onDeliver(order) }, modifier = Modifier.weight(1f)) {
                                Text("Delivered")
                            }
                        }

                        else -> {
                            // No buttons for Delivered, Rejected, or other statuses
                        }
                    }
                    Button(
                        onClick = { onShowLocation(order) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = "Show User Location"
                        )
                    }
                }
            }
        }
    }
}
