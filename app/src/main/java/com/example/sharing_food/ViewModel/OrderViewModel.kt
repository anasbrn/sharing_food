package com.example.sharing_food.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharing_food.Activity.data.model.Order
import com.example.sharing_food.Activity.data.repository.order.OrderRepository
import com.example.sharing_food.utils.Resource
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.sharing_food.Activity.data.repository.user.UserRepository

class OrderViewModel(
    private val repository: OrderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var orderState by mutableStateOf<Resource<List<Order>>>(Resource.Loading)
        private set

    private var currentUserRole: String? = null
    private var currentUserId: String? = null

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                val currentUser = userRepository.getCurrentUser()
                currentUserRole = currentUser?.role
                currentUserId = currentUser?.uid
                when (currentUserRole) {
                    "producer" -> {
                        orderState = repository.getProducerOrders(currentUserId!!)
                    }

                    "client" -> {
                        orderState = repository.getUserOrders(currentUserId!!)
                    }

                    else -> {
                        orderState = Resource.Error("Unknown user role or user not logged in")
                    }
                }
            } catch (e: Exception) {
                orderState = Resource.Error("Failed to fetch orders: ${e.message}")
            }
        }
    }

    private fun fetchOrders() {
        if (currentUserRole != "producer" || currentUserId == null) {
            return
        }
        viewModelScope.launch {
            try {
                orderState = repository.getProducerOrders(currentUserId!!)
            } catch (e: Exception) {
                orderState = Resource.Error("Failed to fetch producer orders: ${e.message}")
            }
        }
    }

    fun updateOrderStatus(order: Order, newStatus: String, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.updateOrderStatus(order.id, newStatus)
                onComplete(true, "Order status updated")
                fetchOrders()
            } catch (e: Exception) {
                onComplete(false, "Failed to update order: ${e.message}")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    val filteredOrders: List<Order>
        get() = when (val state = orderState) {
            is Resource.Success -> state.data.filter {
                it.food.name.contains(searchQuery, ignoreCase = true)
            }

            else -> emptyList()
        }
}

