package com.adityakumar.discovermovies.data_layer.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import com.adityakumar.discovermovies.data_layer.remote.api.TmdbApiServices
import com.adityakumar.discovermovies.data_layer.repositoryImpl.repoImple
import com.adityakumar.discovermovies.domain_layer.repository.MovieRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

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

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: repoImple
    ): MovieRepository
}

