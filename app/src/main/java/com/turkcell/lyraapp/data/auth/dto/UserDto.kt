package com.turkcell.lyraapp.data.auth.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("profileCompleted") val profileCompleted: Boolean,
)
