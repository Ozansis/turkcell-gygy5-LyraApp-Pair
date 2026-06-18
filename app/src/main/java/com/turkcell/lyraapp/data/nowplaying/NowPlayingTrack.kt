package com.turkcell.lyraapp.data.nowplaying

data class NowPlayingTrack(
    val id: String,
    val title: String,
    val artist: String,
    val playlistName: String,
    val coverStartColor: Long,
    val coverEndColor: Long,
    val duration: String,
    val currentPosition: String,
    val progress: Float,
    val isFavorite: Boolean,
    val isPlaying: Boolean,
    val isShuffled: Boolean,
    val isRepeating: Boolean,
)
