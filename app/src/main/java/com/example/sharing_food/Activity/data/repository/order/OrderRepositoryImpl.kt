package com.example.sharing_food.Activity.data.repository.order

import com.example.sharing_food.Activity.data.model.Order
import com.example.sharing_food.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl(
    private val firestore: FirebaseFirestore
) : OrderRepository {
    override suspend fun getUserOrders(userId: String): Resource<List<Order>> {
        return try {
            val snapshot = firestore.collection("orders").whereEqualTo("client.uid", userId).get().await()
            val list = snapshot.toObjects(Order::class.java)
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected error")
        }
    }
}
