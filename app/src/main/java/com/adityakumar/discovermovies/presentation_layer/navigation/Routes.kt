package com.adityakumar.discovermovies.presentation_layer.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class DetailsRoute(val movieId: Int)

@Serializable
object WatchlistRoute

@Serializable
object SettingsRoute
