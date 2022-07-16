package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.MyInfo
import com.naeggeodo.domain.model.MyNickName
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface InfoRepository {
    suspend fun changeNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String,
        nickname: String
    ): MyNickName?

    suspend fun getMyNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String,
    ): MyNickName?

    suspend fun getMyInfo(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyInfo?

    suspend fun report(remoteErrorEmitter: RemoteErrorEmitter, body: HashMap<String, String>): Boolean
}