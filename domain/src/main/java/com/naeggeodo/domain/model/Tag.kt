package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("idx")
    val idx: Int,
    @SerializedName("msg")
    val tag: String
)