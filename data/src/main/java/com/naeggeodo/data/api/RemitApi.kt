package com.naeggeodo.data.api

import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface RemitApi {
    @PATCH("chat-rooms/{chatMain_id}/users/{user_id}")
    suspend fun changeRemittanceState(
        @Path("chatMain_id") chatId: Int,
        @Path("user_id") userId: String
    ): Response<Any>
}