package com.example.sharing_food.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharing_food.Activity.data.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var foods by mutableStateOf<List<Food>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchFoodsByCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("foods")
                    .whereEqualTo("categoryId", categoryId)
                    .get()
                    .await()

                foods = snapshot.toObjects(Food::class.java)
            } catch (e: Exception) {
                errorMessage = "Failed to load foods: ${e.message}"
            } finally {
                isLoading = false
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

