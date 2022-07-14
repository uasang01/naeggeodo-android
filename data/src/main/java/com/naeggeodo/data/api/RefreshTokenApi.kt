package com.naeggeodo.data.api

import com.naeggeodo.domain.model.LogIn
import retrofit2.Response
import retrofit2.http.POST

interface RefreshTokenApi {
    @POST("refreshtoken")
    suspend fun refreshToken(): Response<LogIn>
}