package com.naeggeodo.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

//    @Singleton
//    @Provides
//    fun provideLoginRepository(
//        loginDataSourceImpl: UserDataSourceImpl
//    ): UserRepository {
//        return UserRepositoryImpl(loginDataSourceImpl)
//    }
//
//    @Singleton
//    @Provides
//    fun provideDataStoreRepository(
//        dataStore: DataStore<Preferences>
//    ): DataStoreRepository {
//        return DataStoreRepositoryImpl(dataStore)
//    }
//
//    @Singleton
//    @Provides
//    fun provideRestaurantRepository(
//        restaurantDataSourceImpl: RestaurantDataSourceImpl
//    ): RestaurantRepository {
//        return RestaurantRepositoryImpl(restaurantDataSourceImpl)
//    }
}