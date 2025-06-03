package com.example.sharing_food.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.*
import com.example.sharing_food.Activity.data.model.SnackBar
import com.example.sharing_food.Activity.data.repository.snackbar.SnackBarRepository
import com.example.sharing_food.utils.Resource
import kotlinx.coroutines.launch

class SnackBarViewModel : ViewModel() {
    class SnackBarViewModel(
        private val repository: SnackBarRepository
    ): ViewModel() {

        var snackBars by mutableStateOf<List<SnackBar>>(emptyList())
        var errorMessage by mutableStateOf<String?>(null)
        var isLoading by mutableStateOf(false)

        fun fetchSnackBars() {
            viewModelScope.launch {
                isLoading = true
                when (val result = repository.loadSnackBars()) {
                    is Resource.Success -> snackBars = result.data
                    is Resource.Error -> errorMessage = result.message
                    Resource.Loading -> {}
                }
                isLoading = false
            }
        }
    }

}

