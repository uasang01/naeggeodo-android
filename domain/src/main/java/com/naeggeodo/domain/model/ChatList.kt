package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class ChatList(
    @SerializedName(value = "chatRoom", alternate = ["chat-rooms", "chatRooms"])
    val chatList: List<Chat>
)