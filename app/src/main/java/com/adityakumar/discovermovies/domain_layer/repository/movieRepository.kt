package com.adityakumar.discovermovies.domain_layer.repository

import com.adityakumar.discovermovies.data_layer.local.entity.WatchListEntity
import com.adityakumar.discovermovies.domain_layer.dataModel.MovieDetails
import com.adityakumar.discovermovies.domain_layer.dataModel.movieData
import com.adityakumar.discovermovies.utility.ResultState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun movieSearch(query: String): Flow<ResultState<movieData>>
    fun getPopularMovies(): Flow<ResultState<movieData>>
    fun getMovieDetails(movieId: Int): Flow<ResultState<MovieDetails>>


    // ~~RoomDb

    suspend fun addToWatchlist(movie: WatchListEntity)
    suspend fun removeFromWatchlist(movie: WatchListEntity)
    fun getWatchlist(): Flow<List<WatchListEntity>>
    fun isMovieInWatchlist(movieId: Int): Flow<Boolean>
}
