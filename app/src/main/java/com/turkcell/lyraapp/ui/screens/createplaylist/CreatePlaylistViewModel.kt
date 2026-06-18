package com.turkcell.lyraapp.ui.screens.createplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.createplaylist.CreatePlaylistRepository
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
class CreatePlaylistViewModel @Inject constructor(
    private val createPlaylistRepository: CreatePlaylistRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePlaylistContract.State())
    val state: StateFlow<CreatePlaylistContract.State> = _state.asStateFlow()

    private val _effect = Channel<CreatePlaylistContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: CreatePlaylistContract.Intent) {
        when (intent) {
            CreatePlaylistContract.Intent.LoadData              -> loadData()
            is CreatePlaylistContract.Intent.NameChanged        -> _state.update { it.copy(playlistName = intent.name) }
            is CreatePlaylistContract.Intent.DescriptionChanged -> _state.update { it.copy(description = intent.description) }
            CreatePlaylistContract.Intent.PublicToggled         -> _state.update { it.copy(isPublic = !it.isPublic) }
            is CreatePlaylistContract.Intent.TrackToggled       -> toggleTrack(intent.trackId)
            CreatePlaylistContract.Intent.EditCoverClicked      -> { /* ileride eklenecek */ }
            CreatePlaylistContract.Intent.SaveClicked           -> savePlaylist()
            CreatePlaylistContract.Intent.CloseClicked          -> sendEffect(CreatePlaylistContract.Effect.NavigateBack)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            createPlaylistRepository.getAvailableTracks()
                .onSuccess { tracks -> _state.update { it.copy(availableTracks = tracks) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun toggleTrack(trackId: String) {
        _state.update { state ->
            val updated = if (trackId in state.selectedTrackIds)
                state.selectedTrackIds - trackId
            else
                state.selectedTrackIds + trackId
            state.copy(selectedTrackIds = updated)
        }
    }

    private fun savePlaylist() {
        val state = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            createPlaylistRepository.createPlaylist(
                name        = state.playlistName,
                description = state.description,
                isPublic    = state.isPublic,
                trackIds    = state.selectedTrackIds.toList(),
            )
                .onSuccess { sendEffect(CreatePlaylistContract.Effect.NavigateBack) }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }
            _state.update { it.copy(isSaving = false) }
        }
    }

    private fun sendEffect(effect: CreatePlaylistContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
