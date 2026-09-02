package com.adityakumar.discovermovies.data_layer.repositoryImpl

import com.adityakumar.discovermovies.data_layer.local.Dao.WatchListDao
import com.adityakumar.discovermovies.data_layer.local.entity.WatchListEntity
import com.adityakumar.discovermovies.data_layer.remote.api.TmdbApiServices
import com.adityakumar.discovermovies.domain_layer.dataModel.MovieDetails
import com.adityakumar.discovermovies.domain_layer.dataModel.movieData
import com.adityakumar.discovermovies.domain_layer.repository.MovieRepository
import com.adityakumar.discovermovies.utility.ResultState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class repoImple @Inject constructor(private val api: TmdbApiServices,
                                    private val watchListDao: WatchListDao
): MovieRepository {


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


    //~~RoomDb

    override suspend fun addToWatchlist(movie: WatchListEntity) {
        watchListDao.addToWatchList(movie)
    }

    override suspend fun removeFromWatchlist(movie: WatchListEntity) {
        watchListDao.removeFromWatchList(movie)
    }

    override fun getWatchlist(): Flow<List<WatchListEntity>> {
        return watchListDao.getWatchList()
    }

    override fun isMovieInWatchlist(movieId: Int): Flow<Boolean> {
        return watchListDao.isMovieInWatchList(movieId)
    }
}
