package com.naeggeodo.presentation.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naeggeodo.data.api.RefreshTokenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "https://api.naeggeodo.com/api/"

    @Provides
    @Singleton
    @Named("AuthHeader")
    fun provideHttpClient(
        authenticator: Authenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .authenticator(authenticator)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor { chain ->
                Timber.e("TOKEN / Bearer ${App.prefs.accessToken}")
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${App.prefs.accessToken}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("NoAuthHeader")
    fun provideLoginHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("RefreshHeader")
    fun provideRefreshHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor { chain ->
                Timber.e("REFRESH TOKEN / Bearer ${App.prefs.refreshToken}")
                val request = chain.request().newBuilder()
                    .addHeader("Cookie", "refreshToken=${App.prefs.refreshToken}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Singleton
    @Provides
    @Named("Auth")
    fun provideMainRetrofitInstance(
        @Named("AuthHeader") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    @Named("NoAuth")
    fun provideLoginRetrofitInstance(
        @Named("NoAuthHeader") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    @Named("Refresh")
    fun provideRefreshRetrofitInstance(
        @Named("RefreshHeader") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        refreshTokenApi: RefreshTokenApi
    ): Authenticator = Authenticator { _, response ->
        val request = response.request
        return@Authenticator synchronized(this) {
            if (response.code == 401) {
                val newToken =
                    try {
                        // This will write a new token to sharedPreferences.
                        runBlocking { refreshTokenApi.refreshToken().body()?.accessToken }
                    } catch (e: Exception) {
                        // A error occurred while refreshing token.
                        Timber.w(e)
                        null
                    }

                // If we have a new token let's use it.
                newToken?.let {
                    App.prefs.accessToken = it
                    request.newBuilder()
                        .header("Authorization", "Bearer $it")
                        .build()
                }
            } else null
        }
    }
}
