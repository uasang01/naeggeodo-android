package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.SearchRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend fun execute(
        remoteErrorEmitter: RemoteErrorEmitter,
    ) = searchRepository.getTags(remoteErrorEmitter)
}