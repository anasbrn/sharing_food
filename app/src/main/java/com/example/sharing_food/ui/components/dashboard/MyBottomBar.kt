package com.example.sharing_food.ui.components.dashboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import com.example.sharing_food.R
import androidx.compose.runtime.Composable

data class BottomMenuItem(val label: String, val icon: Painter)

@Composable
fun MyBottomBar() {
    val bottomMenuItemsList = prepareBottomMenu()
    var selectedItem by remember { mutableStateOf("Home") }

    NavigationBar(
        containerColor = Color.Gray,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        bottomMenuItemsList.forEach { bottomMenuItem ->
            NavigationBarItem(
                selected = (selectedItem == bottomMenuItem.label),
                onClick = { selectedItem = bottomMenuItem.label },
                icon = {
                    Icon(
                        painter = bottomMenuItem.icon,
                        contentDescription = bottomMenuItem.label
                    )
                },
                label = null // ou tu peux mettre un Text(bottomMenuItem.label) si tu veux
            )
        }
    }
}

@Composable
fun prepareBottomMenu(): List<BottomMenuItem> {
    return listOf(
        BottomMenuItem("Home", painterResource(R.drawable.btn_1)),
        BottomMenuItem("Cart", painterResource(R.drawable.btn_2)),
        BottomMenuItem("Favorite", painterResource(R.drawable.btn_3)),
        BottomMenuItem("Order", painterResource(R.drawable.btn_4)),
        BottomMenuItem("Profile", painterResource(R.drawable.btn_5))
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMyBottomBar() {
    Surface {
        MyBottomBar()
    }
}
