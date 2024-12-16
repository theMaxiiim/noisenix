package com.maxdejesus.noisenix.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.maxdejesus.noisenix.ui.favoritesTab.FavoritesScreen
import com.maxdejesus.noisenix.ui.loginScreen.LoginScreen
import com.maxdejesus.noisenix.ui.mapTab.MapScreen
import com.maxdejesus.noisenix.ui.phoneNumberScreen.PhoneNumberScreen
import com.maxdejesus.noisenix.ui.submitSample.SubmitSampleOverlay

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var showSubmitOverlay by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(navController)) {
                BottomNavigationBar(
                    navController = navController,
                    onSubmitSampleClick = { showSubmitOverlay = true }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "prelogin",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("prelogin") {
                PhoneNumberScreen(navController = navController)
            }
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable(BottomNavItem.Favorites.route) {
                FavoritesScreen()
            }
            // Removed the SubmitSample route as we no longer navigate to show the overlay
            composable(BottomNavItem.Map.route) {
                MapScreen()
            }
        }

        if (showSubmitOverlay) {
            SubmitSampleOverlay(onClose = { showSubmitOverlay = false })
        }
    }
}

@Composable
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    return currentBackStackEntry?.destination?.route !in listOf("prelogin", "login")
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onSubmitSampleClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem.Favorites,
        BottomNavItem.SubmitSample,
        BottomNavItem.Map
    )
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val currentDestination = navController.currentDestination
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.route && item != BottomNavItem.SubmitSample,
                onClick = {
                    when (item) {
                        BottomNavItem.Favorites -> {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        BottomNavItem.SubmitSample -> {
                            // Show the overlay instead of navigating
                            onSubmitSampleClick()
                        }
                        BottomNavItem.Map -> {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Favorites : BottomNavItem("favorites", Icons.Filled.Star, "Favorites")
    object SubmitSample : BottomNavItem("submitSample", Icons.Filled.Add, "Submit Sample")
    object Map : BottomNavItem("map", Icons.Sharp.Place, "Map")
}