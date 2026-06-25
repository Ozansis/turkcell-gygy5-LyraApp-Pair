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

interface MeApiService {
    @GET("api/v1/me/recently-played")
    suspend fun getRecentlyPlayed(
        @Query("limit") limit: Int = 20,
    ): RecentlyPlayedResponse

    @POST("api/v1/me/plays")
    suspend fun recordPlay(@Body body: RecordPlayBody): RecordPlayResponse
}
