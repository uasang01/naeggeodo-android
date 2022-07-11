package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.ChatId
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.DeleteChat
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody

interface CreateRepository {
    suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ): ChatId?

    suspend fun getChatCreationHistory(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): ChatList?

    suspend fun bookmarking(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): Boolean

    suspend fun deleteChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int
    ): DeleteChat?
}