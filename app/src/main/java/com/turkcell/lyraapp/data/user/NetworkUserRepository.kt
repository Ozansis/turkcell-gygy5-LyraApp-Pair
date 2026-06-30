package com.turkcell.lyraapp.data.user

import com.turkcell.lyraapp.data.profile.UserProfile
import com.turkcell.lyraapp.data.remote.UserApiService
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class NetworkUserRepository @Inject constructor(
    private val userApiService: UserApiService,
) : UserRepository {

    override suspend fun getUser(): Result<UserProfile> = runCatching {
        val dto = userApiService.getMe().data
        val membership = dto.membership

        val daysLeft = membership?.expiresAt?.let { expiresAt ->
            runCatching {
                val expires = LocalDate.parse(expiresAt.substring(0, 10))
                ChronoUnit.DAYS.between(LocalDate.now(), expires).toInt().coerceAtLeast(0)
            }.getOrNull()
        }

        val membershipPlanName = when (membership?.type) {
            "recurring" -> "Premium Aylık"
            "one_time"  -> "Premium"
            else        -> null
        }

        val initials = buildString {
            dto.firstName.firstOrNull()?.let { append(it.uppercaseChar()) }
            dto.lastName.firstOrNull()?.let { append(it.uppercaseChar()) }
        }

        UserProfile(
            name                   = "${dto.firstName} ${dto.lastName}",
            handle                 = "@${dto.displayName ?: dto.firstName.lowercase()}",
            initials               = initials,
            isPremium              = membership?.status == "active",
            avatarStartColor       = 0xFF8B5E6EL,
            avatarEndColor         = 0xFF6B3E50L,
            playlistCount          = 0,
            followerCount          = "0",
            followingCount         = 0,
            audioQuality           = "Yüksek",
            offlineDownloadEnabled = false,
            daysLeft               = daysLeft,
            membershipPlanName     = membershipPlanName,
        )
    }
}
