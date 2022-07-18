package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class ChatTitle(
    @SerializedName("chatMain_id")
    val chatId: Int,
    val title: String
)
