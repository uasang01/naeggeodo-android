package com.naeggeodo.data.repository.chat

import com.naeggeodo.data.repository.chat.remote.ChatRemoteDataSource
import com.naeggeodo.domain.model.*
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

    override suspend fun getPrevChatHistory(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): ChatHistoryList? {
        return dataSource.getPrevChatHistory(remoteErrorEmitter, chatId, userId)
    }

    override suspend fun getQuickChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): QuickChatList? {
        return dataSource.getQuickChat(remoteErrorEmitter, userId)
    }

    override suspend fun patchQuickChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String,
        body: HashMap<String, List<String?>>
    ): QuickChatList? {
        return dataSource.patchQuickChat(remoteErrorEmitter, userId, body)
    }

    override suspend fun getMyChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): ChatList? {
        return dataSource.getMyChatList(remoteErrorEmitter, userId)
    }
}