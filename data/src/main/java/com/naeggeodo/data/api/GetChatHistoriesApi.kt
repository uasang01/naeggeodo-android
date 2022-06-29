package com.naeggeodo.data.api

import com.naeggeodo.domain.model.Categories
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetChatHistoriesApi {
    @GET("chat-rooms/order-list/{user_id}")
    suspend fun getChatHistories(
        @Path("user_id") userId: String
    ): Response<Categories>
}