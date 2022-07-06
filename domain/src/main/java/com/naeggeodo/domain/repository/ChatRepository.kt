package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.ChatHistoryList
import com.naeggeodo.domain.model.QuickChatList
import com.naeggeodo.domain.model.Users
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface ChatRepository {
    suspend fun getChatInfo(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int): Chat?
    suspend fun getUsersInChat(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int): Users?
    suspend fun getPrevChatHistory(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): ChatHistoryList?
    suspend fun getQuickChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): QuickChatList?
    suspend fun patchQuickChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String,
        body: HashMap<String, List<String?>>
    ): QuickChatList?
}