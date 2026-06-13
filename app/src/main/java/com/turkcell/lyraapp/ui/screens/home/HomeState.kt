package com.turkcell.lyraapp.ui.screens.home

import com.turkcell.lyraapp.data.home.MoodCategory
import com.turkcell.lyraapp.data.home.Playlist
import com.turkcell.lyraapp.data.home.Track

data class HomeState(
    val greeting: String = "",
    val userInitials: String = "",
    val moodCategories: List<MoodCategory> = emptyList(),
    val recentlyPlayed: List<Track> = emptyList(),
    val recommendedPlaylists: List<Playlist> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
