package com.naeggeodo.data.repository.login.remote

import com.naeggeodo.data.api.LogInApi
import com.naeggeodo.data.api.RefreshTokenApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import timber.log.Timber
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
            logInApi.logIn(provider, body)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }

    override suspend fun refreshToken(remoteErrorEmitter: RemoteErrorEmitter): LogIn? {
        val res = safeApiCall(remoteErrorEmitter) {
            refreshTokenApi.refreshToken()
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }
}