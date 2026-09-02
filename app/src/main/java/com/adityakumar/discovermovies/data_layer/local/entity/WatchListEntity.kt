package com.adityakumar.discovermovies.data_layer.local.entity

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "watchlist_table")
data class WatchListEntity(
    @PrimaryKey val id:Int,

    val title: String,
    val posterPath: String?,
    val releaseDate:String?,
    val voteAverage: Double?,
    val addedAt: Long = System.currentTimeMillis()
)
