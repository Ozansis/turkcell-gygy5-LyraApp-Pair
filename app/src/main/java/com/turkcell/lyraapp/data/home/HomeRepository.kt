package com.turkcell.lyraapp.data.home

interface HomeRepository {
    fun getGreeting(): String
    fun getUserInitials(): String
    suspend fun getMoodCategories(): Result<List<MoodCategory>>
    suspend fun getRecentlyPlayed(): Result<List<Track>>
    suspend fun getRecommendedPlaylists(): Result<List<Playlist>>
}
