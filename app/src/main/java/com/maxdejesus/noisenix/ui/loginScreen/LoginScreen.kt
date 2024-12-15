//Default SMS login: 147258

package com.maxdejesus.noisenix.ui.loginScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {
    var numericInput by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "NoiseNix",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Text(
                    text = "Please enter the SMS code you have received.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = numericInput,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            numericInput = newValue
                        }
                    },
                    label = { Text("Enter numbers") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (numericInput == "147258") {
                            navController.navigate("favorites")
                        } else {
                            showErrorDialog = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Login")
                }
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Error") },
            text = { Text(text = "Wrong code entered, please try again.") },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Close")
                }
            }
        )
    }
}
