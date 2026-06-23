package com.turkcell.lyraapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class SongDto(
    val id: String,
    val title: String,
    val artist: String,
    val album: String?,
    val durationMs: Int,
    val mimeType: String,
    val sizeBytes: Int,
    val createdAt: String,
)

data class SongsResponse(
    val data: List<SongDto>,
    val nextCursor: String?,
)

interface SongsApiService {
    @GET("api/v1/songs")
    suspend fun getSongs(
        @Query("limit") limit: Int = 20,
        @Query("cursor") cursor: String? = null,
        @Query("q") query: String? = null,
    ): SongsResponse
}
