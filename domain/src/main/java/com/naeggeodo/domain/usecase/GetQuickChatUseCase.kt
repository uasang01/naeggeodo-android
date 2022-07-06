package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.ChatRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class GetQuickChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, userId: String) =
        chatRepository.getQuickChat(remoteErrorEmitter, userId)
}
