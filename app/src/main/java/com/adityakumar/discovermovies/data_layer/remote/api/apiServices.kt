package com.adityakumar.discovermovies.data_layer.remote.api

import com.adityakumar.discovermovies.BuildConfig
import com.adityakumar.discovermovies.domain_layer.dataModel.MovieDetails
import com.adityakumar.discovermovies.domain_layer.dataModel.movieData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiServices {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): movieData

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): movieData

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieDetails
}
