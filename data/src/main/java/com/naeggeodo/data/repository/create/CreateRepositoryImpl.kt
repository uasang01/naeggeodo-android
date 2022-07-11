package com.naeggeodo.data.repository.create

import com.naeggeodo.data.repository.create.remote.CreateRemoteDataSource
import com.naeggeodo.domain.model.ChatId
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.repository.CreateRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody
import javax.inject.Inject

class CreateRepositoryImpl @Inject constructor(
    private val createRemoteDataSource: CreateRemoteDataSource
) : CreateRepository {
    override suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ): ChatId? {
        return createRemoteDataSource.createChat(remoteErrorEmitter, files)
    }

    override suspend fun getChatCreationHistory(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): ChatList? {
        return createRemoteDataSource.getChatCreationHistory(remoteErrorEmitter, userId)
    }

    override suspend fun bookmarking(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): Boolean {
        return createRemoteDataSource.bookmarking(remoteErrorEmitter, chatId, userId)
    }
}