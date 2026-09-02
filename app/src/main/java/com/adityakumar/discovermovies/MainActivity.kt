package com.adityakumar.discovermovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.adityakumar.discovermovies.presentation_layer.navigation.AppNavHost
import com.adityakumar.discovermovies.ui.theme.DiscoverMoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiscoverMoviesTheme {
                AppNavHost()
            }
        }
    }
}
