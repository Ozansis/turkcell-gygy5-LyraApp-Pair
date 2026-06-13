package com.turkcell.lyraapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.home.HomeRepository
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
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadData                      -> loadData()
            HomeIntent.SeeAllRecentlyPlayedClicked   -> { /* ileride navigasyon eklenecek */ }
            is HomeIntent.MoodCategoryClicked        -> sendEffect(HomeEffect.NavigateToCategory(intent.category.id))
            is HomeIntent.TrackClicked               -> sendEffect(HomeEffect.NavigateToPlayer(intent.track.id))
            is HomeIntent.PlaylistClicked            -> sendEffect(HomeEffect.NavigateToPlaylist(intent.playlist.id))
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

            homeRepository.getRecentlyPlayed()
                .onSuccess { tracks -> _state.update { it.copy(recentlyPlayed = tracks) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }

            homeRepository.getRecommendedPlaylists()
                .onSuccess { playlists -> _state.update { it.copy(recommendedPlaylists = playlists) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
