package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val userId: String,
    val nickname: String,
    val idx: Int,
    val remittanceState: String
)