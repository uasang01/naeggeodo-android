package com.naeggeodo.data.repository.chat.remote

import com.naeggeodo.data.api.ChatRoomApi
import com.naeggeodo.data.api.GetPrevChatHistoryApi
import com.naeggeodo.data.api.GetUsersInChatApi
import com.naeggeodo.data.api.QuickChatApi
import com.naeggeodo.data.base.BaseRepository
import com.naeggeodo.domain.model.*
import com.naeggeodo.domain.utils.RemoteErrorEmitter
import retrofit2.HttpException
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val chatRoomApi: ChatRoomApi,
    private val getUsersInChat: GetUsersInChatApi,
    private val getPrevChatHistoryApi: GetPrevChatHistoryApi,
    private val quickChatApi: QuickChatApi
) : ChatRemoteDataSource, BaseRepository() {
    override suspend fun getChatInfo(remoteErrorEmitter: RemoteErrorEmitter, chatId: Int): Chat? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = chatRoomApi.getChatInfo(chatId)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun getUsersInChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int
    ): Users? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = getUsersInChat.getUsersInChat(chatId)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun getPrevChatHistory(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        userId: String
    ): ChatHistoryList? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = getPrevChatHistoryApi.getPrevChatHistory(chatId, userId)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun getQuickChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): QuickChatList? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = quickChatApi.getQuickChatApi(userId)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun patchQuickChat(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String,
        body: HashMap<String, List<String?>>
    ): QuickChatList? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = quickChatApi.patchQuickChatApi(userId, body)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun getMyChatList(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: String
    ): ChatList? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = chatRoomApi.getMyChats(userId)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun changeChatRoomState(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        state: String
    ): ChatRoomState? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = chatRoomApi.changeChatRoomState(chatId, state)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }

    override suspend fun changeChatTitle(
        remoteErrorEmitter: RemoteErrorEmitter,
        chatId: Int,
        title: String
    ): ChatTitle? {
        val res = safeApiCall(remoteErrorEmitter) {
            val result = chatRoomApi.changeChatTitle(chatId, title)
            if (result.code() != 200) throw HttpException(result)
            result
        }
        return res?.body()
    }
}