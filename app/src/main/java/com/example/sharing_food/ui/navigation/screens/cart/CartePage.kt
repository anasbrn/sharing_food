package com.example.sharing_food.ui.navigation.screens.cart

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.coroutines.tasks.await
import com.example.sharing_food.Activity.data.model.SnackBar
import com.google.android.gms.maps.CameraUpdateFactory

@Composable
fun CartePage() {
    val snackBars = remember { mutableStateListOf<SnackBar>() }
    val cameraPositionState = rememberCameraPositionState()

    // Load all snackbars from Firestore
    LaunchedEffect(Unit) {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("snackbars")
            .get()
            .await()

        val loaded = snapshot.documents.mapNotNull { doc ->
            val name = doc.getString("name")
            val imageUrl = doc.getString("imageUrl")
            val geoPoint = doc.getGeoPoint("location")

            if (name != null && geoPoint != null) {
                SnackBar(
                    id = doc.id,
                    name = name,
                    image = imageUrl ?: "",
                    location = geoPoint
                )
            } else null
        }

        snackBars.addAll(loaded)

        // Move camera to first marker if available
        loaded.firstOrNull()?.location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            val cameraPosition = CameraPosition.fromLatLngZoom(latLng, 10f)
            cameraPositionState.move(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    // Full screen map
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        snackBars.forEach { sb ->
            val loc = LatLng(sb.location.latitude, sb.location.longitude)
            Marker(
                state = MarkerState(position = loc),
                title = sb.name
            )
        }
    }
}
