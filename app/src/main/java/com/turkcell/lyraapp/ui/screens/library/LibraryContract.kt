package com.turkcell.lyraapp.ui.screens.library

import com.turkcell.lyraapp.data.library.LibraryPlaylist

enum class LibraryTab { Playlists, Artists, Albums }

object LibraryContract {

    data class State(
        val selectedTab: LibraryTab = LibraryTab.Playlists,
        val playlists: List<LibraryPlaylist> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data object LoadData : Intent
        data class TabSelected(val tab: LibraryTab) : Intent
        data class PlaylistClicked(val playlistId: String) : Intent
        data class MoreOptionsClicked(val playlistId: String) : Intent
        data object SearchClicked : Intent
        data object AddClicked : Intent
        data object SortClicked : Intent
        data object ViewToggleClicked : Intent
    }

    sealed interface Effect {
        data class NavigateToPlaylist(val playlistId: String) : Effect
        data object NavigateToSearch : Effect
        data object NavigateToCreatePlaylist : Effect
    }
}
