package com.naeggeodo.data.repository.create

import com.naeggeodo.data.repository.create.remote.CreateRemoteDataSource
import com.naeggeodo.domain.model.Chat
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
    ): Chat? {
        return createRemoteDataSource.createChat(remoteErrorEmitter, files)
    }

}