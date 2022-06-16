package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class LogIn(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("SerializedName")
    val refreshToken: String,
    @SerializedName("SerializedName")
    val type: String,
    @SerializedName("SerializedName")
    val user_id: String,
    @SerializedName("SerializedName")
    val address: String
)
