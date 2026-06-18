package com.turkcell.lyraapp.ui.screens.nowplaying

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.data.nowplaying.NowPlayingTrack

@Composable
fun NowPlayingRoute(
    onNavigateBack: () -> Unit,
    onNavigateToNotification: () -> Unit,
    viewModel: NowPlayingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                NowPlayingContract.Effect.NavigateBack           -> onNavigateBack()
                NowPlayingContract.Effect.NavigateToNotification -> onNavigateToNotification()
            }
        }
    }

    NowPlayingScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun NowPlayingScreen(
    state: NowPlayingContract.State,
    onIntent: (NowPlayingContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val track = state.track

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        if (track != null) Color(track.coverStartColor).copy(alpha = 0.85f)
                        else MaterialTheme.colorScheme.surfaceContainerHigh,
                        MaterialTheme.colorScheme.surface,
                    )
                )
            ),
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            track != null   -> NowPlayingContent(track = track, onIntent = onIntent)
        }
    }
}

@Composable
private fun NowPlayingContent(
    track: NowPlayingTrack,
    onIntent: (NowPlayingContract.Intent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        NowPlayingTopBar(
            playlistName = track.playlistName,
            onBackClicked = { onIntent(NowPlayingContract.Intent.BackClicked) },
        )

        Spacer(modifier = Modifier.height(36.dp))

        AlbumArt(
            startColor = Color(track.coverStartColor),
            endColor = Color(track.coverEndColor),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
        )

        Spacer(modifier = Modifier.height(32.dp))

        TrackInfo(
            title = track.title,
            artist = track.artist,
            isFavorite = track.isFavorite,
            onFavoriteClicked = { onIntent(NowPlayingContract.Intent.FavoriteClicked) },
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProgressSection(
            progress = track.progress,
            currentPosition = track.currentPosition,
            duration = track.duration,
            onSeek = { onIntent(NowPlayingContract.Intent.SeekTo(it)) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlayerControls(
            isPlaying = track.isPlaying,
            isShuffled = track.isShuffled,
            isRepeating = track.isRepeating,
            onPlayPause = { onIntent(NowPlayingContract.Intent.PlayPauseClicked) },
            onSkipNext = { onIntent(NowPlayingContract.Intent.SkipNextClicked) },
            onSkipPrevious = { onIntent(NowPlayingContract.Intent.SkipPreviousClicked) },
            onShuffle = { onIntent(NowPlayingContract.Intent.ShuffleClicked) },
            onRepeat = { onIntent(NowPlayingContract.Intent.RepeatClicked) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        BottomActions(onArkaplanClicked = { onIntent(NowPlayingContract.Intent.ArkaplanClicked) })
    }
}

@Composable
private fun NowPlayingTopBar(
    playlistName: String,
    onBackClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                imageVector = Icons.Default.QueueMusic,
                contentDescription = "Geri",
                tint = Color.White.copy(alpha = 0.8f),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(
                text = "ŞİMDİ ÇALIYOR",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = androidx.compose.ui.unit.TextUnit(2f, androidx.compose.ui.unit.TextUnitType.Sp),
            )
            Text(
                text = playlistName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Daha fazla",
                tint = Color.White.copy(alpha = 0.8f),
            )
        }
    }
}

@Composable
private fun AlbumArt(
    startColor: Color,
    endColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(Brush.radialGradient(colors = listOf(startColor, endColor))),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val stroke = Stroke(width = 1.5.dp.toPx())
            drawCircle(color = Color.White.copy(alpha = 0.12f), radius = size.width * 0.60f, center = Offset(cx, cy), style = stroke)
            drawCircle(color = Color.White.copy(alpha = 0.10f), radius = size.width * 0.40f, center = Offset(cx, cy), style = stroke)
            drawCircle(color = Color.White.copy(alpha = 0.08f), radius = size.width * 0.22f, center = Offset(cx, cy), style = stroke)
        }
    }
}

@Composable
private fun TrackInfo(
    title: String,
    artist: String,
    isFavorite: Boolean,
    onFavoriteClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = artist,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
            )
        }
        IconButton(onClick = onFavoriteClicked) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favori",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun ProgressSection(
    progress: Float,
    currentPosition: String,
    duration: String,
    onSeek: (Float) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = progress,
            onValueChange = onSeek,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f),
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = currentPosition, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
            Text(text = duration, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun PlayerControls(
    isPlaying: Boolean,
    isShuffled: Boolean,
    isRepeating: Boolean,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onShuffle: () -> Unit,
    onRepeat: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = onShuffle) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = "Karıştır",
                tint = if (isShuffled) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp),
            )
        }
        IconButton(onClick = onSkipPrevious) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Önceki",
                tint = Color.White,
                modifier = Modifier.size(32.dp),
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
        ) {
            IconButton(onClick = onPlayPause) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Duraklat" else "Oynat",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp),
                )
            }
        }
        IconButton(onClick = onSkipNext) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Sonraki",
                tint = Color.White,
                modifier = Modifier.size(32.dp),
            )
        }
        IconButton(onClick = onRepeat) {
            Icon(
                imageVector = Icons.Default.Repeat,
                contentDescription = "Tekrarla",
                tint = if (isRepeating) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun BottomActions(onArkaplanClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.QueueMusic,
                contentDescription = "Kuyruk",
                tint = Color.White.copy(alpha = 0.7f),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(onClick = onArkaplanClicked) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsNone,
                    contentDescription = "Arkaplan",
                    tint = Color.White.copy(alpha = 0.7f),
                )
            }
            Text(
                text = "Arkaplan",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
            )
        }
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.PlaylistPlay,
                contentDescription = "Çalma listesi",
                tint = Color.White.copy(alpha = 0.7f),
            )
        }
    }
}
