package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.CreateRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val createRepository: CreateRepository
) {
    suspend fun execute(
        remoteErrorEmitter: RemoteErrorEmitter,
        body: HashMap<String, Any>
    ) = createRepository.createChat(remoteErrorEmitter, body)
}