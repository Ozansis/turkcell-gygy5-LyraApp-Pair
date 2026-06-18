package com.turkcell.lyraapp.data.profile

import javax.inject.Inject

class FakeProfileRepository @Inject constructor() : ProfileRepository {

    override suspend fun getProfile(): Result<UserProfile> = Result.success(
        UserProfile(
            name                  = "Zeynep Kaya",
            handle                = "@zeynepk",
            isPremium             = true,
            initials              = "ZK",
            avatarStartColor      = 0xFF8B5E6EL,
            avatarEndColor        = 0xFF6B3E50L,
            playlistCount         = 127,
            followerCount         = "1.2B",
            followingCount        = 348,
            audioQuality          = "Yüksek",
            offlineDownloadEnabled = true,
        )
    )
}
