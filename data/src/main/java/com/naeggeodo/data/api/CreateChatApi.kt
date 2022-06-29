package com.naeggeodo.data.api

import com.naeggeodo.domain.model.ChatId
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CreateChatApi {
    @Multipart
    @POST("chat-rooms")
    suspend fun createChat(
        @Part files: List<MultipartBody.Part>
    ): Response<ChatId>
}