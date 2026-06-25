package com.turkcell.lyraapp.ui.screens.home

import com.turkcell.lyraapp.data.home.MoodCategory
import com.turkcell.lyraapp.data.home.Playlist
import com.turkcell.lyraapp.data.home.Track

object HomeContract {
    data class State(
        val greeting: String = "",
        val userInitials: String = "",
        val songs: List<Track> = emptyList(),
        val moodCategories: List<MoodCategory> = emptyList(),
        val recentlyPlayed: List<Track> = emptyList(),
        val recommendations: List<Track> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data object LoadData : Intent
        data object SeeAllRecentlyPlayedClicked : Intent
        data class MoodCategoryClicked(val category: MoodCategory) : Intent
        data class TrackClicked(val track: Track) : Intent
        data class PlaylistClicked(val playlist: Playlist) : Intent
    }

    sealed interface Effect {
        data class NavigateToPlayer(val trackId: String) : Effect
        data class NavigateToPlaylist(val playlistId: String) : Effect
        data class NavigateToCategory(val categoryId: String) : Effect
    }
}
