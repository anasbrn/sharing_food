package com.example.sharing_food.Activity.data.remote.category

import com.example.sharing_food.Activity.data.model.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseCategoryService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun fetchCategories(): List<Category> = suspendCoroutine { cont ->
        db.collection("categories").get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(Category::class.java)
                }
                cont.resume(list)
            }
            .addOnFailureListener {
                cont.resumeWithException(it)
            }
    }
}
