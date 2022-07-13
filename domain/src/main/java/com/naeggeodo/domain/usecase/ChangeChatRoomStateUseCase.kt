package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.ChatRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class ChangeChatRoomStateUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int, userId: String) =
        chatRepository.changeChatRoomState(remoteErrorEmitter, chatId, userId)
}
