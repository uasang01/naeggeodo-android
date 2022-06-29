package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class ChatId(
    @SerializedName("chatMain_id")
    val chatMainId: Int
)
