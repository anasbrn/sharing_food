package com.example.sharing_food.Activity.Dashboard

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.sharing_food.R
import com.example.sharing_food.Activity.BaseActivity

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
            TopBar()  // 👉 your custom top bar here

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),  // optional: add some space below TopBar
                contentAlignment = Alignment.Center
            ) {
                when (selectedItem) {
                    "Home" -> Text("Home Screen")
                    "Cart" -> Text("Cart Screen")
                    "Favorite" -> Text("Favorite Screen")
                    "Order" -> Text("Order Screen")
                    "Profile" -> Text("Profile Screen")
                    else -> Text("Home Screen")
                }
            }
        }
    }
}
