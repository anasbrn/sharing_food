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

        val producer = User(
            "4cqRHoC6D2h7FBmj5KwLCHRhyqp2",
            "brnanas",
            "brnanass@gmail.com",
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

        val pizzaCategory = Category("1", "Pizza", "https://example.com/pizza.jpg")
        val drinksCategory = Category("2", "Drinks", "https://example.com/drinks.jpg")
        val dessertsCategory = Category("3", "Desserts", "https://example.com/desserts.jpg")

        val categories = listOf(
            pizzaCategory, drinksCategory, dessertsCategory
        )
        categories.forEach { db.collection("categories").document(it.id).set(it) }

        val snackBar1 = SnackBar(
            "1",
            "City Snack Bar",
            "https://example.com/desserts.jpg",
            GeoPoint(37.7749, -122.4194)
        )
        val snackBar2 = SnackBar(
            id = "2",
            name = "Downtown Snack Spot",
            image = "https://example.com/snacks.jpg",
            location = com.google.firebase.firestore.GeoPoint(34.0522, -118.2437) // Los Angeles
        )
        val snackBar3 = SnackBar(
            id = "3",
            name = "Beachside Snacks",
            image = "https://example.com/beach.jpg",
            location = com.google.firebase.firestore.GeoPoint(32.7157, -117.1611) // San Diego
        )
        db.collection("snackbars").document(snackBar1.id).set(snackBar1)
        db.collection("snackbars").document(snackBar2.id).set(snackBar2)
        db.collection("snackbars").document(snackBar3.id).set(snackBar3)

        val pizza4Saison = Food(
            "1",
            "Pizza 4 Saison",
            "This is Pizza 4 Saison",
            "https://example.com/pizza.jpg",
            40.50,
            pizzaCategory,
            producer,
            snackBar1
        )
        db.collection("foods").document(pizza4Saison.id).set(pizza4Saison)

        val order = Order(
            "1",
            client,
            pizza4Saison,
            producer,
            "Pending",
            Timestamp.now(),
            GeoPoint(40.7128, -74.0060)
        )
        db.collection("orders").document(order.id).set(order)
    }
}
