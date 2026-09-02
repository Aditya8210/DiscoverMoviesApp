package com.adityakumar.discovermovies.data_layer.repositoryImpl

import com.adityakumar.discovermovies.data_layer.remote.api.TmdbApiServices
import com.adityakumar.discovermovies.domain_layer.dataModel.MovieDetails
import com.adityakumar.discovermovies.domain_layer.dataModel.movieData
import com.adityakumar.discovermovies.domain_layer.repository.MovieRepository
import com.adityakumar.discovermovies.utility.ResultState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class repoImple @Inject constructor(private val api: TmdbApiServices): MovieRepository {
    override fun movieSearch(query: String): Flow<ResultState<movieData>>  = flow{
        emit(ResultState.Loading())
        try {
            val response = api.searchMovie(query=query)
            emit(ResultState.Success(response))
        }catch (e: Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    override fun getPopularMovies(): Flow<ResultState<movieData>> = flow {
        emit(ResultState.Loading())
        try {
            val response = api.getPopularMovies()
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    override fun getMovieDetails(movieId: Int): Flow<ResultState<MovieDetails>> = flow {
        emit(ResultState.Loading())
        try {
            val response = api.getMovieDetails(movieId)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
}
