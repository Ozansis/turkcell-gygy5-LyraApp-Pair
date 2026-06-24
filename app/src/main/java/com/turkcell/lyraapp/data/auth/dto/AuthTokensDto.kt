package com.turkcell.lyraapp.data.auth.dto

import com.google.gson.annotations.SerializedName

data class AuthTokensDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("expiresIn") val expiresIn: Int,
)

data class AuthTokensResponseDto(
    @SerializedName("data") val data: AuthTokensDto,
)
