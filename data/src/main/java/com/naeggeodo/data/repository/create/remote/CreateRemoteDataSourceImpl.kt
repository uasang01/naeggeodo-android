package com.naeggeodo.data.repository.create.remote

import com.damda.data.base.BaseRepository
import com.naeggeodo.data.api.BookmarkingApi
import com.naeggeodo.data.api.CreateChatApi
import com.naeggeodo.data.api.GetChatCreationHistoryApi
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject


class CreateRemoteDataSourceImpl @Inject constructor(
    private val createChatApi: CreateChatApi,
    private val getChatCreationHistoryApi: GetChatCreationHistoryApi,
    private val bookmarkingApi: BookmarkingApi
) : CreateRemoteDataSource, BaseRepository() {
    override suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ): Chat? {
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

    override suspend fun getChatCreationHistory(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): ChatList? {
        val res = safeApiCall(remoteErrorEmitter) {
            getChatCreationHistoryApi.getChatCreationHistoryApi(userId)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }

    override suspend fun bookmarking(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): Boolean {
        val res = safeApiCall(remoteErrorEmitter) {
            bookmarkingApi.bookmarking(chatId, userId)
        }
        Timber.e("bookmark api response $res")
        return if (res != null && res.isSuccessful && res.code() == 200) {
            true
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            false
        }
    }
}