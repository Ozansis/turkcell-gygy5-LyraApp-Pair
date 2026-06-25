package com.turkcell.lyraapp.data.home

import java.util.Calendar
import javax.inject.Inject

class FakeHomeRepository @Inject constructor() : HomeRepository {

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
            MoodCategory(id = "gece-surusu",   name = "Gece Sürüşü",   startColor = 0xFF7B61C8, endColor = 0xFF5A47A3),
            MoodCategory(id = "sabah-kahvesi", name = "Sabah Kahvesi", startColor = 0xFF9C6E8A, endColor = 0xFF7A4F6A),
            MoodCategory(id = "neon-sokaklar", name = "Neon Sokaklar", startColor = 0xFFD4742A, endColor = 0xFFB85A10),
            MoodCategory(id = "odaklan",       name = "Odaklan",       startColor = 0xFF3D8E82, endColor = 0xFF2A6D62),
            MoodCategory(id = "derin-mavi",    name = "Derin Mavi",    startColor = 0xFF3A7A5C, endColor = 0xFF255A42),
            MoodCategory(id = "yaz-anilari",   name = "Yaz Anıları",   startColor = 0xFF4EA356, endColor = 0xFF358540),
        )
    )

    override suspend fun getSongs(): Result<List<Track>> = Result.success(emptyList())

    override suspend fun getRecentlyPlayed(): Result<List<Track>> = Result.success(
        listOf(
            Track(id = "neon-sokaklar-1", title = "Neon Sokaklar",  artist = "Şehir Işıkları", coverStartColor = 0xFFD4742A, coverEndColor = 0xFFB85A10),
            Track(id = "derin-mavi-1",    title = "Derin Mavi",     artist = "Okyanus",         coverStartColor = 0xFF3A7A5C, coverEndColor = 0xFF255A42),
            Track(id = "yildiz-tozu-1",   title = "Yıldız Tozu",   artist = "Polaris",          coverStartColor = 0xFF2D8A8A, coverEndColor = 0xFF1D6A6A),
            Track(id = "mor-yollar-1",    title = "Mor Yollar",     artist = "Gece Yarısı",     coverStartColor = 0xFF7B61C8, coverEndColor = 0xFF5A47A3),
        )
    )

    override suspend fun getRecommendations(): Result<List<Track>> = Result.success(
        listOf(
            Track(id = "rec-1", title = "Gece Sakinliği",   artist = "Ambient",  coverStartColor = 0xFF7B61C8, coverEndColor = 0xFF5A47A3),
            Track(id = "rec-2", title = "Enerjik Başlangıç", artist = "Ritim",   coverStartColor = 0xFF9C6E8A, coverEndColor = 0xFF7A4F6A),
            Track(id = "rec-3", title = "Odak Modu",         artist = "Frekans", coverStartColor = 0xFF2D8A8A, coverEndColor = 0xFF1D6A6A),
            Track(id = "rec-4", title = "Yaz Vibes",         artist = "Güneş",   coverStartColor = 0xFF4EA356, coverEndColor = 0xFF358540),
        )
    )
}
