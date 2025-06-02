package com.example.sharing_food.Activity.data.repository.category

import com.example.sharing_food.Activity.data.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}
