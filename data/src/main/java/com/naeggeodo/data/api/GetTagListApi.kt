package com.naeggeodo.data.api

import com.naeggeodo.domain.model.Tags
import retrofit2.Response
import retrofit2.http.GET

interface GetTagsApi {
    @GET("chat-rooms/tag/most-wanted")
    suspend fun getTags(): Response<Tags>
}