package com.adityakumar.discovermovies.data_layer.di

import android.content.Context
import androidx.room.Room
import com.adityakumar.discovermovies.data_layer.local.Dao.WatchListDao
import com.adityakumar.discovermovies.data_layer.local.DbBase.MovieDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import com.adityakumar.discovermovies.data_layer.remote.api.TmdbApiServices
import com.adityakumar.discovermovies.data_layer.repositoryImpl.repoImple
import com.adityakumar.discovermovies.domain_layer.repository.MovieRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.qualifiers.ApplicationContext

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    // 1. Database ka instance banane ke liye
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_db" // Aapki database file ka naam
        ).build()
    }


    // 2. DAO ka instance dene ke liye (taaki hum isse repo mein inject kar sakein)
    @Provides
    @Singleton
    fun provideWatchListDao(database: MovieDatabase): WatchListDao {
        return database.watchListDao()
    }





    // ~~~~Networking and Retrofit setup

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            // TMDB bahut saara data bhejta hai, hum sirf zaroori fields ko lenge aur baaki ignore karenge
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideTmdbApi(retrofit: Retrofit): TmdbApiServices {
        return retrofit.create(TmdbApiServices::class.java)
    }
}




//  ~~binding repo interface and repo implementation

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: repoImple
    ): MovieRepository
}


