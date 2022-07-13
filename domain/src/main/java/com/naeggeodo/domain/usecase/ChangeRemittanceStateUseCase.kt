package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.RemitRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class ChangeRemittanceStateUseCase @Inject constructor(
    private val remitRepository: RemitRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int, userId: String) =
        remitRepository.changeRemittanceState(remoteErrorEmitter, chatId, userId)
}
