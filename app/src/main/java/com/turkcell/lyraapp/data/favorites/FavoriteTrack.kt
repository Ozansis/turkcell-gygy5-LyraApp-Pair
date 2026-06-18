package com.turkcell.lyraapp.data.favorites

data class FavoriteTrack(
    val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val coverStartColor: Long,
    val coverEndColor: Long,
    val isLiked: Boolean = true,
    val isPlaying: Boolean = false,
)
