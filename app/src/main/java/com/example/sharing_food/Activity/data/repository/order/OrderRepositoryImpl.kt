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
            val snapshot =
                firestore.collection("orders")
                    .whereEqualTo("client.uid", userId)
                    .get()
                    .await()
            val list = snapshot.toObjects(Order::class.java)
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected error")
        }
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        val orderRef = firestore.collection("orders").document(orderId)
        orderRef.update("status", newStatus).await()
    }

    override suspend fun getProducerOrders(userId: String): Resource<List<Order>> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("producer.uid", userId)
                .get()
                .await()

            val orders = snapshot.toObjects(Order::class.java)
            Resource.Success(orders)
        } catch (e: Exception) {
            Resource.Error("Failed to load orders: ${e.message}")
        }
    }
}
