package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.ChatRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class GetMyChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, userId: String) =
        chatRepository.getMyChatList(remoteErrorEmitter, userId)
}
