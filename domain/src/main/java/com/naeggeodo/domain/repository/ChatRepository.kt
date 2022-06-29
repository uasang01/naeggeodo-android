package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.Users
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface ChatRepository {
    suspend fun getChatInfo(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int): Chat?
    suspend fun getUsersInChat(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int): Users?

}