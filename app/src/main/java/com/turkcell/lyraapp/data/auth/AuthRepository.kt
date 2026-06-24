package com.turkcell.lyraapp.data.auth

interface AuthRepository {
    suspend fun requestOtp(phone: String): Result<OtpRequestResult>
    suspend fun verifyOtp(phone: String, code: String): Result<AuthSessionResult>
    suspend fun updateProfile(firstName: String, lastName: String, birthDate: String): Result<Unit>
    suspend fun logout(): Result<Unit>
}

data class OtpRequestResult(val firstTime: Boolean)
data class AuthSessionResult(val firstTime: Boolean)
