package com.damda.data.repository.login

import com.damda.data.repository.login.remote.LoginRemoteDataSource
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
}