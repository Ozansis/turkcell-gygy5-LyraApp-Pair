package com.turkcell.lyraapp.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class RecentlyPlayedResponse(
    val data: List<SongDto>,
)

data class RecordPlayBody(
    val songId: String,
)

data class RecordPlayData(
    val recorded: Boolean,
)

data class RecordPlayResponse(
    val data: RecordPlayData,
)

data class SongListResponse(val data: List<SongDto>)

data class PlaybackNextBody(val songId: String)

data class PlaybackStreamDto(
    val url: String,
    val expiresAt: String,
    val mimeType: String,
)

data class PlaybackAdDto(
    val id: String,
    val title: String,
    val advertiser: String,
    val durationMs: Int,
    val mimeType: String,
)

data class PlaybackNextDataDto(
    val type: String,
    val song: SongDto? = null,
    val stream: PlaybackStreamDto? = null,
    val ad: PlaybackAdDto? = null,
    val adStream: PlaybackStreamDto? = null,
    val impressionId: String? = null,
)

data class PlaybackNextResponse(val data: PlaybackNextDataDto)

data class AdCompleteBody(val impressionId: String)

data class AdCompleteDataDto(val completed: Boolean)

data class AdCompleteResponse(val data: AdCompleteDataDto)

interface MeApiService {
    @GET("api/v1/me/recently-played")
    suspend fun getRecentlyPlayed(
        @Query("limit") limit: Int = 20,
    ): RecentlyPlayedResponse

    @POST("api/v1/me/plays")
    suspend fun recordPlay(@Body body: RecordPlayBody): RecordPlayResponse

    @GET("api/v1/me/recommendations")
    suspend fun getRecommendations(@Query("limit") limit: Int = 20): SongListResponse

    @GET("api/v1/me/for-you")
    suspend fun getForYou(@Query("limit") limit: Int = 20): SongListResponse

    @POST("api/v1/me/playback/next")
    suspend fun requestPlaybackNext(@Body body: PlaybackNextBody): PlaybackNextResponse

    @POST("api/v1/me/playback/ad-complete")
    suspend fun reportAdComplete(@Body body: AdCompleteBody): AdCompleteResponse
}
