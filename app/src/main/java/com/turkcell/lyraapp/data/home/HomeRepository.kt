package com.turkcell.lyraapp.data.home

interface HomeRepository {
    fun getGreeting(): String
    fun getUserInitials(): String
    suspend fun getMoodCategories(): Result<List<MoodCategory>>
    suspend fun getSongs(): Result<List<Track>>
    suspend fun getRecentlyPlayed(): Result<List<Track>>
    suspend fun getRecommendations(): Result<List<Track>>
}
