package com.naeggeodo.data.api

import com.naeggeodo.domain.model.Chat
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatRoomApi {
    @GET("chat-rooms/{chatMain_id}")
    suspend fun getChatInfo(
        @Path("chatMain_id") chatId: Int
    ): Response<Chat>

    @DELETE("chat-rooms/{chatMain_id}")
    suspend fun deleteChatRoom(
        @Path("chatMain_id") chatId: Int
    ): Response<Any>
}