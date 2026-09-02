package com.adityakumar.discovermovies.presentation_layer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adityakumar.discovermovies.presentation_layer.screens.DetailsUi
import com.adityakumar.discovermovies.presentation_layer.screens.HomeUi

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeRoute) {
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
    }
}
