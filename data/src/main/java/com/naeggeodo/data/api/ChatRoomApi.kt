package com.naeggeodo.data.api

import com.naeggeodo.domain.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ChatRoomApi {
    @GET("chat-rooms/{chatMain_id}")
    suspend fun getChatInfo(
        @Path("chatMain_id") chatId: Int
    ): Response<Chat>

    @DELETE("chat-rooms/{chatMain_id}")
    suspend fun deleteChatRoom(
        @Path("chatMain_id") chatId: Int
    ): Response<DeleteChat>

    @PATCH("chat-rooms/{chatMain_id}")
    suspend fun changeChatRoomState(
        @Path("chatMain_id") chatId: Int,
        @Query("state") state: String
    ): Response<ChatRoomState>

    @Multipart
    @POST("chat-rooms")
    suspend fun createChat(
        @Part files: List<MultipartBody.Part>
    ): Response<ChatId>

    @GET("chat-rooms/progressing/user/{user_id}")
    suspend fun getMyChats(
        @Path("user_id") userId: String
    ): Response<ChatList>

    @PATCH("chat-rooms/{chatMain_id}")
    suspend fun changeChatTitle(
        @Path("chatMain_id") chatId: Int,
        @Query("title") title: String
    ): Response<ChatTitle>
}