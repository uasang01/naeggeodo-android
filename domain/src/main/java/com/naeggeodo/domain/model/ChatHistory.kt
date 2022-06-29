package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class ChatHistory(
    @SerializedName("chatMain_id")
    val chatId: Int,
    val contents: String,
    val id: Int,
    val idx: Int,
    val regDate: String,
    val type: String,
    @SerializedName("user_id")
    val userId: String
)