package com.naeggeodo.data.api

import com.naeggeodo.domain.model.Chat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetChatInfoApi {
    @GET("chat-rooms/{chatMain_id}")
    suspend fun getChatInfo(
        @Path("chatMain_id") chatId: Int
    ): Response<Chat>
}