package com.example.sharing_food.utils

import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.Activity.data.model.Food
import com.example.sharing_food.Activity.data.model.Order
import com.example.sharing_food.Activity.data.model.SnackBar
import com.example.sharing_food.Activity.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp

object DatabaseSeeder {
    fun seedFirestoreDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = FirebaseFirestore.getInstance()

            val producer = User(
                "4cqRHoC6D2h7FBmj5KwLCHRhyqp2",
                "brnanas",
                "brnanass@gmail.com",
                "producer"
            )
            val client = User(
                "v9hUzY0coCY2xavwwlEyVlIUtCx1",
                "anasclient",
                "anasbarnoch@gmail.com",
                "client"
            )

            seedUser(db, producer)
            seedUser(db, client)

            val categories = listOf(
                Category("1", "Pizza", "https://example.com/pizza.jpg"),
                Category("2", "Drinks", "https://example.com/drinks.jpg"),
                Category("3", "Desserts", "https://example.com/desserts.jpg")
            )

            for (cat in categories) {
                seedDocumentIfNotExists(db, "categories", cat.id, cat)
            }

            val snackBars = listOf(
                SnackBar("1", "City Snack Bar", "https://example.com/desserts.jpg", GeoPoint(37.7749, -122.4194)),
                SnackBar("2", "Downtown Snack Spot", "https://example.com/snacks.jpg", GeoPoint(34.0522, -118.2437)),
                SnackBar("3", "Beachside Snacks", "https://example.com/beach.jpg", GeoPoint(32.7157, -117.1611))
            )

            for (bar in snackBars) {
                seedDocumentIfNotExists(db, "snackbars", bar.id, bar)
            }

            val pizza4Saison = Food(
                "1",
                "Pizza 4 Saison",
                "This is Pizza 4 Saison",
                "https://example.com/pizza.jpg",
                40.50,
                categories[0],
                producer,
                snackBars[0]
            )
            seedDocumentIfNotExists(db, "foods", pizza4Saison.id, pizza4Saison)

            val order = Order(
                "1",
                client,
                pizza4Saison,
                producer,
                "Pending",
                Timestamp.now(),
                GeoPoint(40.7128, -74.0060),
                2,
                pizza4Saison.price * 2
            )
            seedDocumentIfNotExists(db, "orders", order.id, order)
        }
    }

    private suspend fun <T : Any> seedDocumentIfNotExists(db: FirebaseFirestore, collection: String, docId: String, data: T) {
        val ref = db.collection(collection).document(docId)
        val snapshot = ref.get().await()
        if (!snapshot.exists()) {
            ref.set(data, SetOptions.merge()).await()
        }
    }

    private suspend fun seedUser(db: FirebaseFirestore, user: User) {
        val ref = db.collection("users").document(user.uid)
        val snapshot = ref.get().await()
        if (!snapshot.exists()) {
            ref.set(user, SetOptions.merge()).await()
        }
    }
}
