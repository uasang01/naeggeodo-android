package com.damda.data.api

import com.naeggeodo.domain.model.Categories
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET("categories")
    suspend fun getCategories(): Response<Categories>
}