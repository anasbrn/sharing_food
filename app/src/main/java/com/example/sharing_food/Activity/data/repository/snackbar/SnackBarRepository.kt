package com.example.sharing_food.Activity.data.repository.snackbar

import com.example.sharing_food.Activity.data.model.SnackBar
import com.example.sharing_food.utils.Resource

interface SnackBarRepository {
    abstract suspend fun loadSnackBars(): Resource<List<SnackBar>>
}