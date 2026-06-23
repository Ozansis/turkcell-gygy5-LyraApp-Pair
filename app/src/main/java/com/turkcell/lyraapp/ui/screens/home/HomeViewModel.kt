package com.turkcell.lyraapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.home.HomeRepository
import com.turkcell.lyraapp.data.player.PlayerStateHolder
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
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val playerStateHolder: PlayerStateHolder,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeContract.State())
    val state: StateFlow<HomeContract.State> = _state.asStateFlow()

    private val _effect = Channel<HomeContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: HomeContract.Intent) {
        when (intent) {
            HomeContract.Intent.LoadData                      -> loadData()
            HomeContract.Intent.SeeAllRecentlyPlayedClicked   -> { /* ileride navigasyon eklenecek */ }
            is HomeContract.Intent.MoodCategoryClicked        -> sendEffect(HomeContract.Effect.NavigateToCategory(intent.category.id))
            is HomeContract.Intent.TrackClicked               -> {
                playerStateHolder.currentTrack = intent.track
                sendEffect(HomeContract.Effect.NavigateToPlayer(intent.track.id))
            }
            is HomeContract.Intent.PlaylistClicked            -> sendEffect(HomeContract.Effect.NavigateToPlaylist(intent.playlist.id))
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            _state.update {
                it.copy(
                    greeting     = homeRepository.getGreeting(),
                    userInitials = homeRepository.getUserInitials(),
                )
            }

            homeRepository.getMoodCategories()
                .onSuccess { categories -> _state.update { it.copy(moodCategories = categories) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }

            homeRepository.getSongs()
                .onSuccess { tracks -> _state.update { it.copy(songs = tracks) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }

            homeRepository.getRecentlyPlayed()
                .onSuccess { tracks -> _state.update { it.copy(recentlyPlayed = tracks) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }

            homeRepository.getRecommendedPlaylists()
                .onSuccess { playlists -> _state.update { it.copy(recommendedPlaylists = playlists) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: HomeContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
