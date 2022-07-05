package com.naeggeodo.data.api

import com.naeggeodo.domain.model.ChatList
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path
import javax.annotation.Nullable

interface BookmarkingApi {
    @PATCH("chat-rooms/{chatMain_id}/bookmarks/{user_id}")
    suspend fun bookmarking(
        @Path("chatMain_id") chatId: Int,
        @Path("user_id") userId: String
    ): Response<Any>
}