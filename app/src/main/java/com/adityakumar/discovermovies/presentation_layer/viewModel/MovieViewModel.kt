package com.adityakumar.discovermovies.presentation_layer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityakumar.discovermovies.domain_layer.dataModel.movieData
import com.adityakumar.discovermovies.domain_layer.repository.MovieRepository
import com.adityakumar.discovermovies.utility.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class MovieViewModel @Inject constructor(private val repo: MovieRepository): ViewModel() {

    private val _mState = MutableStateFlow(SearchState())
    val mState = _mState.asStateFlow()

    init {
        getPopularMovies()
    }

    private fun getPopularMovies() {
        repo.getPopularMovies().onEach { result ->
            when (result) {
                is ResultState.Loading -> _mState.value = SearchState(loading = true)
                is ResultState.Success -> _mState.value = SearchState(data = result.data)
                is ResultState.Error -> _mState.value = SearchState(error = result.message.toString())
            }
        }.launchIn(viewModelScope)
    }

    fun searchMovies(query: String){
        if (query.isEmpty()) {
            getPopularMovies()
            return
        }
        repo.movieSearch(query).onEach { result ->
            when(result){
                is ResultState.Loading -> _mState.value = SearchState(loading = true)
                is ResultState.Success -> _mState.value = SearchState(data = result.data)
                is ResultState.Error -> _mState.value = SearchState(error = result.message.toString())
            }
        }.launchIn(viewModelScope)
    }
}




data class SearchState(
    val loading: Boolean = false,
    val data: movieData? = null,
    val error: String = ""
)
