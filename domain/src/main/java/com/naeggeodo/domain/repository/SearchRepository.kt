package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.Tags
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface SearchRepository {
    suspend fun getTags(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Tags?

    suspend fun getChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        searchType: String,
        keyWord: String
    ): ChatList?
}