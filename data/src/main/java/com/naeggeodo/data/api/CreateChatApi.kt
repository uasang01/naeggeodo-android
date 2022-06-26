package com.naeggeodo.data.api

import com.naeggeodo.domain.model.ChatId
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateChatApi {
    @POST("chat-rooms")
    suspend fun createChat(
        @Body body: HashMap<String, Any>
    ): Response<ChatId>
}