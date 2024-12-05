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
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Help
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.Image
import com.maxdejesus.noisenix.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )
    val coroutineScope = rememberCoroutineScope()

    val isTooltipVisible = remember { mutableStateOf(false) }

    val bottomSheetContent = remember { mutableStateOf("Default content for the bottom sheet.") }
    val bottomSheetImageResId = remember { mutableStateOf<Int?>(null) }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            BottomSheetContent(
                content = bottomSheetContent.value,
                imageResId = bottomSheetImageResId.value
            )
        },
        sheetPeekHeight = 80.dp,
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

                SearchBar()

                Spacer(modifier = Modifier.height(16.dp))

                LargeButton(
                    text = "Trabant",
                    color = Color(0xFFE12B56),
                    onClick = {
                        bottomSheetContent.value = "DAY AVG DB: 33.7\nWEEK AVG DB: 35.8\n" +
                                "NUMBER OF SUBS (LAST 7 DAYS): 44"
                        bottomSheetImageResId.value = R.drawable.trabant_image
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
                        bottomSheetContent.value = "DAY AVG DB: 23.4\nWEEK AVG DB: 22.5\n" +
                                "NUMBER OF SUBS (LAST 7 DAYS): 36"
                        bottomSheetImageResId.value = R.drawable.gore_hall_image
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
                        bottomSheetContent.value = "DAY AVG DB: 19.5\nWEEK AVG DB: 18.0\n" +
                                "NUMBER OF SUBS (LAST 7 DAYS): 32"
                        bottomSheetImageResId.value = R.drawable.memorial_image
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
                        bottomSheetContent.value = "DAY AVG DB: 9.8\nWEEK AVG DB: 10.4\n" +
                                "NUMBER OF SUBS (LAST 7 DAYS): 28"
                        bottomSheetImageResId.value = R.drawable.morris_image
                        coroutineScope.launch {
                            sheetState.bottomSheetState.expand()
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Tooltip Icon
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp)
                        .clickable { isTooltipVisible.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Help,
                        contentDescription = "Help Icon",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Tooltip Dialog
            if (isTooltipVisible.value) {
                TooltipDialog(
                    onDismiss = { isTooltipVisible.value = false },
                    text = "Red Indicates High Noise Levels, Green Indicates Low Noise Levels" +
                    ". Click On Any Location To View More Details."
                )
            }
        }
    )
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
fun BottomSheetContent(content: String, imageResId: Int?) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val maxHeight = screenHeight * 0.65f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 0.dp, max = maxHeight)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        imageResId?.let {
            val painter = painterResource(id = it)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}