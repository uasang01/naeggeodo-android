package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class DeleteChat(
    val deleted: Boolean,
    @SerializedName("chatMain_id")
    val chatId: Int
)
