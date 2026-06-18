package com.turkcell.lyraapp.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.data.library.LibraryPlaylist
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun LibraryRoute(
    onNavigateToPlaylist: (String) -> Unit = {},
    onNavigateToCreatePlaylist: () -> Unit = {},
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(LibraryContract.Intent.LoadData)
        viewModel.effect.collect { effect ->
            when (effect) {
                is LibraryContract.Effect.NavigateToPlaylist    -> onNavigateToPlaylist(effect.playlistId)
                LibraryContract.Effect.NavigateToCreatePlaylist -> onNavigateToCreatePlaylist()
                LibraryContract.Effect.NavigateToSearch        -> { /* ileride eklenecek */ }
            }
        }
    }

    LibraryScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun LibraryScreen(
    state: LibraryContract.State,
    onIntent: (LibraryContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        LibraryTopBar(onIntent = onIntent)
        LibraryTabRow(selectedTab = state.selectedTab, onIntent = onIntent)
        LibrarySortRow(onIntent = onIntent)
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 4.dp),
        ) {
            items(items = state.playlists, key = { it.id }) { item ->
                LibraryPlaylistRow(item = item, onIntent = onIntent)
            }
        }
    }
}

@Composable
private fun LibraryTopBar(
    onIntent: (LibraryContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 4.dp, top = 20.dp, bottom = 12.dp),
    ) {
        Text(
            text = "Kütüphane",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { onIntent(LibraryContract.Intent.SearchClicked) }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        IconButton(onClick = { onIntent(LibraryContract.Intent.AddClicked) }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun LibraryTabRow(
    selectedTab: LibraryTab,
    onIntent: (LibraryContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
    ) {
        listOf(LibraryTab.Playlists, LibraryTab.Artists, LibraryTab.Albums).forEach { tab ->
            LibraryTabChip(
                label = tab.label,
                selected = selectedTab == tab,
                onClick = { onIntent(LibraryContract.Intent.TabSelected(tab)) },
            )
        }
    }
}

@Composable
private fun LibraryTabChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelLarge) },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            }
        } else {
            null
        },
        modifier = modifier,
    )
}

private val LibraryTab.label: String
    get() = when (this) {
        LibraryTab.Playlists -> "Çalma listeleri"
        LibraryTab.Artists   -> "Sanatçılar"
        LibraryTab.Albums    -> "Albümler"
    }

@Composable
private fun LibrarySortRow(
    onIntent: (LibraryContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable { onIntent(LibraryContract.Intent.SortClicked) },
        ) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = "Son eklenenler",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onIntent(LibraryContract.Intent.ViewToggleClicked) },
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                imageVector = Icons.Default.GridView,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun LibraryPlaylistRow(
    item: LibraryPlaylist,
    onIntent: (LibraryContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onIntent(LibraryContract.Intent.PlaylistClicked(item.id)) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        PlaylistCover(item = item)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Çalma listesi · ${item.trackCount} şarkı",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (item.isPinned) {
            Icon(
                imageVector = Icons.Default.PushPin,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.dp),
            )
        } else {
            IconButton(
                onClick = { onIntent(LibraryContract.Intent.MoreOptionsClicked(item.id)) },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun PlaylistCover(
    item: LibraryPlaylist,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(item.coverStartColor), Color(item.coverEndColor)),
                )
            ),
    ) {
        if (item.showHeartOnCover) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}
