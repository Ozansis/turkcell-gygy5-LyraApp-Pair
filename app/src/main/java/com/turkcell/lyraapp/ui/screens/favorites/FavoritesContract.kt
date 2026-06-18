package com.turkcell.lyraapp.ui.screens.favorites

import com.turkcell.lyraapp.data.favorites.FavoriteTrack

object FavoritesContract {

    data class State(
        val tracks: List<FavoriteTrack> = emptyList(),
        val trackCount: Int = 0,
        val totalDuration: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data object LoadData : Intent
        data object PlayClicked : Intent
        data object ShuffleClicked : Intent
        data object DownloadClicked : Intent
        data class TrackClicked(val trackId: String) : Intent
        data class TrackLikeClicked(val trackId: String) : Intent
        data class TrackMoreOptionsClicked(val trackId: String) : Intent
    }

    sealed interface Effect {
        data class NavigateToPlayer(val trackId: String) : Effect
    }
}
