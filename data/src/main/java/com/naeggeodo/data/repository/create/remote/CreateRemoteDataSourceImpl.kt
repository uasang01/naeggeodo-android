package com.naeggeodo.data.repository.create.remote

import com.naeggeodo.data.api.BookmarkingApi
import com.naeggeodo.data.api.ChatRoomApi
import com.naeggeodo.data.api.GetChatCreationHistoryApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.model.ChatId
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.DeleteChat
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import okhttp3.MultipartBody
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject


class CreateRemoteDataSourceImpl @Inject constructor(
    private val getChatCreationHistoryApi: GetChatCreationHistoryApi,
    private val bookmarkingApi: BookmarkingApi,
    private val chatRoomApi: ChatRoomApi
) : CreateRemoteDataSource, BaseRepository() {
    override suspend fun createChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        files: List<MultipartBody.Part>
    ): ChatId? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = chatRoomApi.createChat(files)
            if (result.code() != 200) {
                throw HttpException(result)
            }
            result
        }
        return res?.body()
    }

    override suspend fun getChatCreationHistory(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): ChatList? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = getChatCreationHistoryApi.getChatCreationHistoryApi(userId)
            if (result.code() != 200) {
                throw HttpException(result)
            }
            result
        }
        return res?.body()
    }

    override suspend fun bookmarking(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): Boolean {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = bookmarkingApi.bookmarking(chatId, userId)
            if (result.code() != 200) {
                throw HttpException(result)
            }
            result
        }
        return res != null
    }

    override suspend fun deleteChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int
    ): DeleteChat? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = chatRoomApi.deleteChatRoom(chatId)
            if (result.code() != 200) {
                throw HttpException(result)
            }
            result
        }
        return res?.body()
    }
}