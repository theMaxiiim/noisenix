package com.maxdejesus.noisenix.ui.mapTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MapScreen() {
    // System UI Controller for setting the status bar color
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black

    // Set the status bar color to black
    LaunchedEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = statusBarColor,
            darkIcons = false // Use light icons for better visibility on a dark background
        )
    }

    val udel = LatLng(39.6806583, -75.752577) // Define the camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(udel, 16f)
    }

    // Map UI settings: enable zoom controls
    var uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }

    // Map properties: use normal map type
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Google Map composable
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings
        ) {
            // Marker with a custom icon
            Marker(
                state = MarkerState(position = udel),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        }

        // Top gray label covering exactly 7% of the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.07f) // Covers 7% of the screen height
                .background(Color.Gray) // Gray background
                .align(Alignment.TopCenter) // Align at the top
        ) {
            Text(
                text = "Map",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Start, // Left-align text
                modifier = Modifier
                    .fillMaxSize() // Ensure text takes up full height
                    .padding(start = 16.dp) // Add left padding for text
            )
        }
    }
}
