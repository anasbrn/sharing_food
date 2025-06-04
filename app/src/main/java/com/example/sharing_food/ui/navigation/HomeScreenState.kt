package com.example.sharing_food.ui.navigation

import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.Activity.data.model.Food

sealed class HomeScreenState {
    object Categories : HomeScreenState()
    data class FoodList(val category: Category) : HomeScreenState()
    data class FoodDetail(val food: Food) : HomeScreenState()
    data class OrderFood(val food: Food) : HomeScreenState()
}