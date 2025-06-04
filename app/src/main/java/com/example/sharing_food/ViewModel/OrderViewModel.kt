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

    init {
        fetchUserOrders()
    }

    private fun fetchUserOrders() {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser()
            currentUser?.let {
                orderState = repository.getUserOrders(it.uid)
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
