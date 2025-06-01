package com.example.sharing_food.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.sharing_food.Activity.BaseActivity

class NextActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreetingScreen()
        }
    }
}

@Composable
fun GreetingScreen() {
    Text(text = "welcome to our app!")
}
