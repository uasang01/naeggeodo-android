package com.naeggeodo.data.api

import com.naeggeodo.domain.model.ChatList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetChatListApi {
    @GET("chat-rooms")
    suspend fun getChatList(
        @Query("category") category: String?,
        @Query("buildingCode") buildingCode: String
    ): Response<ChatList>
}