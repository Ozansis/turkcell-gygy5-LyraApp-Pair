package com.turkcell.lyraapp.data.playlistdetail

data class PlaylistDetailInfo(
    val title: String,
    val description: String,
    val author: String,
    val trackCount: Int,
    val totalDuration: String,
    val coverStartColor: Long,
    val coverEndColor: Long,
    val isLiked: Boolean,
)
