package com.naeggeodo.data.repository.remit

import com.naeggeodo.data.repository.remit.remote.RemitRemoteDataSource
import com.naeggeodo.domain.repository.RemitRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class RemitRepositoryImpl @Inject constructor(
    private val remitRemoteDataSource: RemitRemoteDataSource
) : RemitRepository {
    override suspend fun changeRemittanceState(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): String? {
        return remitRemoteDataSource.changeRemittanceState(remoteErrorEmitter, chatId, userId)
    }
}