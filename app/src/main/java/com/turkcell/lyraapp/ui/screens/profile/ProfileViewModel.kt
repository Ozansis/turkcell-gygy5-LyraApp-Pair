package com.turkcell.lyraapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.profile.ProfileRepository
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
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileContract.State())
    val state: StateFlow<ProfileContract.State> = _state.asStateFlow()

    private val _effect = Channel<ProfileContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onIntent(ProfileContract.Intent.LoadData)
    }

    fun onIntent(intent: ProfileContract.Intent) {
        when (intent) {
            ProfileContract.Intent.LoadData              -> loadData()
            is ProfileContract.Intent.SyncTheme         -> _state.update { it.copy(isDarkTheme = intent.isDark) }
            is ProfileContract.Intent.ThemeToggled       -> {
                _state.update { it.copy(isDarkTheme = intent.isDark) }
                sendEffect(ProfileContract.Effect.ThemeChanged(intent.isDark))
            }
            ProfileContract.Intent.AudioQualityClicked   -> sendEffect(ProfileContract.Effect.NavigateToAudioQuality)
            ProfileContract.Intent.OfflineDownloadClicked -> sendEffect(ProfileContract.Effect.NavigateToOfflineDownload)
            ProfileContract.Intent.NotificationsClicked  -> sendEffect(ProfileContract.Effect.NavigateToNotifications)
            ProfileContract.Intent.PrivacyClicked        -> sendEffect(ProfileContract.Effect.NavigateToPrivacy)
            ProfileContract.Intent.HelpAndSupportClicked -> sendEffect(ProfileContract.Effect.NavigateToHelpAndSupport)
            ProfileContract.Intent.SettingsClicked       -> sendEffect(ProfileContract.Effect.NavigateToSettings)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            profileRepository.getProfile()
                .onSuccess { profile -> _state.update { it.copy(profile = profile) } }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: ProfileContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
