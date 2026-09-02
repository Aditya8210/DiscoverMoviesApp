package com.adityakumar.discovermovies.data_layer.local.DbBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adityakumar.discovermovies.data_layer.local.Dao.WatchListDao
import com.adityakumar.discovermovies.data_layer.local.entity.WatchListEntity

@Database(entities = [WatchListEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun watchListDao(): WatchListDao
}
