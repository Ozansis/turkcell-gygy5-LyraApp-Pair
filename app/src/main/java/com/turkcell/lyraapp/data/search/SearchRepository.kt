package com.turkcell.lyraapp.data.search

interface SearchRepository {
    fun getGenres(): List<GenreCategory>
    suspend fun search(query: String): Result<List<SearchResult>>
}
