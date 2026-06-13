package com.turkcell.lyraapp.ui.screens.home

sealed interface HomeEffect {
    data class NavigateToPlayer(val trackId: String) : HomeEffect
    data class NavigateToPlaylist(val playlistId: String) : HomeEffect
    data class NavigateToCategory(val categoryId: String) : HomeEffect
}
