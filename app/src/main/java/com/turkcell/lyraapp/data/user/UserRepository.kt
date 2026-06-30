package com.turkcell.lyraapp.data.user

import com.turkcell.lyraapp.data.profile.UserProfile

interface UserRepository {
    suspend fun getUser(): Result<UserProfile>
}
