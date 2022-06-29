package com.naeggeodo.data.repository.chat

import com.naeggeodo.data.repository.chat.remote.ChatRemoteDataSource
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.Users
import com.naeggeodo.domain.repository.ChatRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val dataSource: ChatRemoteDataSource
) : ChatRepository {
    override suspend fun getChatInfo(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int): Chat? {
        return dataSource.getChatInfo(remoteErrorEmitter, chatId)
    }

    override suspend fun getUsersInChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int
    ): Users? {
        return dataSource.getUsersInChat(remoteErrorEmitter, chatId)
    }
}