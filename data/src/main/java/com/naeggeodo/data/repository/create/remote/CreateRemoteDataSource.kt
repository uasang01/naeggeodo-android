package com.naeggeodo.data.repository.create.remote

import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody

interface CreateRemoteDataSource {
    suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ): Chat?
}