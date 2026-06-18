package com.turkcell.lyraapp.ui.screens.createplaylist

import com.turkcell.lyraapp.data.createplaylist.AvailableTrack

object CreatePlaylistContract {

    data class State(
        val playlistName: String = "",
        val description: String = "",
        val isPublic: Boolean = true,
        val coverStartColor: Long = 0xFFC07060L,
        val coverEndColor: Long = 0xFF9A5040L,
        val availableTracks: List<AvailableTrack> = emptyList(),
        val selectedTrackIds: Set<String> = emptySet(),
        val isSaving: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data object LoadData : Intent
        data class NameChanged(val name: String) : Intent
        data class DescriptionChanged(val description: String) : Intent
        data object PublicToggled : Intent
        data class TrackToggled(val trackId: String) : Intent
        data object EditCoverClicked : Intent
        data object SaveClicked : Intent
        data object CloseClicked : Intent
    }

    sealed interface Effect {
        data object NavigateBack : Effect
    }
}
