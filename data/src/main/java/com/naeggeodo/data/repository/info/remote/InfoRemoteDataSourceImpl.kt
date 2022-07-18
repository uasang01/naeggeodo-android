package com.naeggeodo.data.repository.info.remote

import com.naeggeodo.data.api.InfoApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.model.MyInfo
import com.naeggeodo.domain.model.MyNickName
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import retrofit2.HttpException
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
            val result = infoApi.changeNickName(userId, nickname)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun getMyNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyNickName? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = infoApi.getMyNickName(userId)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun getMyInfo(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyInfo? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = infoApi.getMyInfo(userId)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun report(
        remoteErrorEmitter: RemoteErrorEmitter,
        body: HashMap<String, String>
    ): Boolean {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = infoApi.report(body)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res != null
    }
}