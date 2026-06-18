package com.turkcell.lyraapp.data.createplaylist

interface CreatePlaylistRepository {
    suspend fun getAvailableTracks(): Result<List<AvailableTrack>>
    suspend fun createPlaylist(
        name: String,
        description: String,
        isPublic: Boolean,
        trackIds: List<String>,
    ): Result<Unit>
}
