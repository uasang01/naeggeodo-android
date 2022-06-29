package com.naeggeodo.data.repository.create.remote

import com.damda.data.base.BaseRepository
import com.naeggeodo.data.api.CreateChatApi
import com.naeggeodo.domain.model.ChatId
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject


class CreateRemoteDataSourceImpl @Inject constructor(
    private val createChatApi: CreateChatApi
) : CreateRemoteDataSource, BaseRepository() {
    override suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ): ChatId? {
        val res = safeApiCall(remoteErrorEmitter) {
            createChatApi.createChat(files)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }
}