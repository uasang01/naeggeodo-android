package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class ChatRoomState(
    @SerializedName("chatMain_id")
    val chatId: Int,
    val state: String
)
