package com.turkcell.lyraapp.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.data.home.MoodCategory
import com.turkcell.lyraapp.data.home.Playlist
import com.turkcell.lyraapp.data.home.Track

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.LoadData)
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToPlayer   -> { /* ileride eklenecek */ }
                is HomeEffect.NavigateToPlaylist -> { /* ileride eklenecek */ }
                is HomeEffect.NavigateToCategory -> { /* ileride eklenecek */ }
            }
        }
    }

    HomeScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            HomeHeader(
                greeting = state.greeting,
                userInitials = state.userInitials,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            )
        }

        when {
            state.isLoading -> item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                ) {
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            else -> {
                if (state.moodCategories.isNotEmpty()) {
                    item {
                        MoodCategoriesSection(
                            categories = state.moodCategories,
                            onIntent = onIntent,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }

                if (state.recentlyPlayed.isNotEmpty()) {
                    item {
                        RecentlyPlayedSection(
                            tracks = state.recentlyPlayed,
                            onIntent = onIntent,
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }

                if (state.recommendedPlaylists.isNotEmpty()) {
                    item {
                        RecommendedPlaylistsSection(
                            playlists = state.recommendedPlaylists,
                            onIntent = onIntent,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    greeting: String,
    userInitials: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Ne dinlemek istersin?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.WbSunny,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
            )
            UserAvatar(initials = userInitials)
        }
    }
}

@Composable
private fun UserAvatar(
    initials: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun MoodCategoriesSection(
    categories: List<MoodCategory>,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rows = categories.chunked(2)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { category ->
                    MoodCategoryCard(
                        category = category,
                        onClick = { onIntent(HomeIntent.MoodCategoryClicked(category)) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MoodCategoryCard(
    category: MoodCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(category.startColor),
                        Color(category.endColor),
                    ),
                )
            )
            .clickable { onClick() },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width * 0.85f
            val cy = size.height / 2f
            val stroke = Stroke(width = 1.5.dp.toPx())
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = size.width * 0.55f,
                center = Offset(cx, cy),
                style = stroke,
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = size.width * 0.35f,
                center = Offset(cx, cy),
                style = stroke,
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.10f),
                radius = size.width * 0.18f,
                center = Offset(cx, cy),
                style = stroke,
            )
        }
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 12.dp, vertical = 10.dp),
        )
    }
}

@Composable
private fun RecentlyPlayedSection(
    tracks: List<Track>,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Son çalınanlar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Tümü",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onIntent(HomeIntent.SeeAllRecentlyPlayedClicked) },
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items = tracks, key = { it.id }) { track ->
                TrackCard(
                    track = track,
                    onClick = { onIntent(HomeIntent.TrackClicked(track)) },
                )
            }
        }
    }
}

@Composable
private fun TrackCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(120.dp)
            .clickable { onClick() },
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(track.coverStartColor),
                            Color(track.coverEndColor),
                        ),
                    )
                ),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width * 0.5f
                val cy = size.height * 0.5f
                val stroke = Stroke(width = 1.5.dp.toPx())
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = size.width * 0.55f,
                    center = Offset(cx, cy),
                    style = stroke,
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.12f),
                    radius = size.width * 0.35f,
                    center = Offset(cx, cy),
                    style = stroke,
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = track.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = track.artist,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RecommendedPlaylistsSection(
    playlists: List<Playlist>,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Senin için çalma listeleri",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items = playlists, key = { it.id }) { playlist ->
                PlaylistCard(
                    playlist = playlist,
                    onClick = { onIntent(HomeIntent.PlaylistClicked(playlist)) },
                )
            }
        }
    }
}

@Composable
private fun PlaylistCard(
    playlist: Playlist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(130.dp)
            .clickable { onClick() },
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(playlist.coverStartColor),
                            Color(playlist.coverEndColor),
                        ),
                    )
                ),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width * 0.5f
                val cy = size.height * 0.5f
                val stroke = Stroke(width = 1.5.dp.toPx())
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = size.width * 0.6f,
                    center = Offset(cx, cy),
                    style = stroke,
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.10f),
                    radius = size.width * 0.38f,
                    center = Offset(cx, cy),
                    style = stroke,
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = playlist.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
