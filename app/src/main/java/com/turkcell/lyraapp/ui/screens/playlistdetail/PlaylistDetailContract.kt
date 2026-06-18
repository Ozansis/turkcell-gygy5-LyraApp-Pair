package com.turkcell.lyraapp.ui.screens.playlistdetail

import com.turkcell.lyraapp.data.playlistdetail.PlaylistDetailTrack

object PlaylistDetailContract {

    data class State(
        val title: String = "",
        val description: String = "",
        val author: String = "",
        val trackCount: Int = 0,
        val totalDuration: String = "",
        val coverStartColor: Long = 0xFF6B4FA0L,
        val coverEndColor: Long = 0xFF4A3580L,
        val isLiked: Boolean = false,
        val tracks: List<PlaylistDetailTrack> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data object LoadData : Intent
        data object BackClicked : Intent
        data object MoreOptionsClicked : Intent
        data object LikePlaylistClicked : Intent
        data object DownloadClicked : Intent
        data object AddToLibraryClicked : Intent
        data object ShuffleClicked : Intent
        data object PlayClicked : Intent
        data class TrackClicked(val trackId: String) : Intent
        data class TrackLikeClicked(val trackId: String) : Intent
        data class TrackMoreOptionsClicked(val trackId: String) : Intent
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class NavigateToPlayer(val trackId: String) : Effect
    }
}
