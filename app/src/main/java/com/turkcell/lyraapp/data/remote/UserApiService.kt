package com.turkcell.lyraapp.data.remote

import retrofit2.http.GET

data class UserMembershipDto(
    val planId: String,
    val type: String,
    val status: String,
    val autoRenew: Boolean,
    val startedAt: String,
    val expiresAt: String,
)

data class UserDto(
    val firstName: String,
    val lastName: String,
    val displayName: String?,
    val phone: String,
    val birthDate: String?,
    val profileCompleted: Boolean,
    val membership: UserMembershipDto?,
)

data class UserResponse(
    val data: UserDto,
)

interface UserApiService {
    @GET("api/v1/me")
    suspend fun getMe(): UserResponse
}
