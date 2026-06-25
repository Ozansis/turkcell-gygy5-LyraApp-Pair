package com.turkcell.lyraapp.ui.screens.nowplaying

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.nowplaying.NowPlayingTrack
import com.turkcell.lyraapp.data.player.LyraMusicService
import com.turkcell.lyraapp.data.player.PlayerStateHolder
import com.turkcell.lyraapp.data.remote.MeApiService
import com.turkcell.lyraapp.data.remote.RecordPlayBody
import com.turkcell.lyraapp.data.remote.SongsApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val playerStateHolder: PlayerStateHolder,
    private val songsApiService: SongsApiService,
    private val meApiService: MeApiService,
) : ViewModel() {

    private val _state = MutableStateFlow(NowPlayingContract.State())
    val state: StateFlow<NowPlayingContract.State> = _state.asStateFlow()

    private val _effect = Channel<NowPlayingContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    // Player arayzü media3-exoplayer'dan gelir; MediaController IS-A Player
    private var player: Player? = null

    init {
        connectToService()
    }

    private fun connectToService() {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, LyraMusicService::class.java)
        )
        val future = MediaController.Builder(context, sessionToken).buildAsync()
        future.addListener({
            player = future.get()
            loadData()
        }, ContextCompat.getMainExecutor(context))
    }

    fun onIntent(intent: NowPlayingContract.Intent) {
        when (intent) {
            NowPlayingContract.Intent.LoadData            -> loadData()
            NowPlayingContract.Intent.PlayPauseClicked    -> togglePlayPause()
            NowPlayingContract.Intent.SkipNextClicked     -> seekForward()
            NowPlayingContract.Intent.SkipPreviousClicked -> restart()
            NowPlayingContract.Intent.ShuffleClicked      -> toggleShuffle()
            NowPlayingContract.Intent.RepeatClicked       -> toggleRepeat()
            NowPlayingContract.Intent.FavoriteClicked     -> toggleFavorite()
            is NowPlayingContract.Intent.SeekTo           -> seekTo(intent.progress)
            NowPlayingContract.Intent.BackClicked         -> sendEffect(NowPlayingContract.Effect.NavigateBack)
            NowPlayingContract.Intent.ArkaplanClicked     -> sendEffect(NowPlayingContract.Effect.NavigateToNotification)
        }
    }

    private fun loadData() {
        val track = playerStateHolder.currentTrack ?: return
        val p = player ?: return

        _state.update {
            it.copy(
                isLoading = true,
                track = NowPlayingTrack(
                    id              = track.id,
                    title           = track.title,
                    artist          = track.artist,
                    playlistName    = "Sarkılar",
                    coverStartColor = track.coverStartColor,
                    coverEndColor   = track.coverEndColor,
                    duration        = "0:00",
                    currentPosition = "0:00",
                    progress        = 0f,
                    isFavorite      = false,
                    isPlaying       = false,
                    isShuffled      = false,
                    isRepeating     = false,
                ),
            )
        }

        viewModelScope.launch {
            runCatching { songsApiService.getStreamUrl(track.id) }
                .onSuccess { response ->
                    val mediaItem = MediaItem.fromUri(response.data.url)
                    p.setMediaItem(mediaItem)
                    p.prepare()
                    p.play()
                    _state.update { it.copy(isLoading = false, track = it.track?.copy(isPlaying = true)) }
                    startPositionPolling()
                    if (playerStateHolder.lastRecordedSongId != track.id) {
                        runCatching { meApiService.recordPlay(RecordPlayBody(songId = track.id)) }
                        playerStateHolder.lastRecordedSongId = track.id
                    }
                }
                .onFailure { throwable ->
                    _state.update { it.copy(isLoading = false, error = throwable.message) }
                }
        }
    }

    private fun startPositionPolling() {
        viewModelScope.launch {
            while (true) {
                delay(500)
                val p = player ?: break
                val rawDuration = p.duration
                val duration = if (rawDuration > 0) rawDuration else 0L
                val position = p.currentPosition.coerceAtLeast(0L)
                val progress = if (duration > 0) position.toFloat() / duration.toFloat() else 0f
                _state.update { state ->
                    state.copy(
                        track = state.track?.copy(
                            currentPosition = formatMs(position),
                            duration        = formatMs(duration),
                            progress        = progress,
                            isPlaying       = p.isPlaying,
                        )
                    )
                }
            }
        }
    }

    private fun togglePlayPause() {
        val p = player ?: return
        val wasPlaying = p.isPlaying
        if (wasPlaying) p.pause() else p.play()
        _state.update { it.copy(track = it.track?.copy(isPlaying = !wasPlaying)) }
    }

    private fun seekForward() {
        val p = player ?: return
        val newPos = (p.currentPosition + 10_000L).coerceAtMost(p.duration.coerceAtLeast(0L))
        p.seekTo(newPos)
    }

    private fun restart() {
        player?.seekTo(0)
        _state.update { it.copy(track = it.track?.copy(progress = 0f, currentPosition = "0:00")) }
    }

    private fun seekTo(progress: Float) {
        val p = player ?: return
        val duration = p.duration.takeIf { it > 0 } ?: return
        p.seekTo((duration * progress).toLong())
        _state.update { it.copy(track = it.track?.copy(progress = progress)) }
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

    private fun formatMs(ms: Long): String {
        if (ms <= 0) return "0:00"
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }

    private fun sendEffect(effect: NowPlayingContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    override fun onCleared() {
        player = null
        super.onCleared()
    }
}
