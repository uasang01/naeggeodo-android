package com.naeggeodo.data.repository.remit.remote

import com.naeggeodo.data.api.RemitApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import timber.log.Timber
import javax.inject.Inject

class RemitRemoteDataSourceImpl @Inject constructor(
    private val remitApi: RemitApi
) : RemitRemoteDataSource, BaseRepository() {
    override suspend fun changeRemittanceState(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): String? {
        val res = safeApiCall(remoteErrorEmitter) {
            remitApi.changeRemittanceState(chatId, userId)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }
}