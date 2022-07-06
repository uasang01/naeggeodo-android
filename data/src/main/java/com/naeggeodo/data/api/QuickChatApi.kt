package com.naeggeodo.data.api

import com.naeggeodo.domain.model.QuickChatList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface QuickChatApi {
    @GET("user/{user_id}/quick-chatting")
    suspend fun getQuickChatApi(
        @Path("user_id") userId: String
    ): Response<QuickChatList>

    @PATCH("user/{user_id}/quick-chatting")
    suspend fun patchQuickChatApi(
        @Path("user_id") userId: String,
        @Body body: HashMap<String, List<String?>>
    ): Response<QuickChatList>
}