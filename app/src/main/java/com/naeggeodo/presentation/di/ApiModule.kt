package com.naeggeodo.presentation.di

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

//    @Provides
//    @Singleton
//    fun provideSignInApiService(@Named("Login") retrofit: Retrofit): SignInApi {
//        return retrofit.create(SignInApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideGetUserInfoApiService(@Named("Main") retrofit: Retrofit): UserInfoApi {
//        return retrofit.create(UserInfoApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideRestaurantApiService(@Named("Main") retrofit: Retrofit): RestaurantApi {
//        return retrofit.create(RestaurantApi::class.java)
//    }
}