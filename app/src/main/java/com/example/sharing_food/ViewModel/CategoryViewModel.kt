package com.example.sharing_food.Activity.Dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.Activity.data.repository.category.CategoryRepository
import com.example.sharing_food.utils.Resource

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var categoryState by mutableStateOf<Resource<List<Category>>>(Resource.Loading)
        private set

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            categoryState = repository.loadCategories()
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    val filteredCategories: List<Category>
        get() = when (val state = categoryState) {
            is Resource.Success -> state.data.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

            else -> emptyList()
        }
}

