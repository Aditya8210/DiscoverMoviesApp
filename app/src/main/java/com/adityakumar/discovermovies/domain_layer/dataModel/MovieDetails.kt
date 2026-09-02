package com.adityakumar.discovermovies.domain_layer.dataModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetails(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("genres")
    val genres: List<Genre>? = emptyList()
)

@Serializable
data class Genre(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null
)
