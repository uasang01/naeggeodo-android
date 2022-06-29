package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody

interface CreateRepository {
    suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ): Chat?
}