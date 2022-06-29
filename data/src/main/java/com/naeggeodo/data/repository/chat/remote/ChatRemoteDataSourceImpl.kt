package com.naeggeodo.data.repository.chat.remote

import com.damda.data.base.BaseRepository
import com.naeggeodo.data.api.GetChatInfoApi
import com.naeggeodo.data.api.GetPrevChatHistoryApi
import com.naeggeodo.data.api.GetUsersInChatApi
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.ChatHistoryList
import com.naeggeodo.domain.model.Users
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import timber.log.Timber
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val getChatInfoApi: GetChatInfoApi,
    private val getUsersInChat: GetUsersInChatApi,
    private val getPrevChatHistoryApi: GetPrevChatHistoryApi
): ChatRemoteDataSource, BaseRepository() {
    override suspend fun getChatInfo(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int): Chat? {

        val res = safeApiCall(remoteErrorEmitter) {
            getChatInfoApi.getChatInfo(chatId)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }

    override suspend fun getUsersInChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int
    ): Users? {

        val res = safeApiCall(remoteErrorEmitter) {
            getUsersInChat.getUsersInChat(chatId)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }

    override suspend fun getPrevChatHistory(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): ChatHistoryList? {

        val res = safeApiCall(remoteErrorEmitter) {
            getPrevChatHistoryApi.getPrevChatHistory(chatId, userId)
        }
        return if (res != null && res.isSuccessful && res.code() == 200) {
            res.body()
        } else {
            Timber.e("Api call failed / status:${res?.code()} errorBody:${res?.errorBody()}")
            null
        }
    }
}