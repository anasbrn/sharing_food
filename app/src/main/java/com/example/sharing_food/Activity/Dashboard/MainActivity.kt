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
import com.example.sharing_food.R
import com.example.sharing_food.Activity.BaseActivity
import com.example.sharing_food.ui.components.dashboard.TopBar
import com.example.sharing_food.ui.navigation.screens.cart.CartePage
import com.example.sharing_food.ui.navigation.screens.favoris.FavorisPage
import com.example.sharing_food.ui.navigation.screens.home.HomePage
import com.example.sharing_food.ui.navigation.screens.order.OrderPage

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
    var selectedTab by remember { mutableStateOf("Home") }

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
                        selected = selectedTab == label,
                        onClick = { selectedTab = label },
                        icon = { Icon(painter = menuIcons[index], contentDescription = label) },
                        label = null
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TopBar() // Optional: a composable for app title/logo

            when (selectedTab) {
                "Home" -> HomePage()
                    "Cart" -> CartePage()
                    "Favorite" -> FavorisPage()
                    "Order" -> OrderPage()
//                    "Profile" -> ProfilePage()
                else -> Text("Unknown screen")
            }
        }
    }

}
