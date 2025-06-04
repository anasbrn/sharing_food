package com.example.sharing_food.ui.navigation.screens.favoris

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sharing_food.Activity.data.model.Food
import com.example.sharing_food.Activity.data.model.User
import com.example.sharing_food.Activity.data.repository.user.UserRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavorisPage() {
    val context = LocalContext.current
    val userRepo = remember { UserRepository() }
    var favorites by remember { mutableStateOf<List<Food>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val currentUser: User? = userRepo.getCurrentUser()
        if (currentUser != null) {
            favorites = currentUser.favorites
        } else {
            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
        }
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Favorites") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )
        }
    ) { innerPadding ->
        if (loading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (favorites.isEmpty()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No favorites found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(favorites) { food ->
                    FavoriteFoodItem(food = food)
                }
            }
        }
    }
}

@Composable
fun FavoriteFoodItem(food: Food) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = food.imageUrl,
                contentDescription = food.name,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(food.name, style = MaterialTheme.typography.titleMedium)
                Text(food.description, maxLines = 2, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text("${food.price} DH", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
