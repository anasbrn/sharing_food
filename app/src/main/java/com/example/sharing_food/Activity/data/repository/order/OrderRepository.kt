package com.example.sharing_food.Activity.data.repository.order

import com.example.sharing_food.Activity.data.model.Order
import com.example.sharing_food.utils.Resource

interface OrderRepository {
    abstract suspend fun getUserOrders(userId: String): Resource<List<Order>>
    abstract suspend fun updateOrderStatus(orderId: String, newStatus: String)
    abstract suspend fun getProducerOrders(userId: String): Resource<List<Order>>
}
