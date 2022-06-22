package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class ChatList(
    @SerializedName("chatRoom")
    val chatList: List<Chat>
)