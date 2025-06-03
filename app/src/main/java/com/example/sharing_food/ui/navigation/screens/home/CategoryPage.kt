package com.example.sharing_food.ui.navigation.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sharing_food.ui.components.category.CategoryGrid
import com.example.sharing_food.Activity.Dashboard.CategoryViewModel
import com.example.sharing_food.ui.components.global.SearchBar
import com.example.sharing_food.Activity.data.model.Category
import com.example.sharing_food.Activity.data.repository.category.CategoryRepositoryImpl
import com.example.sharing_food.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CategoryPage(
    onCategorySelected: (Category) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val categoryRepo = remember { CategoryRepositoryImpl(firestore) }
    val viewModel = remember { CategoryViewModel(categoryRepo) }
    val context = LocalContext.current

    // Observe categoryState from ViewModel
    val categoryState = viewModel.categoryState
    val searchQuery = viewModel.searchQuery

    LaunchedEffect(categoryState) {
        if (categoryState is Resource.Error) {
            Toast.makeText(context, categoryState.message, Toast.LENGTH_LONG).show()
        }
    }

    when (categoryState) {
        is Resource.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is Resource.Error -> {}
        is Resource.Success -> {
            Column(Modifier.padding(16.dp)) {
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = viewModel::updateSearchQuery
                )
                Spacer(modifier = Modifier.height(16.dp))
                CategoryGrid(
                    categories = viewModel.filteredCategories,
                    onCategoryClick = onCategorySelected
                )
            }
        }
    }
}

