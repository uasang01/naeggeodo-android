package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.CreateRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val createRepository: CreateRepository
) {
    suspend fun execute(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ) = createRepository.createChat(remoteErrorEmitter, files)
}