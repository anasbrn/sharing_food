package com.example.sharing_food.ui.components.global

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.sharing_food.utils.Resource

@Composable
fun <T> DropdownMenuBox(
    items: Resource<List<T>>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    val displayText = selectedItem?.let { itemLabel(it) } ?: "Select $label"

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) }
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            when (items) {
                is Resource.Success -> {
                    println("Dropdown contains ${items.data.size} items")
                    if (items.data.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No snackbars found") },
                            onClick = {},
                            enabled = false
                        )
                    } else {
                        items.data.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(itemLabel(item)) },
                                onClick = {
                                    onItemSelected(item)
                                    expanded = false
                                }
                            )
                        }

                    }
                }

                is Resource.Loading -> {
                    DropdownMenuItem(
                        text = { Text("Loading...") },
                        onClick = {},
                        enabled = false
                    )
                }

                is Resource.Error -> {
                    DropdownMenuItem(
                        text = { Text("Error: ${items.message}") },
                        onClick = {},
                        enabled = false
                    )
                }
            }
        }
    }
}
