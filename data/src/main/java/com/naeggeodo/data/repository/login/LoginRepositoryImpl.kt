package com.naeggeodo.data.repository.login

import com.naeggeodo.data.repository.login.remote.LoginRemoteDataSource
import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.repository.LoginRepository
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource
) : LoginRepository {
    override suspend fun logIn(
        remoteErrorEmitter: RemoteErrorEmitter,
        platform: String,
        params: HashMap<String, String?>
    ): LogIn? {
        return loginRemoteDataSource.login(remoteErrorEmitter, platform, params)
    }

    override suspend fun refreshToken(
        remoteErrorEmitter: RemoteErrorEmitter,
    ): LogIn? {
        return loginRemoteDataSource.refreshToken(remoteErrorEmitter)
    }

}