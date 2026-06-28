package com.turkcell.lyraapp.ui.screens.nowplaying

import com.turkcell.lyraapp.data.nowplaying.NowPlayingTrack

object NowPlayingContract {

    data class State(
        val track: NowPlayingTrack? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val isDownloaded: Boolean = false,
        val isDownloading: Boolean = false,
    )

    sealed interface Intent {
        data object LoadData : Intent
        data object PlayPauseClicked : Intent
        data object SkipNextClicked : Intent
        data object SkipPreviousClicked : Intent
        data object ShuffleClicked : Intent
        data object RepeatClicked : Intent
        data object FavoriteClicked : Intent
        data class SeekTo(val progress: Float) : Intent
        data object BackClicked : Intent
        data object ArkaplanClicked : Intent
        data object DownloadClicked : Intent
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data object NavigateToNotification : Effect
    }
}
