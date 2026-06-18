package com.turkcell.lyraapp.data.playlistdetail

data class PlaylistDetailTrack(
    val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val coverStartColor: Long,
    val coverEndColor: Long,
    val isLiked: Boolean = false,
    val isPlaying: Boolean = false,
)
