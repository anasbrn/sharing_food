package com.example.sharing_food.ui.navigation.screens.order

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sharing_food.Activity.data.model.Order
import com.example.sharing_food.Activity.data.repository.order.OrderRepositoryImpl
import com.example.sharing_food.Activity.data.repository.user.UserRepository
import com.example.sharing_food.ViewModel.OrderViewModel
import com.example.sharing_food.ui.components.global.SearchBar
import com.example.sharing_food.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderPage() {
    val context = LocalContext.current

    val firestore = remember { FirebaseFirestore.getInstance() }
    val orderRepo = remember { OrderRepositoryImpl(firestore) }
    val userRepo = remember { UserRepository() }
    val viewModel = remember { OrderViewModel(orderRepo, userRepo) }

    val orders by remember { derivedStateOf { viewModel.filteredOrders } }
    val orderState = viewModel.orderState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders") }
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
                            OrderItem(order)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Food: ${order.food.name}", style = MaterialTheme.typography.titleMedium)
            Text("Status: ${order.status}", color = when (order.status) {
                "Pending" -> Color.Yellow
                "Approved" -> Color.Green
                "Delivered" -> Color.Blue
                else -> Color.Gray
            })
            Text("Date: ${order.timestamp.toDate()}", style = MaterialTheme.typography.bodySmall)
        }
    }
}


