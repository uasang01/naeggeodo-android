package com.naeggeodo.data.repository.chat.remote

import com.naeggeodo.domain.model.*
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface ChatRemoteDataSource {
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

    suspend fun getMyChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): ChatList?

    suspend fun changeChatRoomState(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        state: String
    ): ChatRoomState?

    suspend fun changeChatTitle(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        title: String
    ): ChatTitle?
}