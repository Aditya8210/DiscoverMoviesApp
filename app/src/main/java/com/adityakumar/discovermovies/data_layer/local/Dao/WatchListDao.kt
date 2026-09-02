package com.adityakumar.discovermovies.data_layer.local.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adityakumar.discovermovies.data_layer.local.entity.WatchListEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface WatchListDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchList(movie: WatchListEntity)


    @Delete
    suspend fun removeFromWatchList(movie: WatchListEntity)


    @Query("SELECT * FROM watchlist_table ORDER BY addedAt DESC")
    fun getWatchList(): Flow<List<WatchListEntity>>


    @Query("SELECT EXISTS(SELECT * FROM watchlist_table WHERE id = :movieId)")
    fun isMovieInWatchList(movieId: Int): Flow<Boolean>





}