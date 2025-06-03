package com.example.sharing_food.ui.components.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.sharing_food.Activity.data.model.Category

@Composable
fun CategoryItem(category: Category, onClick: (Category) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(category) }, // 🔥 clickable
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), // Square image
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CategoryGrid(categories: List<Category>, onCategoryClick: (Category) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxHeight(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category, onClick = onCategoryClick)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CategoryItemPreview() {
//    val sampleCategory = Category(
//        id = "1",
//        name = "Pizza",
//        imageUrl = "https://res.cloudinary.com/dkikc5ywq/image/upload/v1738610243/benner1_fmdi0w.jpg"
//    )
//    val sampleCategory2 = Category(
//        id = "1",
//        name = "Pizza",
//        imageUrl = "https://example.com/pizza.jpg"
//    )
//    var categories = listOf(sampleCategory, sampleCategory2)
//    CategoryGrid(categories = categories)
//}
