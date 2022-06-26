package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class LogIn(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("address")
    val address: String?,
    @SerializedName("buildingCode")
    val buildingCode: String?
)
