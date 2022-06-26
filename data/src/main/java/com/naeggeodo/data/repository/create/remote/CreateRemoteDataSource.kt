package com.naeggeodo.data.repository.create.remote

import com.naeggeodo.domain.model.ChatId
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface CreateRemoteDataSource {
    suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        body: HashMap<String, Any>
    ): ChatId?
}