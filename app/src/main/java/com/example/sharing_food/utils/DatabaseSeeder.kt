package com.example.sharing_food.utils

import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.Activity.data.model.Food
import com.example.sharing_food.Activity.data.model.Order
import com.example.sharing_food.Activity.data.model.SnackBar
import com.example.sharing_food.Activity.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

object DatabaseSeeder {
    fun seedFirestoreDatabase() {
        val db = FirebaseFirestore.getInstance()

        val categories = listOf(
            Category("1", "Pizza", "https://example.com/pizza.jpg"),
            Category("2", "Drinks", "https://example.com/drinks.jpg"),
            Category("3", "Desserts", "https://example.com/desserts.jpg")
        )
        categories.forEach { db.collection("categories").document(it.id).set(it) }

        val snackBar = SnackBar("1", "City Snack Bar", "https://example.com/desserts.jpg", GeoPoint(37.7749, -122.4194))
        db.collection("snackbars").document(snackBar.id).set(snackBar)

        val pizza4Saison = Food(
            "1",
            "Pizza 4 Saison",
            "This is Pizza 4 Saison",
            "https://example.com/pizza.jpg",
            40.50,
            "1",
            "838db3bc-3b17-423a-907c-3005e29b802d",
            "1"
        )
        db.collection("foods").document(pizza4Saison.id).set(pizza4Saison)

        val producer = User(
            "838db3bc-3b17-423a-907c-3005e29b802d",
            "John the Cook",
            "john@snack.com",
            "producer"
        )
        val client = User(
            "a71b5a37-54bf-4c87-8380-77fce01c7489",
            "Alice Client",
            "alice@client.com",
            "client"
        )
        db.collection("users").document(producer.uid).set(producer)
        db.collection("users").document(client.uid).set(client)

        val order = Order("1", client.uid, "1", producer.uid, "Pending", Timestamp.now(), GeoPoint(37.7739, -122.4214))
        db.collection("orders").document(order.id).set(order)
    }
}
