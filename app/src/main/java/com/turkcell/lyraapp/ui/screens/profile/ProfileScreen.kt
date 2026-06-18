package com.turkcell.lyraapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.turkcell.lyraapp.ui.theme.LocalThemeController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.data.profile.UserProfile

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val themeController = LocalThemeController.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileContract.Intent.SyncTheme(themeController.isDarkTheme))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileContract.Effect.ThemeChanged           -> themeController.setDarkTheme(effect.isDark)
                ProfileContract.Effect.NavigateToAudioQuality    -> Unit
                ProfileContract.Effect.NavigateToOfflineDownload -> Unit
                ProfileContract.Effect.NavigateToNotifications   -> Unit
                ProfileContract.Effect.NavigateToPrivacy         -> Unit
                ProfileContract.Effect.NavigateToHelpAndSupport  -> Unit
                ProfileContract.Effect.NavigateToSettings        -> Unit
            }
        }
    }

    ProfileScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun ProfileScreen(
    state: ProfileContract.State,
    onIntent: (ProfileContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val profile = state.profile ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
    ) {
        ProfileTopBar(onIntent = onIntent)
        ProfileAvatarSection(profile = profile)
        Spacer(modifier = Modifier.height(24.dp))
        ProfileThemeToggle(isDarkTheme = state.isDarkTheme, onIntent = onIntent)
        Spacer(modifier = Modifier.height(8.dp))
        ProfileSettingsMenu(profile = profile, onIntent = onIntent)
    }
}

@Composable
private fun ProfileTopBar(
    onIntent: (ProfileContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = "Profil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { onIntent(ProfileContract.Intent.SettingsClicked) }) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Ayarlar",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun ProfileAvatarSection(
    profile: UserProfile,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(profile.avatarStartColor),
                            Color(profile.avatarEndColor),
                        )
                    )
                ),
        ) {
            Text(
                text = profile.initials,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = profile.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = profile.handle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (profile.isPremium) {
                Text(
                    text = " · Premium",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            StatItem(value = profile.playlistCount.toString(), label = "Çalma listesi")
            StatItem(value = profile.followerCount, label = "Takipçi")
            StatItem(value = profile.followingCount.toString(), label = "Takip")
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProfileThemeToggle(
    isDarkTheme: Boolean,
    onIntent: (ProfileContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "Görünüm",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            ThemeToggleButton(
                label = "Açık",
                icon = Icons.Outlined.LightMode,
                isSelected = !isDarkTheme,
                onClick = { onIntent(ProfileContract.Intent.ThemeToggled(isDark = false)) },
                modifier = Modifier.weight(1f),
            )
            ThemeToggleButton(
                label = "Koyu",
                icon = Icons.Outlined.DarkMode,
                isSelected = isDarkTheme,
                onClick = { onIntent(ProfileContract.Intent.ThemeToggled(isDark = true)) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ThemeToggleButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor    = if (isSelected) MaterialTheme.colorScheme.onPrimary
                          else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = contentColor,
        )
    }
}

@Composable
private fun ProfileSettingsMenu(
    profile: UserProfile,
    onIntent: (ProfileContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SettingsRow(
            icon = Icons.Outlined.GraphicEq,
            label = "Ses kalitesi",
            trailingValue = profile.audioQuality,
            onClick = { onIntent(ProfileContract.Intent.AudioQualityClicked) },
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        SettingsRow(
            icon = Icons.Outlined.Download,
            label = "Çevrimdışı indirme",
            trailingValue = if (profile.offlineDownloadEnabled) "Açık" else "Kapalı",
            onClick = { onIntent(ProfileContract.Intent.OfflineDownloadClicked) },
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        SettingsRow(
            icon = Icons.Outlined.NotificationsNone,
            label = "Bildirimler",
            onClick = { onIntent(ProfileContract.Intent.NotificationsClicked) },
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        SettingsRow(
            icon = Icons.Outlined.Lock,
            label = "Gizlilik",
            onClick = { onIntent(ProfileContract.Intent.PrivacyClicked) },
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        SettingsRow(
            icon = Icons.Outlined.HelpOutline,
            label = "Yardım ve destek",
            onClick = { onIntent(ProfileContract.Intent.HelpAndSupportClicked) },
        )
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingValue: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (trailingValue != null) {
            Text(
                text = trailingValue,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
    }
}
