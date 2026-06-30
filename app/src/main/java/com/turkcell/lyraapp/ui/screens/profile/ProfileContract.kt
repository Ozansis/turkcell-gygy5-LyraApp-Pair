package com.turkcell.lyraapp.ui.screens.profile

import com.turkcell.lyraapp.data.profile.UserProfile

object ProfileContract {

    data class State(
        val profile: UserProfile? = null,
        val isDarkTheme: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data object LoadData : Intent
        data class SyncTheme(val isDark: Boolean) : Intent
        data class ThemeToggled(val isDark: Boolean) : Intent
        data object AudioQualityClicked : Intent
        data object OfflineDownloadClicked : Intent
        data object NotificationsClicked : Intent
        data object PrivacyClicked : Intent
        data object HelpAndSupportClicked : Intent
        data object SettingsClicked : Intent
        data object NavigateToMembershipClicked : Intent
    }

    sealed interface Effect {
        data class ThemeChanged(val isDark: Boolean) : Effect
        data object NavigateToAudioQuality : Effect
        data object NavigateToOfflineDownload : Effect
        data object NavigateToNotifications : Effect
        data object NavigateToPrivacy : Effect
        data object NavigateToHelpAndSupport : Effect
        data object NavigateToSettings : Effect
        data object NavigateToMembership : Effect
    }
}
