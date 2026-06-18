package com.turkcell.lyraapp.ui.screens.playlistdetail

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.data.playlistdetail.PlaylistDetailTrack
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun PlaylistDetailRoute(
    onNavigateBack: () -> Unit = {},
    viewModel: PlaylistDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(PlaylistDetailContract.Intent.LoadData)
        viewModel.effect.collect { effect ->
            when (effect) {
                PlaylistDetailContract.Effect.NavigateBack          -> onNavigateBack()
                is PlaylistDetailContract.Effect.NavigateToPlayer   -> { /* ileride eklenecek */ }
            }
        }
    }

    PlaylistDetailScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun PlaylistDetailScreen(
    state: PlaylistDetailContract.State,
    onIntent: (PlaylistDetailContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        PlaylistDetailTopBar(onIntent = onIntent)
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            item {
                PlaylistDetailHeader(state = state, onIntent = onIntent)
            }
            items(items = state.tracks, key = { it.id }) { track ->
                TrackRow(track = track, onIntent = onIntent)
            }
        }
    }
}

@Composable
private fun PlaylistDetailTopBar(
    onIntent: (PlaylistDetailContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
    ) {
        IconButton(onClick = { onIntent(PlaylistDetailContract.Intent.BackClicked) }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { onIntent(PlaylistDetailContract.Intent.MoreOptionsClicked) }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun PlaylistDetailHeader(
    state: PlaylistDetailContract.State,
    onIntent: (PlaylistDetailContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        PlaylistCoverArt(
            startColor = state.coverStartColor,
            endColor = state.coverEndColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .padding(top = 8.dp, bottom = 24.dp),
        )
        Text(
            text = state.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = state.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${state.author} · ${state.trackCount} şarkı · ${state.totalDuration}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        PlaylistDetailActions(state = state, onIntent = onIntent)
    }
}

@Composable
private fun PlaylistCoverArt(
    startColor: Long,
    endColor: Long,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(startColor), Color(endColor)),
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
                color = Color.White.copy(alpha = 0.10f),
                radius = size.width * 0.35f,
                center = Offset(cx, cy),
                style = stroke,
            )
        }
    }
}

@Composable
private fun PlaylistDetailActions(
    state: PlaylistDetailContract.State,
    onIntent: (PlaylistDetailContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onIntent(PlaylistDetailContract.Intent.LikePlaylistClicked) }) {
                Icon(
                    imageVector = if (state.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = if (state.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = { onIntent(PlaylistDetailContract.Intent.DownloadClicked) }) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = { onIntent(PlaylistDetailContract.Intent.AddToLibraryClicked) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            IconButton(onClick = { onIntent(PlaylistDetailContract.Intent.ShuffleClicked) }) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onIntent(PlaylistDetailContract.Intent.PlayClicked) },
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp),
                )
            }
        }
    }
}

@Composable
private fun TrackRow(
    track: PlaylistDetailTrack,
    onIntent: (PlaylistDetailContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onIntent(PlaylistDetailContract.Intent.TrackClicked(track.id)) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        TrackCover(track = track)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (track.isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
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
        Text(
            text = track.duration,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp),
        )
        IconButton(
            onClick = { onIntent(PlaylistDetailContract.Intent.TrackLikeClicked(track.id)) },
            modifier = Modifier.size(36.dp),
        ) {
            Icon(
                imageVector = if (track.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = if (track.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
        IconButton(
            onClick = { onIntent(PlaylistDetailContract.Intent.TrackMoreOptionsClicked(track.id)) },
            modifier = Modifier.size(36.dp),
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun TrackCover(
    track: PlaylistDetailTrack,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(track.coverStartColor), Color(track.coverEndColor)),
                )
            ),
    ) {
        if (track.isPlaying) {
            Icon(
                imageVector = Icons.Default.Equalizer,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
