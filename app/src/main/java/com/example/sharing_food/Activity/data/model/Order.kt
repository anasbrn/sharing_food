package com.example.sharing_food.Activity.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Order(
    val id: String = "",
    val clientId: String = "",
    val foodId: String = "",
    val producerId: String = "",
    val status: String = "", // e.g., "Pending", "Preparing", "Out for Delivery", "Delivered"
    val timestamp: Timestamp = Timestamp.now(),
    val clientLocation: GeoPoint = GeoPoint(0.0, 0.0)
)