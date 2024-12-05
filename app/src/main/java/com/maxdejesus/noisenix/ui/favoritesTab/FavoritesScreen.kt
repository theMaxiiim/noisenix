package com.maxdejesus.noisenix.ui.favoritesTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {
    val expandedItem = remember { mutableStateOf<String?>(null) }
    val locations = listOf("Trabant", "Gore Hall", "Memorial Hall", "Morris Library")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Favorites") }
        )

        // Search Bar
        SearchBar()

        Spacer(modifier = Modifier.height(16.dp))

        // Location Buttons
        locations.forEachIndexed { index, location ->
            val noiseLevel = when (index) {
                0 -> 0.7f // Noise level for Trabant
                1 -> 0.5f // Noise level for Gore Hall
                2 -> 0.3f // Noise level for Memorial Hall
                3 -> 0.1f // Noise level for Morris Library
                else -> 0.0f
            }
            val color = when {
                noiseLevel > 0.6f -> Color(0xFFE12B56) // High noise: Red
                noiseLevel > 0.4f -> Color(0xFFFFDE75) // Medium noise: Yellow
                else -> Color(0xFFBCE051) // Low noise: Green
            }
            ExpandableLocationCard(
                location = location,
                isExpanded = expandedItem.value == location,
                onExpandClick = {
                    expandedItem.value = if (expandedItem.value == location) null else location
                },
                noiseLevel = noiseLevel,
                cardColor = color
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ExpandableLocationCard(
    location: String,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    noiseLevel: Float,
    cardColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        // Location Button
        Button(
            onClick = onExpandClick,
            colors = ButtonDefaults.buttonColors(containerColor = cardColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = location,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Arrow Down",
                    tint = Color.Black
                )
            }
        }

        // Expanded Content
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            // Noise Level Bar
            Text(text = "Avg. Noise Score", style = MaterialTheme.typography.bodyMedium)
            LinearProgressIndicator(
                progress = noiseLevel, // Dynamic noise level
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = cardColor,
                trackColor = Color(0xFFF2F2F2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Location Details
            Text(
                text = "Directions &",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Last Updated ~ 6 hours ago",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search a location in Newark") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}
