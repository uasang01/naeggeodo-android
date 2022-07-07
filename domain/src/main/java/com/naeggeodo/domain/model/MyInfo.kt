package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class MyInfo(
    @SerializedName("user_id")
    val userId: String,
    val nickname: String
)
