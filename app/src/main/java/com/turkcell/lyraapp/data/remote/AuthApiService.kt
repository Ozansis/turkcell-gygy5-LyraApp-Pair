package com.turkcell.lyraapp.data.remote

import com.turkcell.lyraapp.data.auth.dto.AuthSessionResponseDto
import com.turkcell.lyraapp.data.auth.dto.AuthTokensResponseDto
import com.turkcell.lyraapp.data.auth.dto.OtpRequestResponseDto
import com.turkcell.lyraapp.data.auth.dto.UpdateProfileBody
import com.turkcell.lyraapp.data.auth.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/otp/request")
    suspend fun requestOtp(@Body body: Map<String, String>): OtpRequestResponseDto

    @POST("api/v1/auth/otp/verify")
    suspend fun verifyOtp(@Body body: Map<String, String>): AuthSessionResponseDto

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Body body: Map<String, String>): AuthTokensResponseDto

    @POST("api/v1/auth/logout")
    suspend fun logout(@Body body: Map<String, String>): Response<Unit>

    @POST("api/v1/me/update-informations")
    suspend fun updateProfile(@Body body: UpdateProfileBody): UserResponseDto
}
