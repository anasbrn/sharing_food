package com.example.sharing_food.Activity.Splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.sharing_food.Activity.BaseActivity
import com.example.sharing_food.Activity.Dashboard.MainActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sharing_food.R

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen(
                onSignupClick = {
                    // TODO: Add signup logic if needed
                },
                onGetStartedClick = {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            )
        }
    }
}

@Composable
fun SplashScreen(
    onSignupClick: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkBrown))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.pizza),
            contentDescription = "Pizza Image",
            modifier = Modifier.size(280.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        val styledText = buildAnnotatedString {
            append("Welcome to your ")
            withStyle(style = SpanStyle(color = colorResource(R.color.orange))) {
                append("food\nparadis ")
            }
            append("experience food perfection delivered")
        }

        Text(
            text = styledText,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp),
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.splashSubtitle),
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        GetStartedButtons(
            onSignupClick = onSignupClick,
            onGetStartedClick = onGetStartedClick
        )
    }
}

@Composable
fun GetStartedButtons(
    onSignupClick: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onSignupClick,
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.darkBrown))
        ) {
            Text(text = "Sign Up", color = Color.White)
        }

        Button(
            onClick = onGetStartedClick,
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.orange))
        ) {
            Text(text = "Get Started", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(
        onSignupClick = {},
        onGetStartedClick = {}
    )
}
