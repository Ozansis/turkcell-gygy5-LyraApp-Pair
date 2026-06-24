package com.turkcell.lyraapp.data.auth

import com.turkcell.lyraapp.data.auth.dto.UpdateProfileBody
import com.turkcell.lyraapp.data.remote.AuthApiService
import javax.inject.Inject

class NetworkAuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override suspend fun requestOtp(phone: String): Result<OtpRequestResult> = runCatching {
        val response = authApiService.requestOtp(mapOf("phone" to phone))
        OtpRequestResult(firstTime = response.data.firstTime)
    }

    override suspend fun verifyOtp(phone: String, code: String): Result<AuthSessionResult> =
        runCatching {
            val response = authApiService.verifyOtp(mapOf("phone" to phone, "code" to code))
            sessionManager.saveTokens(
                accessToken = response.data.accessToken,
                refreshToken = response.data.refreshToken,
            )
            AuthSessionResult(firstTime = response.data.firstTime)
        }

    override suspend fun updateProfile(
        firstName: String,
        lastName: String,
        birthDate: String,
    ): Result<Unit> = runCatching {
        authApiService.updateProfile(
            UpdateProfileBody(
                firstName = firstName,
                lastName = lastName,
                birthDate = birthDate,
            )
        )
        Unit
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refreshToken = sessionManager.getRefreshToken() ?: return@runCatching
        authApiService.logout(mapOf("refreshToken" to refreshToken))
        sessionManager.clearTokens()
    }
}
