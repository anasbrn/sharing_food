package com.example.sharing_food.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharing_food.Activity.data.model.SnackBar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SnackBarViewModel : ViewModel() {

    private val _snackBars = MutableStateFlow<List<SnackBar>>(emptyList())
    val snackBars: StateFlow<List<SnackBar>> = _snackBars

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchSnackBars()
    }

    private fun fetchSnackBars() {
        viewModelScope.launch {
            db.collection("snackbars")
                .get()
                .addOnSuccessListener { result ->
                    val list = result.map { document ->
                        document.toObject(SnackBar::class.java)
                    }
                    _snackBars.value = list
                }
                .addOnFailureListener {
                    // Handle error if needed
                }
        }
    }
}
