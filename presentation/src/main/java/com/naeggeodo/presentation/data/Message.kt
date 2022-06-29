package com.naeggeodo.presentation.data

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("chatMain_id")
    val chatId: String,
    val contents: String,
    val nickname: String,
    val sender: String,
    val type: String,
    val target_id: String? = null
)