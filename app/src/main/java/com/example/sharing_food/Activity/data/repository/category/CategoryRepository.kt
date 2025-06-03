package com.example.sharing_food.Activity.data.repository.category

import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.utils.Resource

interface CategoryRepository {
    abstract suspend fun loadCategories(): Resource<List<Category>>
}
