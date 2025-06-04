package com.example.sharing_food.ui.navigation

sealed class ProfileScreenState {
    object Viewing : ProfileScreenState()
    object Editing : ProfileScreenState()
}