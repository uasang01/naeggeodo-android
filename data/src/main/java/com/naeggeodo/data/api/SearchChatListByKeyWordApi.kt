package com.naeggeodo.data.api

import com.naeggeodo.domain.model.ChatList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchChatListByKeyWordApi {
    @GET("chat-rooms/{searchType}")
    suspend fun getChatList(
        @Path("searchType") searchType: String,
        @Query("keyWord") keyWord: String
    ): Response<ChatList>
}