package com.example.sharing_food.Activity.data.repository.category

import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryRepositoryImpl(
    private val firestore: FirebaseFirestore
) : CategoryRepository {
    override suspend fun loadCategories(): Resource<List<Category>> {
        return try {
            val snapshot = firestore.collection("categories").get().await()
            val list = snapshot.toObjects(Category::class.java)
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected error")
        }
    }
}
