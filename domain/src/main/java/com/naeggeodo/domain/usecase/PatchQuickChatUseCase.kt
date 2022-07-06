package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.ChatRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class PatchQuickChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, userId: String, body: HashMap<String, List<String?>>) =
        chatRepository.patchQuickChat(remoteErrorEmitter, userId, body)
}
