package com.naeggeodo.data.repository.info

import com.naeggeodo.data.repository.info.remote.InfoRemoteDataSource
import com.naeggeodo.domain.model.MyInfo
import com.naeggeodo.domain.model.MyNickName
import com.naeggeodo.domain.repository.InfoRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class InfoRepositoryImpl @Inject constructor(
    private val infoRemoteDataSource: InfoRemoteDataSource
) : InfoRepository {
    override suspend fun changeNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String,
        nickname: String
    ): MyNickName? {
        return infoRemoteDataSource.changeNickName(remoteErrorEmitter, userId, nickname)
    }

    override suspend fun getMyNickName(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyNickName? {
        return infoRemoteDataSource.getMyNickName(remoteErrorEmitter, userId)
    }

    override suspend fun getMyInfo(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): MyInfo? {
        return infoRemoteDataSource.getMyInfo(remoteErrorEmitter, userId)
    }

    override suspend fun report(
        remoteErrorEmitter: RemoteErrorEmitter,
        body: HashMap<String, String>
    ): Boolean {
        return infoRemoteDataSource.report(remoteErrorEmitter, body)
    }
}