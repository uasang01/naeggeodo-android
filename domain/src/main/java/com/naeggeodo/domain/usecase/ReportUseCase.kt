package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.InfoRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class ReportUseCase @Inject constructor(
    private val infoRepository: InfoRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, body: HashMap<String, String>) =
        infoRepository.report(remoteErrorEmitter, body)
}
