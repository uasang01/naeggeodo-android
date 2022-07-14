package com.naeggeodo.domain.repository

import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.utils.RemoteErrorEmitter

interface LoginRepository {
    suspend fun logIn(
        remoteErrorEmitter: RemoteErrorEmitter,
        platform: String,
        params: HashMap<String, String?>
    ): LogIn?

    suspend fun refreshToken(
        remoteErrorEmitter: RemoteErrorEmitter,
    ): LogIn?
}