package com.turkcell.lyraapp.ui.screens.playlistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.playlistdetail.PlaylistDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val playlistDetailRepository: PlaylistDetailRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistDetailContract.State())
    val state: StateFlow<PlaylistDetailContract.State> = _state.asStateFlow()

    private val _effect = Channel<PlaylistDetailContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: PlaylistDetailContract.Intent) {
        when (intent) {
            PlaylistDetailContract.Intent.LoadData              -> loadData()
            PlaylistDetailContract.Intent.BackClicked           -> sendEffect(PlaylistDetailContract.Effect.NavigateBack)
            is PlaylistDetailContract.Intent.TrackClicked       -> sendEffect(PlaylistDetailContract.Effect.NavigateToPlayer(intent.trackId))
            PlaylistDetailContract.Intent.LikePlaylistClicked   -> _state.update { it.copy(isLiked = !it.isLiked) }
            is PlaylistDetailContract.Intent.TrackLikeClicked   -> toggleTrackLike(intent.trackId)
            PlaylistDetailContract.Intent.MoreOptionsClicked    -> { /* ileride eklenecek */ }
            PlaylistDetailContract.Intent.DownloadClicked       -> { /* ileride eklenecek */ }
            PlaylistDetailContract.Intent.AddToLibraryClicked   -> { /* ileride eklenecek */ }
            PlaylistDetailContract.Intent.ShuffleClicked        -> { /* ileride eklenecek */ }
            PlaylistDetailContract.Intent.PlayClicked           -> { /* ileride eklenecek */ }
            is PlaylistDetailContract.Intent.TrackMoreOptionsClicked -> { /* ileride eklenecek */ }
        }
    }

    private fun loadData(playlistId: String = "gece-surusu") {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            playlistDetailRepository.getPlaylistInfo(playlistId)
                .onSuccess { info ->
                    _state.update {
                        it.copy(
                            title           = info.title,
                            description     = info.description,
                            author          = info.author,
                            trackCount      = info.trackCount,
                            totalDuration   = info.totalDuration,
                            coverStartColor = info.coverStartColor,
                            coverEndColor   = info.coverEndColor,
                            isLiked         = info.isLiked,
                        )
                    }
                }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }

            playlistDetailRepository.getPlaylistTracks(playlistId)
                .onSuccess { tracks -> _state.update { it.copy(tracks = tracks) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun toggleTrackLike(trackId: String) {
        _state.update { state ->
            state.copy(
                tracks = state.tracks.map { track ->
                    if (track.id == trackId) track.copy(isLiked = !track.isLiked) else track
                }
            )
        }
    }

    private fun sendEffect(effect: PlaylistDetailContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
