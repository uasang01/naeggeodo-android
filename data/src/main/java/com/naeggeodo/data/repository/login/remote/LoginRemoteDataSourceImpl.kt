package com.naeggeodo.data.repository.login.remote

import com.naeggeodo.data.api.LogInApi
import com.naeggeodo.data.api.RefreshTokenApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import retrofit2.HttpException
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val logInApi: LogInApi,
    private val refreshTokenApi: RefreshTokenApi
) : LoginRemoteDataSource, BaseRepository() {
    override suspend fun login(
        remoteErrorEmitter: RemoteErrorEmitter,
        provider: String,
        body: HashMap<String, String?>
    ): LogIn? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = logInApi.logIn(provider, body)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun refreshToken(remoteErrorEmitter: RemoteErrorEmitter): LogIn? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = refreshTokenApi.refreshToken()
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }
}