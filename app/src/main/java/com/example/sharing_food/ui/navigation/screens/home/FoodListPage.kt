package com.example.sharing_food.ui.navigation.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sharing_food.ui.components.global.SearchBar
import com.example.sharing_food.ViewModel.FoodViewModel
import com.example.sharing_food.Activity.data.model.*
import com.example.sharing_food.Activity.data.repository.snackbar.SnackBarRepositoryImpl
import com.example.sharing_food.Activity.data.repository.user.UserRepository
import com.example.sharing_food.ui.components.global.DropdownMenuBox
import com.example.sharing_food.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListPage(
    category: Category,
    onFoodSelected: (Food) -> Unit,
    onBack: () -> Unit
) {
    val userRepo = UserRepository()
    val viewModel = remember { FoodViewModel() }
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var favorites by remember { mutableStateOf<List<Food>>(emptyList()) }

    val error = viewModel.errorMessage

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }
    LaunchedEffect(Unit) {
        currentUser = userRepo.getCurrentUser()
        currentUser?.let { user ->
            // Load favorites from user
            favorites = user.favorites
        }
    }

    LaunchedEffect(category.id) {
        viewModel.fetchFoodsByCategory(category.id)
    }

    if (showDialog && currentUser != null) {
        AddFoodDialog(
            category = category,
            producer = currentUser!!,
            onDismiss = { showDialog = false },
            onFoodAdded = { newFood ->
                viewModel.addFood(newFood)
                showDialog = false
            }
        )
    }

    fun toggleFavorite(food: Food) {
        currentUser?.let { user ->
            if (favorites.any { it.id == food.id }) {
                viewModel.removeFoodFromFavorites(context, user.uid, food)
                favorites = favorites.filterNot { it.id == food.id }
            } else {
                viewModel.addFoodToFavorites(context, user.uid, food)
                favorites = favorites + food
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foods in ${category.name}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentUser?.role == "producer") {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Food")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            SearchBar(
                query = viewModel.searchQuery,
                onQueryChanged = viewModel::updateSearchQuery
            )
            Spacer(modifier = Modifier.height(16.dp))
            FoodList(
                foods = viewModel.filteredFoods,
                favorites = favorites,
                onFoodClick = onFoodSelected,
                onFavoriteClick = { food -> toggleFavorite(food) }
            )
        }
    }
}

@Composable
fun AddFoodDialog(
    category: Category,
    producer: User,
    onDismiss: () -> Unit,
    onFoodAdded: (Food) -> Unit
) {
    val firestore = remember { FirebaseFirestore.getInstance() }
    val snackbarRepository = SnackBarRepositoryImpl(firestore)

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var imageUrlError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var snackBarError by remember { mutableStateOf(false) }

    var selectedSnackBar by remember { mutableStateOf<SnackBar?>(null) }
    var snackBars by remember { mutableStateOf<Resource<List<SnackBar>>>(Resource.Loading) }

    LaunchedEffect(Unit) {
        snackBars = snackbarRepository.loadSnackBars()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                nameError = name.isBlank()
                descriptionError = description.isBlank()
                imageUrlError = imageUrl.isBlank()
                priceError =
                    price.isBlank() || price.toDoubleOrNull() == null || price.toDouble() <= 0
                snackBarError = selectedSnackBar == null

                val isValid =
                    !(nameError || descriptionError || imageUrlError || priceError || snackBarError)

                if (isValid) {
                    val newFood = Food(
                        name = name,
                        description = description,
                        imageUrl = imageUrl,
                        price = price.toDouble(),
                        category = category,
                        producer = producer,
                        snackBar = selectedSnackBar
                    )
                    onFoodAdded(newFood)
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add New Food") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text("Name") },
                    isError = nameError
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = false
                    },
                    label = { Text("Description") },
                    isError = descriptionError
                )

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = {
                        imageUrl = it
                        imageUrlError = false
                    },
                    label = { Text("Image URL") },
                    isError = imageUrlError
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        priceError = false
                    },
                    label = { Text("Price") },
                    isError = priceError
                )

                Spacer(modifier = Modifier.height(8.dp))

                DropdownMenuBox(
                    items = snackBars,
                    selectedItem = selectedSnackBar,
                    onItemSelected = {
                        selectedSnackBar = it
                        snackBarError = false
                    },
                    itemLabel = { it.name },
                    label = if (snackBarError) "SnackBar (Required)" else "SnackBar"
                )
            }
        }
    )
}

@Composable
fun FoodListItem(
    food: Food,
    isFavorite: Boolean,
    onFoodClick: (Food) -> Unit,
    onFavoriteClick: (Food) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onFoodClick(food) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.height(180.dp)) {
            // ✅ Background image fills entire card
            AsyncImage(
                model = food.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // Overlay gradient
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 50f
                        )
                    )
            )

            // Bottom text and favorite button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = food.name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${food.price} DH",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(
                    onClick = { onFavoriteClick(food) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun FoodList(
    foods: List<Food>,
    favorites: List<Food>,
    onFoodClick: (Food) -> Unit,
    onFavoriteClick: (Food) -> Unit
) {
    LazyColumn {
        items(foods) { food ->
            val isFavorite = favorites.any { it.id == food.id }
            FoodListItem(
                food = food,
                isFavorite = isFavorite,
                onFoodClick = onFoodClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}






