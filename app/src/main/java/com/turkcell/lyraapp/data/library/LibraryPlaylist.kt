package com.turkcell.lyraapp.data.library

data class LibraryPlaylist(
    val id: String,
    val title: String,
    val trackCount: Int,
    val coverStartColor: Long,
    val coverEndColor: Long,
    val isPinned: Boolean = false,
    val showHeartOnCover: Boolean = false,
)
