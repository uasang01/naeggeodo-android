package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.ChatId
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface CreateRepository {
    suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        body: HashMap<String, Any>
    ): ChatId?
}