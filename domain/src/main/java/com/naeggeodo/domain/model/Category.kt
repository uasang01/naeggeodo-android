package com.naeggeodo.domain.model

import com.google.gson.annotations.SerializedName

class Category(
    @SerializedName("idx")
    val idx: Int,
    @SerializedName("category")
    val category: String,
)