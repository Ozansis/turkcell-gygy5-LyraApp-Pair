package com.turkcell.lyraapp.ui.screens.home

import com.turkcell.lyraapp.data.home.MoodCategory
import com.turkcell.lyraapp.data.home.Playlist
import com.turkcell.lyraapp.data.home.Track

sealed interface HomeIntent {
    data object LoadData : HomeIntent
    data object SeeAllRecentlyPlayedClicked : HomeIntent
    data class MoodCategoryClicked(val category: MoodCategory) : HomeIntent
    data class TrackClicked(val track: Track) : HomeIntent
    data class PlaylistClicked(val playlist: Playlist) : HomeIntent
}
