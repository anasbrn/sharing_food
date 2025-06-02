package com.example.sharing_food.Activity.data.model

import com.google.firebase.firestore.GeoPoint

data class SnackBar(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0)
)