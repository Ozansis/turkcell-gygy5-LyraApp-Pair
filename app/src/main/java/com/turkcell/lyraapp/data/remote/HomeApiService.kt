package com.turkcell.lyraapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class SongListResponse(val data: List<SongDto>)

interface HomeApiService {
    @GET("api/v1/me/recommendations")
    suspend fun getRecommendations(@Query("limit") limit: Int = 20): SongListResponse
}
