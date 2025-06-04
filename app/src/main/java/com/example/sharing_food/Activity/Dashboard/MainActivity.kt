package com.example.sharing_food.Activity.Dashboard

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import com.example.sharing_food.Activity.BaseActivity
import com.example.sharing_food.ui.components.dashboard.MyBottomBar
import com.example.sharing_food.ui.components.dashboard.TopBar
import com.example.sharing_food.ui.navigation.screens.cart.CartePage
import com.example.sharing_food.ui.navigation.screens.favoris.FavorisPage
import com.example.sharing_food.ui.navigation.screens.home.HomePage
import com.example.sharing_food.ui.navigation.screens.order.OrderPage
import com.example.sharing_food.ui.theme.SharingFoodTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SharingFoodTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf("Home") }

    Scaffold(
        bottomBar = {
            SharingFoodTheme {
                MyBottomBar(
                    selectedRoute = selectedTab,
                    onItemSelected = { selectedTab = it }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TopBar()
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
