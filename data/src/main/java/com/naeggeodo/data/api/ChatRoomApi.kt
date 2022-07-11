package com.naeggeodo.data.api

import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.DeleteChat
import com.naeggeodo.domain.utils.ChatState
import retrofit2.Response
import retrofit2.http.*

interface ChatRoomApi {
    @GET("chat-rooms/{chatMain_id}")
    suspend fun getChatInfo(
        @Path("chatMain_id") chatId: Int
    ): Response<Chat>

    @DELETE("chat-rooms/{chatMain_id}")
    suspend fun deleteChatRoom(
        @Path("chatMain_id") chatId: Int
    ): Response<DeleteChat>

    @PATCH("chat-rooms/{chatMain_id}?state=?")
    suspend fun changeChatRoomState(
        @Path("chatMain_id") chatId: Int,
        @Query("state") state: String
    )
}