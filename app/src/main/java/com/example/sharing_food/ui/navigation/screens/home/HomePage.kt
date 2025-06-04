package com.example.sharing_food.ui.navigation.screens.home

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.sharing_food.Activity.data.model.User
import com.example.sharing_food.Activity.data.repository.user.UserRepository
import com.example.sharing_food.ui.navigation.HomeScreenState
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomePage() {
    var currentScreen by remember { mutableStateOf<HomeScreenState>(HomeScreenState.Categories) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    val userRepository = UserRepository()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        currentUser = userRepository.getCurrentUser()
    }


    when (val screen = currentScreen) {
        is HomeScreenState.Categories -> CategoryPage(
            onCategorySelected = { selectedCategory ->
                currentScreen = HomeScreenState.FoodList(selectedCategory)
            }
        )

        is HomeScreenState.FoodList -> FoodListPage(
            category = screen.category,
            onFoodSelected = { selectedFood ->
                currentScreen = HomeScreenState.FoodDetail(selectedFood)
            },
            onBack = {
                currentScreen = HomeScreenState.Categories
            }
        )

        is HomeScreenState.FoodDetail -> FoodDetailPage(
            food = screen.food,
            onBack = {
                currentScreen = HomeScreenState.FoodList(screen.food.category)
            },
            onReserveClick = {
                currentScreen = HomeScreenState.OrderFood(screen.food)
            }
        )

        is HomeScreenState.OrderFood -> {
            currentUser?.let { user ->
                OrderFoodPage(
                    food = screen.food,
                    currentUser = user,
                    onOrderConfirmed = { order ->
                        val db = FirebaseFirestore.getInstance()
                        db.collection("orders").document(order.id).set(order)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Order placed!", Toast.LENGTH_SHORT).show()
                                currentScreen = HomeScreenState.FoodList(screen.food.category)
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to place order", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    },
                    onBack = {
                        currentScreen = HomeScreenState.FoodList(screen.food.category)
                    }
                )
            } ?: run {
                Text("Loading user...")
            }
        }
    }
}
