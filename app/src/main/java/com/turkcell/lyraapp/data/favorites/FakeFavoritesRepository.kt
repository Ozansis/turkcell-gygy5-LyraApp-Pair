package com.turkcell.lyraapp.data.favorites

import javax.inject.Inject

class FakeFavoritesRepository @Inject constructor() : FavoritesRepository {

    override suspend fun getFavoriteTracks(): Result<List<FavoriteTrack>> = Result.success(
        listOf(
            FavoriteTrack(
                id              = "gece-yarisi-1",
                title           = "Gece Yarısı",
                artist          = "Mavi Deniz",
                duration        = "3:34",
                coverStartColor = 0xFF2E7D4EL,
                coverEndColor   = 0xFF1A5C36L,
            ),
            FavoriteTrack(
                id              = "yildiz-tozu-1",
                title           = "Yıldız Tozu",
                artist          = "Polaris",
                duration        = "4:07",
                coverStartColor = 0xFF2D8A8AL,
                coverEndColor   = 0xFF1D6A6AL,
            ),
            FavoriteTrack(
                id              = "ilk-isik-1",
                title           = "İlk Işık",
                artist          = "Sabah Ezgisi",
                duration        = "3:25",
                coverStartColor = 0xFF2A6E9EL,
                coverEndColor   = 0xFF1A5080L,
            ),
            FavoriteTrack(
                id              = "neon-sokaklar-1",
                title           = "Neon Sokaklar",
                artist          = "Şehir Işıkları",
                duration        = "3:43",
                coverStartColor = 0xFFD4742AL,
                coverEndColor   = 0xFFB85A10L,
                isPlaying       = true,
            ),
            FavoriteTrack(
                id              = "derin-mavi-1",
                title           = "Derin Mavi",
                artist          = "Okyanus",
                duration        = "4:29",
                coverStartColor = 0xFF3A7A5CL,
                coverEndColor   = 0xFF255A42L,
            ),
        )
    )
}
