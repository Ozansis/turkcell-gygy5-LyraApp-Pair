package com.turkcell.lyraapp.data.playlistdetail

interface PlaylistDetailRepository {
    suspend fun getPlaylistInfo(playlistId: String): Result<PlaylistDetailInfo>
    suspend fun getPlaylistTracks(playlistId: String): Result<List<PlaylistDetailTrack>>
}
