package com.naeggeodo.data.repository.remit.remote

import com.naeggeodo.data.api.RemitApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import retrofit2.HttpException
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
            val result = remitApi.changeRemittanceState(chatId, userId)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }
}