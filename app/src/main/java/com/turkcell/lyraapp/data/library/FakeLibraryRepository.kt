package com.turkcell.lyraapp.data.library

import javax.inject.Inject

class FakeLibraryRepository @Inject constructor() : LibraryRepository {

    override suspend fun getPlaylists(): Result<List<LibraryPlaylist>> = Result.success(
        listOf(
            LibraryPlaylist(
                id = "favorites",
                title = "Beğenilen Şarkılar",
                trackCount = 5,
                coverStartColor = 0xFFD4738AL,
                coverEndColor = 0xFFC45A75L,
                isPinned = true,
                showHeartOnCover = true,
            ),
            LibraryPlaylist(
                id = "night_drive",
                title = "Gece Sürüşü",
                trackCount = 6,
                coverStartColor = 0xFF6B4FA0L,
                coverEndColor = 0xFF4A3580L,
            ),
            LibraryPlaylist(
                id = "morning_coffee",
                title = "Sabah Kahvesi",
                trackCount = 5,
                coverStartColor = 0xFF8B5CB8L,
                coverEndColor = 0xFF6B40A0L,
            ),
            LibraryPlaylist(
                id = "focus",
                title = "Odaklan",
                trackCount = 5,
                coverStartColor = 0xFF2E7D6EL,
                coverEndColor = 0xFF1B5A50L,
            ),
            LibraryPlaylist(
                id = "summer_memories",
                title = "Yaz Anıları",
                trackCount = 5,
                coverStartColor = 0xFF3A8E9EL,
                coverEndColor = 0xFF2A6E7EL,
            ),
            LibraryPlaylist(
                id = "acoustic_evening",
                title = "Akustik Akşam",
                trackCount = 4,
                coverStartColor = 0xFF3A7A9EL,
                coverEndColor = 0xFF2A5A7EL,
            ),
        )
    )
}
