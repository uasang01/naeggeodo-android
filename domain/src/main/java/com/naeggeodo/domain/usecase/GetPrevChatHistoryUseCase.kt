package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.ChatRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class GetPrevChatHistoryUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int, userId: String) =
        repository.getPrevChatHistory(remoteErrorEmitter, chatId, userId)
}
