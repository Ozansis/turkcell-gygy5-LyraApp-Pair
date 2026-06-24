package com.turkcell.lyraapp.data.auth.dto

import com.google.gson.annotations.SerializedName

data class OtpRequestData(
    @SerializedName("sent") val sent: Boolean,
    @SerializedName("firstTime") val firstTime: Boolean,
)

data class OtpRequestResponseDto(
    @SerializedName("data") val data: OtpRequestData,
)

data class UpdateProfileBody(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("birthDate") val birthDate: String,
)

data class UserResponseDto(
    @SerializedName("data") val data: UserDto,
)
