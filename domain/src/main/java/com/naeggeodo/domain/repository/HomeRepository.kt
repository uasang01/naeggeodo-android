package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.Categories
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.MyNickName
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface HomeRepository {
    suspend fun getCategories(remoteErrorEmitter: RemoteErrorEmitter): Categories?
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