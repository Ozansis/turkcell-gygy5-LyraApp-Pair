package com.turkcell.lyraapp.data.search

import javax.inject.Inject

class FakeSearchRepository @Inject constructor() : SearchRepository {

    private val allTracks = listOf(
        SearchResult("neon-sokaklar-1", "Neon Sokaklar", "Şehir Işıkları", "Pop",        0xFFD4742AL, 0xFFB85A10L),
        SearchResult("gece-yarisi-1",   "Gece Yarısı",   "Mavi Deniz",     "Elektronik", 0xFF2E7D4EL, 0xFF1A5C36L),
        SearchResult("yildiz-tozu-1",   "Yıldız Tozu",   "Polaris",        "Pop",        0xFF2D8A8AL, 0xFF1D6A6AL),
        SearchResult("mor-bulutlar-1",  "Mor Bulutlar",  "Derin Kaya",     "Akustik",    0xFF7A5A8AL, 0xFF5A3A6AL),
        SearchResult("son-tren-1",      "Son Tren",      "Peron",          "Indie",      0xFF4A3082L, 0xFF361E62L),
        SearchResult("ilk-isik-1",      "İlk Işık",      "Sabah Ezgisi",   "Akustik",    0xFF2A6E9EL, 0xFF1A5080L),
        SearchResult("derin-mavi-1",    "Derin Mavi",    "Okyanus",        "Lo-fi",      0xFF3A7A5CL, 0xFF255A42L),
        SearchResult("sessiz-sehir-1",  "Sessiz Şehir",  "Ela Tuna",       "Jazz",       0xFF4A8040L, 0xFF336030L),
        SearchResult("sahil-yolu-1",    "Sahil Yolu",    "Kumsal",         "Pop",        0xFFE08080L, 0xFFC06060L),
        SearchResult("kayip-anlar-1",   "Kayıp Anlar",   "Eko",            "Elektronik", 0xFF6A5AC8L, 0xFF4A3AA8L),
    )

    override fun getGenres() = listOf(
        GenreCategory("pop",       "Pop",       0xFF26C6C0L, 0xFF1AA8A2L),
        GenreCategory("elektronik","Elektronik",0xFF7B65D4L, 0xFF5E48B8L),
        GenreCategory("akustik",   "Akustik",   0xFF9870A8L, 0xFF7A5888L),
        GenreCategory("lofi",      "Lo-fi",     0xFF2A7A6AL, 0xFF1A5A4AL),
        GenreCategory("indie",     "Indie",     0xFF4A3082L, 0xFF361E62L),
        GenreCategory("jazz",      "Jazz",      0xFF4A8040L, 0xFF336030L),
        GenreCategory("klasik",    "Klasik",    0xFFC06898L, 0xFF9E4878L),
        GenreCategory("yolculuk",  "Yolculuk",  0xFFE07878L, 0xFFC05858L),
    )

    override suspend fun search(query: String): Result<List<SearchResult>> = Result.success(
        allTracks.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.artist.contains(query, ignoreCase = true)
        }
    )
}
