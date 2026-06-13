package com.turkcell.lyraapp.di

import com.turkcell.lyraapp.data.home.FakeHomeRepository
import com.turkcell.lyraapp.data.home.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: FakeHomeRepository): HomeRepository
}
