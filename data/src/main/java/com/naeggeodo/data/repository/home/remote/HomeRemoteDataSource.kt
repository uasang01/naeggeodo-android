package com.naeggeodo.data.repository.home.remote

import com.naeggeodo.domain.model.Categories
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.MyNickName
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface HomeRemoteDataSource {
    suspend fun getCategories(
        remoteErrorEmitter: RemoteErrorEmitter
    ): Categories?

    suspend fun getChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        category: String?,
        buildingCode: String
    ): ChatList?

    suspend fun getMyNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyNickName?
}