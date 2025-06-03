package com.example.sharing_food.Activity.data.repository.user

import com.example.sharing_food.Activity.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    suspend fun getCurrentUser(): User? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        return try {
            val documentSnapshot = FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}