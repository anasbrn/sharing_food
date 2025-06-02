package com.example.sharing_food.Activity.Dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sharing_food.R
import com.example.sharing_food.Activity.BaseActivity
import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.ViewModel.FoodViewModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf("Home") }

    var categoryIsLoading by remember { mutableStateOf(false) }

    val menuLabels = listOf("Home", "Cart", "Favorite", "Order", "Profile")
    val menuIcons = listOf(
        painterResource(R.drawable.btn_1),
        painterResource(R.drawable.btn_2),
        painterResource(R.drawable.btn_3),
        painterResource(R.drawable.btn_4),
        painterResource(R.drawable.btn_5)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Gray,
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                menuLabels.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedItem == label,
                        onClick = { selectedItem = label },
                        icon = { Icon(painter = menuIcons[index], contentDescription = label) },
                        label = null
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopBar()

            when (selectedItem) {
                "Home" -> {
                    var selectedCategory by remember { mutableStateOf<Category?>(null) }
                    val foodViewModel = remember { FoodViewModel() }

                    selectedCategory?.let { category ->
                        val foodContext = LocalContext.current
                        val foodSearchQuery = foodViewModel.searchQuery
                        val foodErrorMsg = foodViewModel.errorMessage
                        val filteredFoods = foodViewModel.filteredFoods

                        LaunchedEffect(category.id) {
                            foodViewModel.fetchFoodsByCategory(category.id)
                        }

                        LaunchedEffect(foodErrorMsg) {
                            foodErrorMsg?.let {
                                Toast.makeText(foodContext, it, Toast.LENGTH_LONG).show()
                                foodViewModel.clearError()
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Foods in ${category.name}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            SearchBar(
                                query = foodSearchQuery,
                                onQueryChanged = foodViewModel::updateSearchQuery
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            FoodList(filteredFoods)
                            Button(onClick = { selectedCategory = null }) {
                                Text("Back to Categories")
                            }
                        }
                    } ?: run {
                        // Show Categories
                        if (categoryIsLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            val context = LocalContext.current
                            val categoryViewModel = remember { CategoryViewModel() }
                            val categorySearchQuery = categoryViewModel.searchQuery
                            val filteredCategories = categoryViewModel.filteredCategories
                            val categoryErrorMsg = categoryViewModel.errorMessage

                            LaunchedEffect(categoryErrorMsg) {
                                categoryErrorMsg?.let {
                                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                                    categoryViewModel.clearError()
                                }
                            }
                            // Show categories
                            Column(modifier = Modifier.padding(16.dp)) {
                                SearchBar(
                                    query = categorySearchQuery,
                                    onQueryChanged = categoryViewModel::updateSearchQuery
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                CategoryGrid(categories = filteredCategories) { category ->
                                    selectedCategory = category
                                }
                            }
                        }
                    }
                }

                "Cart" -> Text("Cart Screen")
                "Favorite" -> Text("Favorite Screen")
                "Order" -> Text("Order Screen")
                "Profile" -> Text("Profile Screen")
                else -> Text("Home Screen")
            }
        }
    }
}
