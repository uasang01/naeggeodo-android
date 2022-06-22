package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.HomeRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class CategoryUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter) =
        homeRepository.getCategories(remoteErrorEmitter)
}
