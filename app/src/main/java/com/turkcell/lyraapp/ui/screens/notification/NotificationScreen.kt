package com.turkcell.lyraapp.ui.screens.notification

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val AmberStart = Color(0xFFD4742A)
private val AmberEnd   = Color(0xFFB85A10)
private val PanelBg    = Color(0xFF1A1A1A)
private val CardBg     = Color(0xFF2A2A2A)
private val PinkAccent = Color(0xFFFFB1C8)

@Composable
fun NotificationRoute() {
    NotificationScreen()
}

@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PanelBg)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        StatusBar()

        Spacer(modifier = Modifier.height(20.dp))

        QuickSettings()

        Spacer(modifier = Modifier.height(16.dp))

        MediaNotificationCard()

        Spacer(modifier = Modifier.height(10.dp))

        DownloadNotificationCard()
    }
}

@Composable
private fun StatusBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Çar, 4 Haziran",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )
            Text(
                text = "9:41",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Light,
                color = Color.White,
            )
        }
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun QuickSettings() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        QuickSettingTile(icon = Icons.Default.Wifi,             label = "Wi-Fi",         active = true)
        QuickSettingTile(icon = Icons.Default.Bluetooth,        label = "Bluetooth",     active = true)
        QuickSettingTile(icon = Icons.Default.DarkMode,         label = "Koyu tema",     active = true)
        QuickSettingTile(icon = Icons.Default.NotificationsOff, label = "Rahatsız etme", active = false)
    }
}

@Composable
private fun QuickSettingTile(
    icon: ImageVector,
    label: String,
    active: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(if (active) PinkAccent else Color.White.copy(alpha = 0.15f)),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (active) Color(0xFF5E1133) else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f),
        )
    }
}

@Composable
private fun MediaNotificationCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colors = listOf(AmberStart, AmberEnd)))
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "LyraApp · Şimdi çalıyor",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            MediaAlbumThumb()
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Neon Sokaklar",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    text = "Şehir Işıkları · Gece Vardiyası",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = Icons.Default.Favorite,    contentDescription = "Favori",  tint = PinkAccent, modifier = Modifier.size(22.dp))
            Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "Önceki", tint = Color.White, modifier = Modifier.size(22.dp))
            Icon(imageVector = Icons.Default.Pause,        contentDescription = "Duraklat", tint = Color.White, modifier = Modifier.size(22.dp))
            Icon(imageVector = Icons.Default.SkipNext,     contentDescription = "Sonraki", tint = Color.White, modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "1:33", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.width(8.dp))
            LinearProgressIndicator(
                progress = { 0.42f },
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "3:43", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun MediaAlbumThumb() {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.radialGradient(colors = listOf(AmberStart.copy(alpha = 0.6f), AmberEnd))),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val stroke = Stroke(width = 1.dp.toPx())
            drawCircle(color = Color.White.copy(alpha = 0.2f), radius = size.width * 0.45f, center = Offset(cx, cy), style = stroke)
            drawCircle(color = Color.White.copy(alpha = 0.15f), radius = size.width * 0.28f, center = Offset(cx, cy), style = stroke)
        }
    }
}

@Composable
private fun DownloadNotificationCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f)),
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "İndirme tamamlandı",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
            Text(
                text = "'Gece Sürüşü' çevrimdışı kullanıma hazır",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f),
            )
        }
    }
}
