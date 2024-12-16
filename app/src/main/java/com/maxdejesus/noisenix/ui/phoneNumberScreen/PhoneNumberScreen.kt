package com.maxdejesus.noisenix.ui.phoneNumberScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PhoneNumberScreen(navController: NavHostController) {
    var rawPhoneNumber by remember { mutableStateOf("") }

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
                    modifier = Modifier.padding(bottom = 32.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Please enter a phone number used for account creation.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = formatPhoneNumber(rawPhoneNumber),
                    onValueChange = { newValue ->
                        rawPhoneNumber = newValue.filter { it.isDigit() }.take(10)
                    },
                    label = { Text("Enter your phone number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    singleLine = true
                )

                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Receive SMS Code")
                }
            }
        }
    }
}

/**
 Formats a raw phone number into the format (XXX)-XXX-XXXX.
 */
fun formatPhoneNumber(phone: String): String {
    return when (phone.length) {
        in 1..3 -> "(${phone.substring(0, phone.length)})"
        in 4..6 -> "(${phone.substring(0, 3)})-${phone.substring(3, phone.length)}"
        in 7..10 -> "(${phone.substring(0, 3)})-${phone.substring(3, 6)}-${phone.substring(6, phone.length)}"
        else -> phone
    }
}
