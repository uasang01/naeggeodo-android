package com.naeggeodo.domain.repository

import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface RemitRepository {
    suspend fun changeRemittanceState(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String,
    ): String?
}