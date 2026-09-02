package com.adityakumar.discovermovies.domain_layer.repository

import com.adityakumar.discovermovies.domain_layer.dataModel.MovieDetails
import com.adityakumar.discovermovies.domain_layer.dataModel.movieData
import com.adityakumar.discovermovies.utility.ResultState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun movieSearch(query: String): Flow<ResultState<movieData>>
    fun getPopularMovies(): Flow<ResultState<movieData>>
    fun getMovieDetails(movieId: Int): Flow<ResultState<MovieDetails>>
}
