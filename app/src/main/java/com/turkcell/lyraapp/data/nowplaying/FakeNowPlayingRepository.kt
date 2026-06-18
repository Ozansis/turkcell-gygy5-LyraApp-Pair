package com.turkcell.lyraapp.data.nowplaying

import javax.inject.Inject

class FakeNowPlayingRepository @Inject constructor() : NowPlayingRepository {

    override suspend fun getNowPlayingTrack(): Result<NowPlayingTrack> = Result.success(
        NowPlayingTrack(
            id              = "neon-sokaklar-1",
            title           = "Neon Sokaklar",
            artist          = "Şehir Işıkları",
            playlistName    = "Gece Vardiyası",
            coverStartColor = 0xFFD4742AL,
            coverEndColor   = 0xFFB85A10L,
            duration        = "3:43",
            currentPosition = "1:33",
            progress        = 0.42f,
            isFavorite      = true,
            isPlaying       = true,
            isShuffled      = false,
            isRepeating     = false,
        )
    )
}
