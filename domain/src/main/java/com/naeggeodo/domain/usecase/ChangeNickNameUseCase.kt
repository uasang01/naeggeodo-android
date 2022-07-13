package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.InfoRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class ChangeNickNameUseCase @Inject constructor(
    private val infoRepository: InfoRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, userId: String, nickname: String) =
        infoRepository.changeNickName(remoteErrorEmitter, userId, nickname)
}
