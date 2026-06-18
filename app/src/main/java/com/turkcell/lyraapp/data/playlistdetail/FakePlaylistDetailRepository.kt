package com.turkcell.lyraapp.data.playlistdetail

import javax.inject.Inject

class FakePlaylistDetailRepository @Inject constructor() : PlaylistDetailRepository {

    override suspend fun getPlaylistInfo(playlistId: String): Result<PlaylistDetailInfo> =
        Result.success(
            PlaylistDetailInfo(
                title           = "Gece Sürüşü",
                description     = "Karanlık yollar için synth-pop",
                author          = "Zeynep Kaya",
                trackCount      = 6,
                totalDuration   = "23 dk",
                coverStartColor = 0xFF6B4FA0L,
                coverEndColor   = 0xFF4A3580L,
                isLiked         = false,
            )
        )

    override suspend fun getPlaylistTracks(playlistId: String): Result<List<PlaylistDetailTrack>> =
        Result.success(
            listOf(
                PlaylistDetailTrack(
                    id              = "neon_sokaklar",
                    title           = "Neon Sokaklar",
                    artist          = "Şehir Işıkları",
                    duration        = "3:43",
                    coverStartColor = 0xFF8B6040L,
                    coverEndColor   = 0xFF6B4030L,
                    isLiked         = true,
                    isPlaying       = true,
                ),
                PlaylistDetailTrack(
                    id              = "gece_yarisi",
                    title           = "Gece Yarısı",
                    artist          = "Mavi Deniz",
                    duration        = "3:34",
                    coverStartColor = 0xFF3D8050L,
                    coverEndColor   = 0xFF2A5E3AL,
                    isLiked         = true,
                ),
                PlaylistDetailTrack(
                    id              = "mor_bulutlar",
                    title           = "Mor Bulutlar",
                    artist          = "Derin Kaya",
                    duration        = "3:52",
                    coverStartColor = 0xFF3A7A9EL,
                    coverEndColor   = 0xFF2A5A7EL,
                    isLiked         = false,
                ),
                PlaylistDetailTrack(
                    id              = "son_tren",
                    title           = "Son Tren",
                    artist          = "Peron",
                    duration        = "3:37",
                    coverStartColor = 0xFF2E9E7EL,
                    coverEndColor   = 0xFF1E7A5EL,
                    isLiked         = true,
                ),
                PlaylistDetailTrack(
                    id              = "yildiz_tozu",
                    title           = "Yıldız Tozu",
                    artist          = "Polaris",
                    duration        = "4:07",
                    coverStartColor = 0xFF3A8E9EL,
                    coverEndColor   = 0xFF2A6E7EL,
                    isLiked         = true,
                ),
            )
        )
}
