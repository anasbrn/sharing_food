package com.example.sharing_food.Activity.data.remote.category

import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.Activity.data.repository.category.CategoryRepository

class CategoryRepositoryImpl(
    private val service: FirebaseCategoryService
) : CategoryRepository {
    override suspend fun getCategories(): List<Category> {
        return service.fetchCategories()
    }
}
