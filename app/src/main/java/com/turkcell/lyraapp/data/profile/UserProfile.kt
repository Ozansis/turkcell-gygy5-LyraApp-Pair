package com.turkcell.lyraapp.data.profile

data class UserProfile(
    val name: String,
    val handle: String,
    val isPremium: Boolean,
    val initials: String,
    val avatarStartColor: Long,
    val avatarEndColor: Long,
    val playlistCount: Int,
    val followerCount: String,
    val followingCount: Int,
    val audioQuality: String,
    val offlineDownloadEnabled: Boolean,
)
