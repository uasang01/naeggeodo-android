package com.naeggeodo.presentation.di

import com.damda.data.api.LogInApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideLogInApiService(@Named("Login") retrofit: Retrofit): LogInApi {
        return retrofit.create(LogInApi::class.java)
    }
}