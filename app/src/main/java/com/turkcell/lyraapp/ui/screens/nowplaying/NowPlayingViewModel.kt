package com.turkcell.lyraapp.ui.screens.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.nowplaying.NowPlayingRepository
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
class NowPlayingViewModel @Inject constructor(
    private val nowPlayingRepository: NowPlayingRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(NowPlayingContract.State())
    val state: StateFlow<NowPlayingContract.State> = _state.asStateFlow()

    private val _effect = Channel<NowPlayingContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onIntent(NowPlayingContract.Intent.LoadData)
    }

    fun onIntent(intent: NowPlayingContract.Intent) {
        when (intent) {
            NowPlayingContract.Intent.LoadData          -> loadData()
            NowPlayingContract.Intent.PlayPauseClicked  -> togglePlayPause()
            NowPlayingContract.Intent.SkipNextClicked   -> Unit
            NowPlayingContract.Intent.SkipPreviousClicked -> Unit
            NowPlayingContract.Intent.ShuffleClicked    -> toggleShuffle()
            NowPlayingContract.Intent.RepeatClicked     -> toggleRepeat()
            NowPlayingContract.Intent.FavoriteClicked   -> toggleFavorite()
            is NowPlayingContract.Intent.SeekTo         -> seekTo(intent.progress)
            NowPlayingContract.Intent.BackClicked       -> sendEffect(NowPlayingContract.Effect.NavigateBack)
            NowPlayingContract.Intent.ArkaplanClicked   -> sendEffect(NowPlayingContract.Effect.NavigateToNotification)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            nowPlayingRepository.getNowPlayingTrack()
                .onSuccess { track -> _state.update { it.copy(track = track, isLoading = false) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message, isLoading = false) } }
        }
    }

    private fun togglePlayPause() {
        _state.update { it.copy(track = it.track?.copy(isPlaying = !it.track.isPlaying)) }
    }

    private fun toggleShuffle() {
        _state.update { it.copy(track = it.track?.copy(isShuffled = !it.track.isShuffled)) }
    }

    private fun toggleRepeat() {
        _state.update { it.copy(track = it.track?.copy(isRepeating = !it.track.isRepeating)) }
    }

    private fun toggleFavorite() {
        _state.update { it.copy(track = it.track?.copy(isFavorite = !it.track.isFavorite)) }
    }

    private fun seekTo(progress: Float) {
        _state.update { it.copy(track = it.track?.copy(progress = progress)) }
    }

    private fun sendEffect(effect: NowPlayingContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
