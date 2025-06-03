package com.example.sharing_food.Activity.data.repository.snackbar

import com.example.sharing_food.Activity.data.model.SnackBar
import com.example.sharing_food.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SnackBarRepositoryImpl(
    private val firestore: FirebaseFirestore
) : SnackBarRepository {
    override suspend fun loadSnackBars(): Resource<List<SnackBar>> {
        return try {
            val snapshot = firestore.collection("snackbars").get().await()
            val list = snapshot.toObjects(SnackBar::class.java)
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected error")
        }
    }
}
