package com.turkcell.lyraapp.data.auth.dto

import com.google.gson.annotations.SerializedName

data class AuthSessionDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("expiresIn") val expiresIn: Int,
    @SerializedName("user") val user: UserDto,
    @SerializedName("firstTime") val firstTime: Boolean,
)

data class AuthSessionResponseDto(
    @SerializedName("data") val data: AuthSessionDto,
)
