package com.naeggeodo.data.repository.login.remote

import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface LoginRemoteDataSource {
    suspend fun login(
        remoteErrorEmitter: RemoteErrorEmitter,
        provider: String,
        body: HashMap<String, String?>
    ): LogIn?

    suspend fun refreshToken(
        remoteErrorEmitter: RemoteErrorEmitter,
    ): LogIn?
}