package com.maxdejesus.noisenix.ui.favoritesTab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.maxdejesus.noisenix.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {
    val expandedItem = remember { mutableStateOf<String?>(null) }
    val locations = listOf("Trabant", "Gore Hall", "Morris Library", "Memorial Hall")
    val isTooltipVisible = remember { mutableStateOf(false) }

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.PartiallyExpanded)
    )

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            BottomSheetContent()
        },
        sheetPeekHeight = 80.dp,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .padding(top = LocalConfiguration.current.screenHeightDp.dp * 0.1f) // 10% top padding
                .fillMaxSize()
        ) {
            // Search Bar
            SearchBar()

            Spacer(modifier = Modifier.height(16.dp))

            // Location Cards
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

            // Tooltip Icon
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
                    .clickable { isTooltipVisible.value = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Help Icon",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Tooltip Dialog
            if (isTooltipVisible.value) {
                TooltipDialog(
                    onDismiss = { isTooltipVisible.value = false },
                    text = "Red indicates high noise levels, green indicates low noise levels. " +
                            "Click on any location to view more details."
                )
            }
        }
    }
}

@Composable
fun BottomSheetContent() {
    // Define noise levels for each location
    val noiseLevels = mapOf(
        "Trabant" to 0.7f,
        "Gore Hall" to 0.5f,
        "Memorial Hall" to 0.1f,
        "Morris Library" to 0.3f
    )

    // Sort locations by noise level (ascending)
    val sortedLocations = noiseLevels.entries.sortedBy { it.value }

    // Create rankings
    val rankings = sortedLocations.mapIndexed { index, entry ->
        Triple(entry.key, entry.value, index + 1) // Triple(Location, NoiseLevel, Rank)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title with a blue line
        Text(
            text = "Quietness Rankings",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HorizontalDivider(color = Color.Blue, thickness = 2.dp)

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal Scrollable Rankings
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            rankings.forEach { (location, noiseLevel, rank) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(150.dp) // Adjust width for larger items
                ) {
                    // Load images directly from the drawable directory
                    val imageResource = when (location) {
                        "Trabant" -> R.drawable.trabant_image
                        "Gore Hall" -> R.drawable.gore_hall_image
                        "Memorial Hall" -> R.drawable.memorial_image
                        "Morris Library" -> R.drawable.morris_image
                        else -> R.drawable.ic_launcher_foreground // Default placeholder
                    }

                    // Display image
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = "$location Image",
                        modifier = Modifier
                            .size(140.dp) // Increased image size
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Location details
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1
                    )
                    Text(
                        text = "Quietness Score: %.1f".format(noiseLevel),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1
                    )
                    Text(
                        text = "#${rank}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                }
            }
        }
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
        LargeButton(
            text = location,
            color = cardColor,
            onClick = onExpandClick
        )

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
                text = "Directions & Facilities",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Last Updated ~ 5 hours ago",
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
        modifier = Modifier.fillMaxWidth(),
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
                containerColor = Color.LightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
fun LargeButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Arrow Down",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun TooltipDialog(onDismiss: () -> Unit, text: String) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        }
    }
}
