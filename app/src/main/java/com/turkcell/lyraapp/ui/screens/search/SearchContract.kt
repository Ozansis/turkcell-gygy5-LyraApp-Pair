package com.turkcell.lyraapp.ui.screens.search

import com.turkcell.lyraapp.data.search.GenreCategory
import com.turkcell.lyraapp.data.search.SearchResult

object SearchContract {

    data class State(
        val query: String = "",
        val genres: List<GenreCategory> = emptyList(),
        val searchResults: List<SearchResult> = emptyList(),
        val filters: List<String> = listOf("Hepsi", "Pop", "Elektronik", "Akustik"),
        val selectedFilter: String = "Hepsi",
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data object LoadData : Intent
        data class QueryChanged(val query: String) : Intent
        data class FilterSelected(val filter: String) : Intent
        data class GenreClicked(val genre: GenreCategory) : Intent
        data class ResultClicked(val result: SearchResult) : Intent
    }

    sealed interface Effect {
        data class NavigateToGenre(val genreId: String) : Effect
        data class NavigateToPlayer(val trackId: String) : Effect
    }
}
