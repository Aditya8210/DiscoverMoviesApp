package com.adityakumar.discovermovies.presentation_layer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.adityakumar.discovermovies.presentation_layer.screens.DetailsUi
import com.adityakumar.discovermovies.presentation_layer.screens.HomeUi
import com.adityakumar.discovermovies.presentation_layer.screens.WatchlistUi

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Sirf in screens par bottom bar dikhana hai
    val showBottomBar = currentDestination?.hierarchy?.any { 
        it.hasRoute(HomeRoute::class) || it.hasRoute(WatchlistRoute::class) || it.hasRoute(SettingsRoute::class)
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF0F1113),
                    contentColor = Color.White
                ) {
                    val items = listOf(
                        Triple("Discover", HomeRoute, Icons.Default.Home),
                        Triple("Watchlist", WatchlistRoute, Icons.Default.Bookmark),
                        Triple("Settings", SettingsRoute, Icons.Default.Settings)
                    )

                    items.forEach { (label, route, icon) ->
                        val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(route::class) } == true
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF673AB7),
                                selectedTextColor = Color(0xFF673AB7),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, 
            startDestination = HomeRoute,
            modifier = Modifier.padding(if (showBottomBar) innerPadding else PaddingValues(0.dp))
        ) {
            composable<HomeRoute> {
                HomeUi(onMovieClick = { movieId ->
                    navController.navigate(DetailsRoute(movieId))
                })
            }
            composable<DetailsRoute> {
                DetailsUi(onBackClick = {
                    navController.popBackStack()
                })
            }
            composable<WatchlistRoute> {
                WatchlistUi(onMovieClick = { movieId ->
                    navController.navigate(DetailsRoute(movieId))
                })
            }
            composable<SettingsRoute> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Settings Screen (Coming Soon)", color = Color.White)
                }
            }
        }
    }
}
