package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.ChatRepository
import com.naeggeodo.domain.repository.HomeRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class GetChatInfoUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int) =
        repository.getChatInfo(remoteErrorEmitter, chatId)
}
