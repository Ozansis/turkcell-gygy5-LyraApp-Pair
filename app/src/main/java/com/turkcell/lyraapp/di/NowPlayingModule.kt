package com.turkcell.lyraapp.di

import com.turkcell.lyraapp.data.nowplaying.FakeNowPlayingRepository
import com.turkcell.lyraapp.data.nowplaying.NowPlayingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NowPlayingModule {

    @Binds
    @Singleton
    abstract fun bindNowPlayingRepository(impl: FakeNowPlayingRepository): NowPlayingRepository
}
