package com.example.sharing_food.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharing_food.Activity.data.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    var isLoading by mutableStateOf(true)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var foods = mutableStateListOf<Food>()
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchFoodsByCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("foods")
                    .whereEqualTo("category.id", categoryId)
                    .get()
                    .await()

                foods.clear()
                foods.addAll(snapshot.toObjects(Food::class.java))

            } catch (e: Exception) {
                errorMessage = "Failed to load foods: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addFood(food: Food) {
        viewModelScope.launch {
            try {
                val docRef = db.collection("foods").document()
                val foodWithId = food.copy(id = docRef.id)

                docRef.set(foodWithId).await()

                // Update local list
                foods.add(foodWithId)

            } catch (e: Exception) {
                errorMessage = "Failed to add food: ${e.message}"
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun clearError() {
        errorMessage = null
    }

    val filteredFoods: List<Food>
        get() = foods.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
}

