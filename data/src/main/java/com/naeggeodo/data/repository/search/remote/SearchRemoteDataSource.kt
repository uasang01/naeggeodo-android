package com.naeggeodo.data.repository.search.remote

import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.Tags
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface SearchRemoteDataSource {
    suspend fun getTags(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Tags?

    suspend fun getChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        searchType: String,
        keyWord: String
    ): ChatList?
}