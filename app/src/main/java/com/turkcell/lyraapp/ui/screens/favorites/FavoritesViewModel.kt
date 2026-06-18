package com.turkcell.lyraapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.favorites.FavoritesRepository
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
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesContract.State())
    val state: StateFlow<FavoritesContract.State> = _state.asStateFlow()

    private val _effect = Channel<FavoritesContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onIntent(FavoritesContract.Intent.LoadData)
    }

    fun onIntent(intent: FavoritesContract.Intent) {
        when (intent) {
            FavoritesContract.Intent.LoadData              -> loadData()
            FavoritesContract.Intent.PlayClicked           -> Unit
            FavoritesContract.Intent.ShuffleClicked        -> Unit
            FavoritesContract.Intent.DownloadClicked       -> Unit
            is FavoritesContract.Intent.TrackClicked       -> selectTrack(intent.trackId)
            is FavoritesContract.Intent.TrackLikeClicked   -> toggleLike(intent.trackId)
            is FavoritesContract.Intent.TrackMoreOptionsClicked -> Unit
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            favoritesRepository.getFavoriteTracks()
                .onSuccess { tracks ->
                    _state.update {
                        it.copy(
                            tracks        = tracks,
                            trackCount    = tracks.size,
                            totalDuration = computeTotalDuration(tracks.map { t -> t.duration }),
                        )
                    }
                }
                .onFailure { throwable ->
                    _state.update { it.copy(error = throwable.message) }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun computeTotalDuration(durations: List<String>): String {
        val totalSeconds = durations.sumOf { duration ->
            val parts = duration.split(":")
            parts[0].toInt() * 60 + parts[1].toInt()
        }
        return "${totalSeconds / 60} dk"
    }

    private fun selectTrack(trackId: String) {
        _state.update { current ->
            current.copy(
                tracks = current.tracks.map { track ->
                    track.copy(isPlaying = track.id == trackId)
                }
            )
        }
    }

    private fun toggleLike(trackId: String) {
        _state.update { current ->
            current.copy(
                tracks = current.tracks.map { track ->
                    if (track.id == trackId) track.copy(isLiked = !track.isLiked) else track
                }
            )
        }
    }

    private fun sendEffect(effect: FavoritesContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
