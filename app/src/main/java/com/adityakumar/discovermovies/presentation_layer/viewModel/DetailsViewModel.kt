package com.adityakumar.discovermovies.presentation_layer.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adityakumar.discovermovies.data_layer.local.entity.WatchListEntity
import com.adityakumar.discovermovies.domain_layer.dataModel.MovieDetails
import com.adityakumar.discovermovies.domain_layer.repository.MovieRepository
import com.adityakumar.discovermovies.presentation_layer.navigation.DetailsRoute
import com.adityakumar.discovermovies.utility.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val detailsRoute = savedStateHandle.toRoute<DetailsRoute>()

    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    // Watchlist status observe karne ke liye
    val isInWatchlist: StateFlow<Boolean> = repo.isMovieInWatchlist(detailsRoute.movieId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        getMovieDetails(detailsRoute.movieId)
    }

    private fun getMovieDetails(id: Int) {
        repo.getMovieDetails(id).onEach { result ->
            when (result) {
                is ResultState.Loading -> _detailsState.value = DetailsState(loading = true)
                is ResultState.Success -> _detailsState.value = DetailsState(data = result.data)
                is ResultState.Error -> _detailsState.value = DetailsState(error = result.message.toString())
            }
        }.launchIn(viewModelScope)
    }

    fun toggleWatchlist() {
        val movie = _detailsState.value.data ?: return
        viewModelScope.launch {
            val entity = WatchListEntity(
                id = movie.id ?: 0,
                title = movie.title ?: "",
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage
            )
            
            if (isInWatchlist.value) {
                repo.removeFromWatchlist(entity)
            } else {
                repo.addToWatchlist(entity)
            }
        }
    }
}

data class DetailsState(
    val loading: Boolean = false,
    val data: MovieDetails? = null,
    val error: String = ""
)
