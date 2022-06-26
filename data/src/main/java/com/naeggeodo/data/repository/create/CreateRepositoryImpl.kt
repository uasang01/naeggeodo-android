package com.naeggeodo.data.repository.create

import com.naeggeodo.data.repository.create.remote.CreateRemoteDataSource
import com.naeggeodo.domain.model.ChatId
import com.naeggeodo.domain.repository.CreateRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class CreateRepositoryImpl @Inject constructor(
    private val createRemoteDataSource: CreateRemoteDataSource
) : CreateRepository {
    override suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        body: HashMap<String, Any>
    ): ChatId? {
        return createRemoteDataSource.createChat(remoteErrorEmitter, body)
    }

}