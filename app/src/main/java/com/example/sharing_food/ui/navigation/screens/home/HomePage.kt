package com.example.sharing_food.ui.navigation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.sharing_food.ui.navigation.HomeScreenState

@Composable
fun HomePage() {
    var currentScreen by remember { mutableStateOf<HomeScreenState>(HomeScreenState.Categories) }

    when (val screen = currentScreen) {
        is HomeScreenState.Categories -> CategoryPage(
            onCategorySelected = { selectedCategory ->
                currentScreen = HomeScreenState.FoodList(selectedCategory)
            }
        )

        is HomeScreenState.FoodList -> FoodListPage(
            category = screen.category,
            onFoodSelected = { selectedFood ->
                currentScreen = HomeScreenState.FoodDetail(selectedFood)
            },
            onBack = {
                currentScreen = HomeScreenState.Categories
            }
        )

        is HomeScreenState.FoodDetail -> FoodDetailPage(
            food = screen.food,
            onBack = {
                currentScreen = HomeScreenState.FoodList(screen.food.category)
            }
        )
    }
}
