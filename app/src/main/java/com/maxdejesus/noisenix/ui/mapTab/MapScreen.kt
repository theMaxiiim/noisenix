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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import com.google.android.gms.maps.model.TileOverlayOptions

@Composable
fun MapScreen() {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black

    LaunchedEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = statusBarColor,
            darkIcons = false
        )
    }

    val udel = LatLng(39.6806583, -75.752577)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(udel, 16f)
    }

    var uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }

    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings
        ) {
            // Existing marker
            Marker(
                state = MarkerState(position = udel),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )

            // Add multiple heatmap points around 'udel' with individual intensities
            // These are example points with varying intensities
            val heatmapPoints = listOf(
                Pair(LatLng(39.67626314311263, -75.7497993547859), 8),
                Pair(LatLng(39.68226059816463, -75.75424106126172), 4),
                Pair(LatLng(39.68432542409064, -75.75146352542058), 9),
                Pair(LatLng(39.6806, -75.7532), 2)
            )

            AddHeatMap(points = heatmapPoints)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.07f)
                .background(Color.Gray)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "Map",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun AddHeatMap(points: List<Pair<LatLng, Int>>) {
    // Convert LatLng pairs to WeightedLatLng with individual intensities
    val weightedPoints = points.map { (latLng, intensity) ->
        val clampedIntensity = intensity.coerceIn(1, 10)
        WeightedLatLng(latLng, clampedIntensity.toDouble())
    }

    // Define a custom gradient that smoothly transitions through multiple colors
    // Example gradient: Transparent → Light Blue → Green → Yellow → Red
    val gradientColors = intArrayOf(
        0x0000FFFF.toInt(), // Transparent
        0xFF0080FF.toInt(), // Light Blue
        0xFF00FF00.toInt(), // Green
        0xFFFFFF00.toInt(), // Yellow
        0xFFFF0000.toInt()  // Red
    )

    // Start points must be in ascending order and range from 0 to 1
    // These specify where along the intensity range each color applies
    val startPoints = floatArrayOf(
        0.0f,   // Transparent at lower intensities
        0.25f,  // Transition to light blue
        0.50f,  // Then green
        0.75f,  // Then yellow
        1.0f    // Finally red at the highest intensity
    )

    val gradient = com.google.maps.android.heatmaps.Gradient(gradientColors, startPoints)

    val heatmapProvider by remember(weightedPoints) {
        mutableStateOf(
            HeatmapTileProvider.Builder()
                .weightedData(weightedPoints)
                .gradient(gradient)   // Use our custom cohesive gradient
                .build()
        )
    }

    // Increase radius if desired for larger spots
    heatmapProvider.setRadius(120)

    // Draw the heatmap overlay on the map
    TileOverlay(tileProvider = heatmapProvider)
}