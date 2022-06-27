package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.ChatId
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CreateRepository {
    suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ): ChatId?
}