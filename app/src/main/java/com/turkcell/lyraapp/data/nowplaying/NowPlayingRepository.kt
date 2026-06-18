package com.turkcell.lyraapp.data.nowplaying

interface NowPlayingRepository {
    suspend fun getNowPlayingTrack(): Result<NowPlayingTrack>
}
