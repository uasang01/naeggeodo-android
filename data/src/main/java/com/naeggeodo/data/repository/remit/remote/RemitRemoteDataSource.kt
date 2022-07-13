package com.naeggeodo.data.repository.remit.remote

import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface RemitRemoteDataSource {
    suspend fun changeRemittanceState(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String,
    ): String?
}