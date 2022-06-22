package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.HomeRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class SearchChatListByCategoryUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend fun execute(
        remoteErrorEmitter: RemoteErrorEmitter,
        category: String?,
        buildingCode: String
    ) = homeRepository.getChatList(remoteErrorEmitter, category, buildingCode)
}