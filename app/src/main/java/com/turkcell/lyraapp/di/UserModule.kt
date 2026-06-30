package com.turkcell.lyraapp.di

import com.turkcell.lyraapp.data.user.NetworkUserRepository
import com.turkcell.lyraapp.data.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: NetworkUserRepository,
    ): UserRepository
}
