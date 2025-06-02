package com.example.sharing_food.Activity.Dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.*
import com.example.sharing_food.Activity.data.model.Category

class CategoryViewModel : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("categories")
                    .get()
                    .await()

                categories = snapshot.toObjects(Category::class.java)
            } catch (e: Exception) {
                errorMessage = "Failed to load categories: ${e.message}"
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

    val filteredCategories: List<Category>
        get() = categories.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
}
