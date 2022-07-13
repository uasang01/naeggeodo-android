package com.naeggeodo.data.repository.info.remote

import com.naeggeodo.data.api.InfoApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.model.MyInfo
import com.naeggeodo.domain.model.MyNickName
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import timber.log.Timber
import javax.inject.Inject

class InfoRemoteDataSourceImpl @Inject constructor(
    private val infoApi: InfoApi
) : InfoRemoteDataSource, BaseRepository() {
    override suspend fun changeNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String,
        nickname: String
    ): MyNickName? {
        val res = safeApiCall(remoteErrorEmitter) {
            infoApi.changeNickName(userId, nickname)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }

    override suspend fun getMyNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyNickName? {
        val res = safeApiCall(remoteErrorEmitter) {
            infoApi.getMyNickName(userId)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }

    override suspend fun getMyInfo(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyInfo? {
        val res = safeApiCall(remoteErrorEmitter) {
            infoApi.getMyInfo(userId)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }
}