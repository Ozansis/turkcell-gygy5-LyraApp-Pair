package com.turkcell.lyraapp.data.createplaylist

import javax.inject.Inject

class FakeCreatePlaylistRepository @Inject constructor() : CreatePlaylistRepository {

    override suspend fun getAvailableTracks(): Result<List<AvailableTrack>> = Result.success(
        listOf(
            AvailableTrack("gece_yarisi",  "Gece Yarısı",  "Mavi Deniz",     0xFF3D8050L, 0xFF2A5E3AL),
            AvailableTrack("sessiz_sehir", "Sessiz Şehir", "Ela Tuna",       0xFF7B5CB8L, 0xFF5A3A9AL),
            AvailableTrack("yildiz_tozu",  "Yıldız Tozu",  "Polaris",        0xFF3A8E9EL, 0xFF2A6E7EL),
            AvailableTrack("sahil_yolu",   "Sahil Yolu",   "Kumsal",         0xFFC07060L, 0xFF9A4030L),
            AvailableTrack("mor_bulutlar", "Mor Bulutlar", "Derin Kaya",     0xFF3A7A9EL, 0xFF2A5A7EL),
            AvailableTrack("ilk_isik",     "İlk Işık",     "Sabah Ezgisi",   0xFF2E9E7EL, 0xFF1E7A5EL),
            AvailableTrack("kayip_anlar",  "Kayıp Anlar",  "Eko",            0xFF3A9E8EL, 0xFF2A7A6EL),
        )
    )

    override suspend fun createPlaylist(
        name: String,
        description: String,
        isPublic: Boolean,
        trackIds: List<String>,
    ): Result<Unit> = Result.success(Unit)
}
