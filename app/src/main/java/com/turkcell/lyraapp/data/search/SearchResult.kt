package com.turkcell.lyraapp.data.search

data class SearchResult(
    val id: String,
    val title: String,
    val artist: String,
    val genre: String,
    val coverStartColor: Long,
    val coverEndColor: Long,
)
