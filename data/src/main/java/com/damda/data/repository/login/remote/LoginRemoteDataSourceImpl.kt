package com.damda.data.repository.login.remote

import com.damda.data.api.LogInApi
import com.damda.data.base.BaseRepository
import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import timber.log.Timber
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val logInApi: LogInApi
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
}