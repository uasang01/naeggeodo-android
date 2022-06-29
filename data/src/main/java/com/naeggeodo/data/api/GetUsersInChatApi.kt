package com.naeggeodo.data.api

import com.naeggeodo.domain.model.Tags
import com.naeggeodo.domain.model.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetUsersInChatApi {
    @GET("chat-rooms/{chatMain_id}/users")
    suspend fun getUsersInChat(
        @Path("chatMain_id") chatId: Int
    ): Response<Users>
}