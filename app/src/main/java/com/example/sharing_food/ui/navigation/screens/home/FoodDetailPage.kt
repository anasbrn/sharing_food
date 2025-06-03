package com.example.sharing_food.ui.navigation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sharing_food.Activity.data.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun FoodDetailPage(
    food: Food,
    onBack: () -> Unit
) {
    val snackBarName = remember { mutableStateOf<String?>(null) }
    val snackBarImage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(food.snackBar) {
        val snackBarRef = food.snackBar?.let {
            FirebaseFirestore.getInstance().collection("snackbars")
                .document(it.id)
        }
        val snapshot = snackBarRef?.get()?.await()
        snackBarName.value = snapshot?.getString("name")
        snackBarImage.value = snapshot?.getString("imageUrl")
    }

    Column(Modifier.padding(16.dp)) {
        Text("Food: ${food.name}", style = MaterialTheme.typography.h1)
        Spacer(modifier = Modifier.height(8.dp))

        snackBarImage.value?.let {
            AsyncImage(model = it, contentDescription = "Snack Bar Image")
        }

        Text("Snack Bar: ${snackBarName.value ?: "Loading..."}")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back to Food List")
        }
    }
}
