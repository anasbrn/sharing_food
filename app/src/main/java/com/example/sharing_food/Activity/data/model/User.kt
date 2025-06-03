package com.example.sharing_food.Activity.data.model

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val role: String = "", // You can also use an enum if roles are limited (e.g., "Client", "Producer", etc.)
    val favorites: List<String> = emptyList()
)
