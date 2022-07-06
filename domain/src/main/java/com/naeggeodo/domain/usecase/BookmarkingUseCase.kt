package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.CreateRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class BookmarkingUseCase @Inject constructor(
    private val createRepository: CreateRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int, userId: String) =
        createRepository.bookmarking(remoteErrorEmitter, chatId, userId)
}
