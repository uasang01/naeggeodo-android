package com.naeggeodo.data.api

import com.naeggeodo.domain.model.ChatHistoryList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPrevChatHistoryApi {
    @GET("chat/messages/{chatMain_id}/{user_id}")
    suspend fun getPrevChatHistory(
        @Path("chatMain_id") chatId: Int,
        @Path("user_id") userId: String
    ): Response<ChatHistoryList>
}