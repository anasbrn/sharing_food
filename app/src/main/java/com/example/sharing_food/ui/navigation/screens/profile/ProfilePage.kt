package com.example.sharing_food.ui.navigation.screens.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharing_food.Activity.data.model.User
import com.example.sharing_food.Activity.data.repository.user.UserRepository
import com.example.sharing_food.ui.navigation.ProfileScreenState
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.example.sharing_food.Activity.LoginActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    context: Context = LocalContext.current,
    activity: Activity? = context as? Activity,
    showLogoutDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val userRepo = remember { UserRepository() }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var screenState by remember { mutableStateOf<ProfileScreenState>(ProfileScreenState.Viewing) }

    LaunchedEffect(Unit) {
        currentUser = userRepo.getCurrentUser()
    }

    when (screenState) {
        is ProfileScreenState.Viewing -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Profile") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(100.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = currentUser?.username?.firstOrNull()?.uppercase() ?: "?",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentUser?.username ?: "",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = currentUser?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currentUser?.role?.replaceFirstChar { it.uppercase() } ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { screenState = ProfileScreenState.Editing },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Edit Profile")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { showLogoutDialog.value = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Logout")
                    }

                    if (showLogoutDialog.value) {
                        AlertDialog(
                            onDismissRequest = { showLogoutDialog.value = false },
                            title = { Text("Confirm Logout") },
                            text = { Text("Are you sure you want to log out?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    FirebaseAuth.getInstance().signOut()
                                    showLogoutDialog.value = false
                                    // Navigate to LoginActivity
                                    activity?.let {
                                        it.startActivity(Intent(it, LoginActivity::class.java))
                                        it.finish() // close MainActivity so back doesn't go here
                                    }
                                }) {
                                    Text("Logout")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showLogoutDialog.value = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }
            }
        }

        is ProfileScreenState.Editing -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Edit Profile") },
                        navigationIcon = {
                            IconButton(onClick = { screenState = ProfileScreenState.Viewing }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Edit Profile Page",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // You can add editable fields here like TextFields for username, email, etc.
                    Text("Coming soon...", textAlign = TextAlign.Center)
                }
            }
        }
    }
}
