package com.example.sharing_food.ui.components.dashboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomMenuItem(val label: String, val icon: ImageVector)

@Composable
fun MyBottomBar(
    selectedRoute: String = "Home",
    onItemSelected: (String) -> Unit = {}
) {
    val bottomMenuItemsList = prepareBottomMenu()

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        bottomMenuItemsList.forEach { item ->
            val isSelected = item.label == selectedRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.label) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}

@Composable
fun prepareBottomMenu(): List<BottomMenuItem> {
    return listOf(
        BottomMenuItem("Home", Icons.Filled.Home),
        BottomMenuItem("Cart", Icons.Filled.LocationOn),
        BottomMenuItem("Favorite", Icons.Filled.Favorite),
        BottomMenuItem("Order", Icons.Filled.ShoppingCart),
        BottomMenuItem("Profile", Icons.Filled.Person)
    )
}
