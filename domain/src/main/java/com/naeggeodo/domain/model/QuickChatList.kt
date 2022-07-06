package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class QuickChatList(
    @SerializedName("quickChat")
    val quickChats: List<QuickChat>
)
