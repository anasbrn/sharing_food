package com.example.sharing_food.Activity.data.model

data class Food(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val categoryId: String = "",
    val producerId: String = "",
    val snackBarId: String = ""
)