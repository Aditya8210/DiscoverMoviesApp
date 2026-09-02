package com.adityakumar.discovermovies.presentation_layer.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.adityakumar.discovermovies.domain_layer.dataModel.MovieDetails
import com.adityakumar.discovermovies.domain_layer.repository.MovieRepository
import com.adityakumar.discovermovies.presentation_layer.navigation.DetailsRoute
import com.adityakumar.discovermovies.utility.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val detailsRoute = savedStateHandle.toRoute<DetailsRoute>()

    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

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
}

data class DetailsState(
    val loading: Boolean = false,
    val data: MovieDetails? = null,
    val error: String = ""
)
