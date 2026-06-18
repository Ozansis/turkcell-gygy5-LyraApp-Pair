package com.turkcell.lyraapp.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.library.LibraryRepository
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
class LibraryViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryContract.State())
    val state: StateFlow<LibraryContract.State> = _state.asStateFlow()

    private val _effect = Channel<LibraryContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: LibraryContract.Intent) {
        when (intent) {
            LibraryContract.Intent.LoadData              -> loadData()
            is LibraryContract.Intent.TabSelected        -> _state.update { it.copy(selectedTab = intent.tab) }
            is LibraryContract.Intent.PlaylistClicked    -> sendEffect(LibraryContract.Effect.NavigateToPlaylist(intent.playlistId))
            LibraryContract.Intent.AddClicked            -> sendEffect(LibraryContract.Effect.NavigateToCreatePlaylist)
            LibraryContract.Intent.SearchClicked         -> sendEffect(LibraryContract.Effect.NavigateToSearch)
            is LibraryContract.Intent.MoreOptionsClicked -> { /* ileride eklenecek */ }
            LibraryContract.Intent.SortClicked           -> { /* ileride eklenecek */ }
            LibraryContract.Intent.ViewToggleClicked     -> { /* ileride eklenecek */ }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            libraryRepository.getPlaylists()
                .onSuccess { playlists -> _state.update { it.copy(playlists = playlists) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: LibraryContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
