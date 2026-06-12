package com.turkcell.lyraapp.data.auth

import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {

    override suspend fun login(phoneNumber: String, password: String): Result<Unit> {
        delay(1_000)
        return if (phoneNumber.isNotBlank() && password.length >= 6) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Hatalı numara veya şifre"))
        }
    }
}
