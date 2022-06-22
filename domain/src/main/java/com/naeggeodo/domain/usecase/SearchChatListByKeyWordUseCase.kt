package com.naeggeodo.domain.usecase

import com.naeggeodo.domain.repository.HomeRepository
import com.naeggeodo.domain.repository.LoginRepository
import com.naeggeodo.domain.repository.SearchRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class SearchChatListByKeyWordUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend fun execute(
        remoteErrorEmitter: RemoteErrorEmitter,
        searchType: String,
        keyWord: String
    ) = searchRepository.getChatList(remoteErrorEmitter, searchType, keyWord)
}