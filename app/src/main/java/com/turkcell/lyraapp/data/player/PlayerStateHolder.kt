package com.turkcell.lyraapp.data.player

import com.turkcell.lyraapp.data.home.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerStateHolder @Inject constructor() {
    var currentTrack: Track? = null
}
