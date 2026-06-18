package com.turkcell.lyraapp.data.favorites

interface FavoritesRepository {
    suspend fun getFavoriteTracks(): Result<List<FavoriteTrack>>
}
