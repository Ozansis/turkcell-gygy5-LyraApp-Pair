package com.turkcell.lyraapp.data.library

interface LibraryRepository {
    suspend fun getPlaylists(): Result<List<LibraryPlaylist>>
}
