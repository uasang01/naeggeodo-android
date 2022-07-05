package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.CreateRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class GetChatCreationHistoryUseCase @Inject constructor(
    private val createRepository: CreateRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, userId: String) =
        createRepository.getChatCreationHistory(remoteErrorEmitter, userId)
}
