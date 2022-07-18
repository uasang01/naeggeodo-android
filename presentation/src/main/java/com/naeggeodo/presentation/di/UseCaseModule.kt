package com.naeggeodo.presentation.di

import com.naeggeodo.domain.repository.*
import com.naeggeodo.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideLogInUseCase(repository: LoginRepository) = LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideCategoryUseCase(repository: HomeRepository) = CategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTagsUseCase(repository: SearchRepository) = GetTagsUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchChatListByCategoryUseCase(repository: HomeRepository) =
        SearchChatListByCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchChatListByKeyWordUseCase(repository: SearchRepository) =
        SearchChatListByKeyWordUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateChatUseCase(repository: CreateRepository) = CreateChatUseCase(repository)

    @Provides
    @Singleton
    fun provideGetChatInfoUseCase(repository: ChatRepository) = GetChatInfoUseCase(repository)

    @Provides
    @Singleton
    fun provideGetUsersInChatUseCase(repository: ChatRepository) = GetUsersInChatUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPrevChatHistoryUseCase(repository: ChatRepository) =
        GetPrevChatHistoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetChatCreationHistoryUseCase(repository: CreateRepository) =
        GetChatCreationHistoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMyNickNameUseCase(repository: InfoRepository) = GetMyNickNameUseCase(repository)

    @Provides
    @Singleton
    fun provideChangeNickNameUseCase(repository: InfoRepository) = ChangeNickNameUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMyInfoUseCase(repository: InfoRepository) = GetMyInfoUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCreationChatHistoryUseCase(repository: CreateRepository) =
        DeleteCreationChatHistoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMyChatListUseCase(repository: ChatRepository) = GetMyChatListUseCase(repository)

    @Provides
    @Singleton
    fun provideChangeRemittanceStateUseCase(repository: RemitRepository) =
        ChangeRemittanceStateUseCase(repository)

    @Provides
    @Singleton
    fun provideRefreshTokenUseCase(repository: LoginRepository) =
        RefreshTokenUseCase(repository)

    @Provides
    @Singleton
    fun provideReportUseCase(repository: InfoRepository) =
        ReportUseCase(repository)

    @Provides
    @Singleton
    fun provideChangeChatTitleUseCase(repository: ChatRepository) =
        ChangeChatTitleUseCase(repository)
}
