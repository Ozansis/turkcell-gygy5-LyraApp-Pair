package com.turkcell.lyraapp.data.home

import com.turkcell.lyraapp.data.remote.SongsApiService
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs

class NetworkHomeRepository @Inject constructor(
    private val songsApiService: SongsApiService,
) : HomeRepository {

    override fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 6..11  -> "Günaydın"
            in 12..16 -> "İyi günler"
            in 17..21 -> "İyi akşamlar"
            else      -> "İyi geceler"
        }
    }

    override fun getUserInitials(): String = "ZK"

    override suspend fun getMoodCategories(): Result<List<MoodCategory>> = Result.success(
        listOf(
            MoodCategory(id = "gece-surusu",    name = "Gece Sürüşü",    startColor = 0xFF7B61C8, endColor = 0xFF5A47A3),
            MoodCategory(id = "sabah-kahvesi",  name = "Sabah Kahvesi",  startColor = 0xFF9C6E8A, endColor = 0xFF7A4F6A),
            MoodCategory(id = "neon-sokaklar",  name = "Neon Sokaklar",  startColor = 0xFFD4742A, endColor = 0xFFB85A10),
            MoodCategory(id = "odaklan",        name = "Odaklan",        startColor = 0xFF3D8E82, endColor = 0xFF2A6D62),
            MoodCategory(id = "derin-mavi",     name = "Derin Mavi",     startColor = 0xFF3A7A5C, endColor = 0xFF255A42),
            MoodCategory(id = "yaz-anilari",    name = "Yaz Anıları",    startColor = 0xFF4EA356, endColor = 0xFF358540),
        )
    )

    override suspend fun getSongs(): Result<List<Track>> = runCatching {
        songsApiService.getSongs(limit = 20).data.map { dto ->
            val (start, end) = COLOR_PALETTE[abs(dto.id.hashCode()) % COLOR_PALETTE.size]
            Track(
                id              = dto.id,
                title           = dto.title,
                artist          = dto.artist,
                coverStartColor = start,
                coverEndColor   = end,
            )
        }
    }

    override suspend fun getRecentlyPlayed(): Result<List<Track>> = Result.success(emptyList())

    override suspend fun getRecommendedPlaylists(): Result<List<Playlist>> = Result.success(
        listOf(
            Playlist(id = "playlist-1", title = "Gece Sakinliği",   coverStartColor = 0xFF7B61C8, coverEndColor = 0xFF5A47A3),
            Playlist(id = "playlist-2", title = "Enerjik Başlangıç", coverStartColor = 0xFF9C6E8A, coverEndColor = 0xFF7A4F6A),
            Playlist(id = "playlist-3", title = "Odak Modu",         coverStartColor = 0xFF2D8A8A, coverEndColor = 0xFF1D6A6A),
            Playlist(id = "playlist-4", title = "Yaz Vibes",         coverStartColor = 0xFF4EA356, coverEndColor = 0xFF358540),
        )
    )

    companion object {
        private val COLOR_PALETTE = listOf(
            Pair(0xFF7B61C8L, 0xFF5A47A3L),
            Pair(0xFF9C6E8AL, 0xFF7A4F6AL),
            Pair(0xFFD4742AL, 0xFFB85A10L),
            Pair(0xFF3D8E82L, 0xFF2A6D62L),
            Pair(0xFF3A7A5CL, 0xFF255A42L),
            Pair(0xFF4EA356L, 0xFF358540L),
            Pair(0xFF2D8A8AL, 0xFF1D6A6AL),
            Pair(0xFF6B5B95L, 0xFF4A3A7AL),
            Pair(0xFFE07B54L, 0xFFC25A30L),
            Pair(0xFF5B8DB8L, 0xFF3A6A95L),
        )
    }
}
