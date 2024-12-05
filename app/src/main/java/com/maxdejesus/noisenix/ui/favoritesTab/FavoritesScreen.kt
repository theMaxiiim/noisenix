package com.maxdejesus.noisenix.ui.favoritesTab

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            BottomSheetContent()
        },
        sheetPeekHeight = 80.dp, // Adjust the peek height as needed
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // Top App Bar
                TopAppBar(
                    title = { Text("Favorites") }
                )

                // Search Bar
                SearchBar()

                Spacer(modifier = Modifier.height(16.dp))

                // Large Buttons
                LargeButton(
                    text = "Trabant",
                    color = Color(0xFFE12B56),
                    onClick = {
                        coroutineScope.launch {
                            sheetState.bottomSheetState.expand()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LargeButton(
                    text = "Gore Hall",
                    color = Color(0xFFFFDE75),
                    onClick = {
                        coroutineScope.launch {
                            sheetState.bottomSheetState.expand()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LargeButton(
                    text = "Memorial Hall",
                    color = Color(0xFFBCE051),
                    onClick = {
                        coroutineScope.launch {
                            sheetState.bottomSheetState.expand()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LargeButton(
                    text = "Morris Library",
                    color = Color(0xFFBCE051),
                    onClick = {
                        coroutineScope.launch {
                            sheetState.bottomSheetState.expand()
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    )
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

@Composable
fun LargeButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(60.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
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
}

@Composable
fun BottomSheetContent() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val maxHeight = screenHeight * 0.65f // 65% of screen height

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 0.dp, max = maxHeight)
            .padding(24.dp) // Increased padding
    ) {
        // Removed custom drag handle

        Spacer(modifier = Modifier.height(16.dp))

        // Content of the bottom sheet
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            style = MaterialTheme.typography.bodyLarge, // Increased text size
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}