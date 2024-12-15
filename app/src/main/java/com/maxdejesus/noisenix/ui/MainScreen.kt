package com.maxdejesus.noisenix.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.maxdejesus.noisenix.ui.favoritesTab.FavoritesScreen
import com.maxdejesus.noisenix.ui.mapTab.MapScreen
import com.maxdejesus.noisenix.ui.loginScreen.LoginScreen
import com.maxdejesus.noisenix.ui.phoneNumberScreen.PhoneNumberScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(navController)) {
                BottomNavigationBar(navController = navController)
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
            composable(BottomNavItem.Map.route) {
                MapScreen()
            }
        }
    }
}

@Composable
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    return currentBackStackEntry?.destination?.route !in listOf("prelogin", "login")
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Favorites,
        BottomNavItem.Map
    )
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val currentDestination = navController.currentDestination
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
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
    object Map : BottomNavItem("map", Icons.Sharp.Place, "Map")
}
