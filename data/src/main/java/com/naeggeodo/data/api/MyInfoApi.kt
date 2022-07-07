package com.naeggeodo.data.api

import com.naeggeodo.domain.model.MyInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MyInfoApi {
    @GET("user/{user_id}/nickname")
    suspend fun getMyInfo(
        @Path("user_id") userId: String
    ): Response<MyInfo>
}