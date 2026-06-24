package com.turkcell.lyraapp.di

import com.turkcell.lyraapp.data.auth.SessionManager
import com.turkcell.lyraapp.data.remote.AuthApiService
import com.turkcell.lyraapp.data.remote.SongsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://streaming-api.halitkalayci.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val path = request.url.encodedPath
                val isAuthEndpoint = path.contains("/api/v1/auth/")
                val accessToken = sessionManager.getAccessToken()

                val newRequest: Request = if (!isAuthEndpoint && accessToken != null) {
                    request.newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .build()
                } else {
                    request
                }

                val response = chain.proceed(newRequest)

                // 401 gelirse token yenile ve bir kez tekrar dene
                if (response.code == 401 && !isAuthEndpoint) {
                    response.close()
                    val refreshToken = sessionManager.getRefreshToken()
                    if (refreshToken != null) {
                        try {
                            // runBlocking: OkHttp interceptor senkron çalışır,
                            // zaten arka plan thread'inde olduğundan main thread'i bloklamaz
                            val newTokens = runBlocking {
                                val refreshRetrofit = Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                refreshRetrofit.create(AuthApiService::class.java)
                                    .refreshToken(mapOf("refreshToken" to refreshToken))
                            }
                            sessionManager.saveTokens(
                                accessToken = newTokens.data.accessToken,
                                refreshToken = newTokens.data.refreshToken,
                            )
                            val retryRequest = request.newBuilder()
                                .addHeader("Authorization", "Bearer ${newTokens.data.accessToken}")
                                .build()
                            chain.proceed(retryRequest)
                        } catch (e: Exception) {
                            sessionManager.clearTokens()
                            chain.proceed(request)
                        }
                    } else {
                        chain.proceed(request)
                    }
                } else {
                    response
                }
            }
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSongsApiService(retrofit: Retrofit): SongsApiService =
        retrofit.create(SongsApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)
}
