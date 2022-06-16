package com.naeggeodo.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
//    @Provides
//    @Singleton
//    fun provideSignInRepoUseCase(repository: UserRepository) = SignInUseCase(repository)
//
//    @Provides
//    @Singleton
//    fun provideGetUserRepoUseCase(repository: UserRepository) = UserInfoUseCase(repository)
//
//    @Provides
//    @Singleton
//    fun provideRestaurantRepoUseCase(repository: RestaurantRepository) = RestaurantUseCase(repository)
}