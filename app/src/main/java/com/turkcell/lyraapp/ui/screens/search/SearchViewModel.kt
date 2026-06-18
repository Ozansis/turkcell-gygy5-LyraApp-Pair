package com.turkcell.lyraapp.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchContract.State())
    val state: StateFlow<SearchContract.State> = _state.asStateFlow()

    private val _effect = Channel<SearchContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onIntent(SearchContract.Intent.LoadData)
    }

    fun onIntent(intent: SearchContract.Intent) {
        when (intent) {
            is SearchContract.Intent.LoadData             -> loadGenres()
            is SearchContract.Intent.QueryChanged         -> onQueryChanged(intent.query)
            is SearchContract.Intent.FilterSelected       -> onFilterSelected(intent.filter)
            is SearchContract.Intent.GenreClicked         -> sendEffect(SearchContract.Effect.NavigateToGenre(intent.genre.id))
            is SearchContract.Intent.ResultClicked        -> sendEffect(SearchContract.Effect.NavigateToPlayer(intent.result.id))
        }
    }

    private fun loadGenres() {
        _state.update { it.copy(genres = searchRepository.getGenres()) }
    }

    private fun onQueryChanged(query: String) {
        _state.update { it.copy(query = query, isLoading = query.isNotEmpty()) }
        if (query.isBlank()) {
            _state.update { it.copy(searchResults = emptyList(), isLoading = false) }
            return
        }
        viewModelScope.launch {
            searchRepository.search(query)
                .onSuccess { results ->
                    val filtered = applyFilter(results, _state.value.selectedFilter)
                    _state.update { it.copy(searchResults = filtered, isLoading = false, error = null) }
                }
                .onFailure { throwable ->
                    _state.update { it.copy(error = throwable.message, isLoading = false) }
                }
        }
    }

    private fun onFilterSelected(filter: String) {
        _state.update { it.copy(selectedFilter = filter) }
        val currentResults = _state.value.searchResults
        if (currentResults.isNotEmpty() || _state.value.query.isNotBlank()) {
            viewModelScope.launch {
                searchRepository.search(_state.value.query)
                    .onSuccess { results ->
                        _state.update { it.copy(searchResults = applyFilter(results, filter)) }
                    }
            }
        }
    }

    private fun applyFilter(results: List<com.turkcell.lyraapp.data.search.SearchResult>, filter: String) =
        if (filter == "Hepsi") results else results.filter { it.genre.equals(filter, ignoreCase = true) }

    private fun sendEffect(effect: SearchContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
