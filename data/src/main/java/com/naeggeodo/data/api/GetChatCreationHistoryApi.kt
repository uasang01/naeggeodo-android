package com.naeggeodo.data.api

import com.naeggeodo.domain.model.ChatList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetChatCreationHistoryApi {
    @GET("chat-rooms/order-list/{user_id}")
    suspend fun getChatCreationHistoryApi(
        @Path("user_id") userId: String
    ): Response<ChatList>
}